#ifndef SECURELINE_FSK_INTERNAL_H
#define SECURELINE_FSK_INTERNAL_H

#include <stddef.h>
#include <stdint.h>

int sl_fsk_bytes_to_symbols(const uint8_t *bytes, size_t byte_len, uint8_t *symbols, size_t symbols_cap);
int sl_fsk_symbols_to_bytes(const uint8_t *symbols, size_t symbols_len, uint8_t *bytes, size_t bytes_cap);

#endif
