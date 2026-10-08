#ifndef SECURELINE_FEC_INTERNAL_H
#define SECURELINE_FEC_INTERNAL_H

#include <stddef.h>
#include <stdint.h>

int sl_fec_encode(const uint8_t *in, size_t in_len, uint8_t *out, size_t out_cap);
int sl_fec_decode(const uint8_t *in, size_t in_len, uint8_t *out, size_t out_cap);

#endif
