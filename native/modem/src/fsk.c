#include "fsk_internal.h"

/* Map 7-bit FEC codewords into 4-FSK dibit symbols (MSB-first packing). */
int sl_fsk_bytes_to_symbols(const uint8_t *bytes, size_t byte_len, uint8_t *symbols, size_t symbols_cap) {
    /* Each byte contributes 4 symbols (8 bits). FEC codewords are 7-bit in a byte — still 4 symbols. */
    size_t need = byte_len * 4;
    if (symbols_cap < need) {
        return -1;
    }
    size_t out = 0;
    for (size_t i = 0; i < byte_len; ++i) {
        uint8_t b = bytes[i];
        symbols[out++] = (uint8_t)((b >> 6) & 0x3);
        symbols[out++] = (uint8_t)((b >> 4) & 0x3);
        symbols[out++] = (uint8_t)((b >> 2) & 0x3);
        symbols[out++] = (uint8_t)(b & 0x3);
    }
    return (int)out;
}

int sl_fsk_symbols_to_bytes(const uint8_t *symbols, size_t symbols_len, uint8_t *bytes, size_t bytes_cap) {
    if (symbols_len % 4 != 0) {
        return -1;
    }
    size_t need = symbols_len / 4;
    if (bytes_cap < need) {
        return -1;
    }
    for (size_t i = 0; i < need; ++i) {
        uint8_t b = 0;
        b |= (uint8_t)((symbols[i * 4 + 0] & 0x3) << 6);
        b |= (uint8_t)((symbols[i * 4 + 1] & 0x3) << 4);
        b |= (uint8_t)((symbols[i * 4 + 2] & 0x3) << 2);
        b |= (uint8_t)(symbols[i * 4 + 3] & 0x3);
        bytes[i] = b;
    }
    return (int)need;
}
