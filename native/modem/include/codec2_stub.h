#ifndef SECURELINE_CODEC2_STUB_H
#define SECURELINE_CODEC2_STUB_H

#include <stddef.h>
#include <stdint.h>

#ifdef __cplusplus
extern "C" {
#endif

/* Matches Codec2 mode 1200-ish framing for glue tests: 160 PCM samples @ 8 kHz / 20 ms. */
#define SL_CODEC2_PCM_SAMPLES 160
#define SL_CODEC2_BYTES_PER_FRAME 6

int sl_codec2_encode(const int16_t *pcm, size_t pcm_samples, uint8_t *out, size_t out_cap);
int sl_codec2_decode(const uint8_t *in, size_t in_len, int16_t *pcm_out, size_t pcm_cap);

#ifdef __cplusplus
}
#endif

#endif
