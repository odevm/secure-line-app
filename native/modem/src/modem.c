#include "secureline_modem.h"

#include "fec_internal.h"
#include "fsk_internal.h"

int sl_modem_encode(const uint8_t *payload, size_t payload_len, uint8_t *symbols_out, size_t symbols_cap) {
    if (payload == NULL || symbols_out == NULL || payload_len == 0 || payload_len > SL_MODEM_MAX_PAYLOAD) {
        return -1;
    }
    uint8_t fec[SL_MODEM_MAX_PAYLOAD * 2];
    int fec_len = sl_fec_encode(payload, payload_len, fec, sizeof(fec));
    if (fec_len < 0) {
        return -1;
    }
    return sl_fsk_bytes_to_symbols(fec, (size_t)fec_len, symbols_out, symbols_cap);
}

int sl_modem_decode(const uint8_t *symbols, size_t symbols_len, uint8_t *payload_out, size_t payload_cap) {
    if (symbols == NULL || payload_out == NULL || symbols_len == 0) {
        return -1;
    }
    uint8_t fec[SL_MODEM_MAX_PAYLOAD * 2];
    int fec_len = sl_fsk_symbols_to_bytes(symbols, symbols_len, fec, sizeof(fec));
    if (fec_len < 0) {
        return -1;
    }
    return sl_fec_decode(fec, (size_t)fec_len, payload_out, payload_cap);
}

void sl_modem_corrupt_symbols(uint8_t *symbols, size_t symbols_len, int flip_rate_milli, uint32_t seed) {
    if (symbols == NULL || symbols_len == 0 || flip_rate_milli <= 0) {
        return;
    }
    uint32_t state = seed ? seed : 1u;
    for (size_t i = 0; i < symbols_len; ++i) {
        state = state * 1664525u + 1013904223u;
        if ((int)(state % 1000u) < flip_rate_milli) {
            symbols[i] = (uint8_t)((symbols[i] + 1) & 0x3);
        }
    }
}
