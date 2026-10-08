#include "fec_internal.h"

/* Hamming(7,4): data nibble → 7-bit codeword in low bits. */
static uint8_t hamming74_encode_nibble(uint8_t nibble) {
    uint8_t d0 = (nibble >> 0) & 1;
    uint8_t d1 = (nibble >> 1) & 1;
    uint8_t d2 = (nibble >> 2) & 1;
    uint8_t d3 = (nibble >> 3) & 1;
    uint8_t p0 = d0 ^ d1 ^ d3;
    uint8_t p1 = d0 ^ d2 ^ d3;
    uint8_t p2 = d1 ^ d2 ^ d3;
    return (uint8_t)((p0) | (p1 << 1) | (d0 << 2) | (p2 << 3) | (d1 << 4) | (d2 << 5) | (d3 << 6));
}

static int hamming74_decode_nibble(uint8_t codeword, uint8_t *out_nibble) {
    uint8_t b0 = (codeword >> 0) & 1;
    uint8_t b1 = (codeword >> 1) & 1;
    uint8_t b2 = (codeword >> 2) & 1;
    uint8_t b3 = (codeword >> 3) & 1;
    uint8_t b4 = (codeword >> 4) & 1;
    uint8_t b5 = (codeword >> 5) & 1;
    uint8_t b6 = (codeword >> 6) & 1;
    uint8_t s0 = b0 ^ b2 ^ b4 ^ b6;
    uint8_t s1 = b1 ^ b2 ^ b5 ^ b6;
    uint8_t s2 = b3 ^ b4 ^ b5 ^ b6;
    uint8_t syndrome = (uint8_t)(s0 | (s1 << 1) | (s2 << 2));
    if (syndrome != 0) {
        codeword ^= (uint8_t)(1u << (syndrome - 1));
        b0 = (codeword >> 0) & 1;
        b1 = (codeword >> 1) & 1;
        b2 = (codeword >> 2) & 1;
        b3 = (codeword >> 3) & 1;
        b4 = (codeword >> 4) & 1;
        b5 = (codeword >> 5) & 1;
        b6 = (codeword >> 6) & 1;
    }
    *out_nibble = (uint8_t)(b2 | (b4 << 1) | (b5 << 2) | (b6 << 3));
    return 0;
}

int sl_fec_encode(const uint8_t *in, size_t in_len, uint8_t *out, size_t out_cap) {
    size_t need = in_len * 2;
    if (out_cap < need) {
        return -1;
    }
    for (size_t i = 0; i < in_len; ++i) {
        out[i * 2] = hamming74_encode_nibble((uint8_t)(in[i] & 0x0F));
        out[i * 2 + 1] = hamming74_encode_nibble((uint8_t)((in[i] >> 4) & 0x0F));
    }
    return (int)need;
}

int sl_fec_decode(const uint8_t *in, size_t in_len, uint8_t *out, size_t out_cap) {
    if (in_len % 2 != 0) {
        return -1;
    }
    size_t out_len = in_len / 2;
    if (out_cap < out_len) {
        return -1;
    }
    for (size_t i = 0; i < out_len; ++i) {
        uint8_t lo = 0;
        uint8_t hi = 0;
        if (hamming74_decode_nibble(in[i * 2], &lo) != 0) {
            return -1;
        }
        if (hamming74_decode_nibble(in[i * 2 + 1], &hi) != 0) {
            return -1;
        }
        out[i] = (uint8_t)((hi << 4) | lo);
    }
    return (int)out_len;
}
