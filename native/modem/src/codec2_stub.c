#include "codec2_stub.h"

#include <string.h>

/*
 * Production path will call drowe67/codec2 (see scripts/fetch_codec2.sh).
 * This glue preserves length + non-zero energy contracts for CI/JNI smoke.
 */
int sl_codec2_encode(const int16_t *pcm, size_t pcm_samples, uint8_t *out, size_t out_cap) {
    if (pcm == NULL || out == NULL || pcm_samples < SL_CODEC2_PCM_SAMPLES || out_cap < SL_CODEC2_BYTES_PER_FRAME) {
        return -1;
    }
    uint32_t energy = 0;
    for (size_t i = 0; i < SL_CODEC2_PCM_SAMPLES; ++i) {
        int32_t s = pcm[i];
        energy += (uint32_t)(s < 0 ? -s : s);
    }
    out[0] = (uint8_t)((energy >> 24) & 0xFF);
    out[1] = (uint8_t)((energy >> 16) & 0xFF);
    out[2] = (uint8_t)((energy >> 8) & 0xFF);
    out[3] = (uint8_t)(energy & 0xFF);
    out[4] = 0xC2; /* marker */
    out[5] = 0x12; /* mode tag 1200-ish */
    return SL_CODEC2_BYTES_PER_FRAME;
}

int sl_codec2_decode(const uint8_t *in, size_t in_len, int16_t *pcm_out, size_t pcm_cap) {
    if (in == NULL || pcm_out == NULL || in_len < SL_CODEC2_BYTES_PER_FRAME || pcm_cap < SL_CODEC2_PCM_SAMPLES) {
        return -1;
    }
    if (in[4] != 0xC2) {
        return -1;
    }
    uint32_t energy =
        ((uint32_t)in[0] << 24) | ((uint32_t)in[1] << 16) | ((uint32_t)in[2] << 8) | (uint32_t)in[3];
    int16_t amp = (int16_t)(energy / SL_CODEC2_PCM_SAMPLES);
    if (amp == 0) {
        amp = 1;
    }
    for (int i = 0; i < SL_CODEC2_PCM_SAMPLES; ++i) {
        pcm_out[i] = amp;
    }
    return SL_CODEC2_PCM_SAMPLES;
}
