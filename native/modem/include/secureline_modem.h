#ifndef SECURELINE_MODEM_H
#define SECURELINE_MODEM_H

#include <stddef.h>
#include <stdint.h>

#ifdef __cplusplus
extern "C" {
#endif

/** Max payload bytes for a single modem frame (spike). */
#define SL_MODEM_MAX_PAYLOAD 256

/**
 * Encode payload with Hamming(7,4) FEC then map to 4-FSK symbols (0..3).
 * @return number of symbols written, or -1 on error.
 */
int sl_modem_encode(const uint8_t *payload, size_t payload_len, uint8_t *symbols_out, size_t symbols_cap);

/**
 * Decode 4-FSK symbols → FEC → payload.
 * @return number of payload bytes written, or -1 on error / uncorrectable.
 */
int sl_modem_decode(const uint8_t *symbols, size_t symbols_len, uint8_t *payload_out, size_t payload_cap);

/**
 * Inject random dibit flips into symbols for BER lab tests.
 * flip_rate_milli: flips per 1000 symbols (e.g. 10 = 1%).
 */
void sl_modem_corrupt_symbols(uint8_t *symbols, size_t symbols_len, int flip_rate_milli, uint32_t seed);

#ifdef __cplusplus
}
#endif

#endif /* SECURELINE_MODEM_H */
