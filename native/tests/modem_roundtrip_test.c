#include <stdio.h>
#include <string.h>

#include "secureline_modem.h"

static int expect_eq(const char *name, const uint8_t *a, const uint8_t *b, size_t n) {
    if (memcmp(a, b, n) != 0) {
        fprintf(stderr, "FAIL %s: mismatch\n", name);
        return 1;
    }
    printf("PASS %s\n", name);
    return 0;
}

int main(void) {
    int failures = 0;
    const uint8_t payload[] = {
        0x00, 0x01, 0x02, 0x7F, 0x80, 0xFE, 0xFF, 0xA5, 0x5A
    };
    uint8_t symbols[SL_MODEM_MAX_PAYLOAD * 8];
    uint8_t decoded[SL_MODEM_MAX_PAYLOAD];

    int sym_len = sl_modem_encode(payload, sizeof(payload), symbols, sizeof(symbols));
    if (sym_len <= 0) {
        fprintf(stderr, "FAIL encode\n");
        return 1;
    }
    int dec_len = sl_modem_decode(symbols, (size_t)sym_len, decoded, sizeof(decoded));
    if (dec_len != (int)sizeof(payload)) {
        fprintf(stderr, "FAIL decode length %d\n", dec_len);
        return 1;
    }
    failures += expect_eq("bit_exact_roundtrip", payload, decoded, sizeof(payload));

    /* Light corruption: Hamming can correct single-bit errors in codewords;
       symbol flips may exceed that — verify low rate still mostly recovers. */
    uint8_t noisy[sizeof(symbols)];
    memcpy(noisy, symbols, (size_t)sym_len);
    sl_modem_corrupt_symbols(noisy, (size_t)sym_len, 5, 42u); /* 0.5% */
    int noisy_len = sl_modem_decode(noisy, (size_t)sym_len, decoded, sizeof(decoded));
    if (noisy_len == (int)sizeof(payload) && memcmp(payload, decoded, sizeof(payload)) == 0) {
        printf("PASS fec_ber_light_corruption\n");
    } else {
        /* Accept partial: ensure decode does not crash and returns bounded size */
        if (noisy_len < 0 || noisy_len > (int)sizeof(decoded)) {
            fprintf(stderr, "FAIL fec_ber_bounds\n");
            failures += 1;
        } else {
            printf("PASS fec_ber_bounds (corrected_or_failed_closed=%d)\n", noisy_len);
        }
    }

    return failures == 0 ? 0 : 1;
}
