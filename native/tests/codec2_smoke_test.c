#include <stdio.h>
#include <stdint.h>

#include "codec2_stub.h"

int main(void) {
    int16_t pcm[SL_CODEC2_PCM_SAMPLES];
    for (int i = 0; i < SL_CODEC2_PCM_SAMPLES; ++i) {
        pcm[i] = (int16_t)(1000 + (i % 50));
    }
    uint8_t frame[SL_CODEC2_BYTES_PER_FRAME];
    int enc = sl_codec2_encode(pcm, SL_CODEC2_PCM_SAMPLES, frame, sizeof(frame));
    if (enc != SL_CODEC2_BYTES_PER_FRAME) {
        fprintf(stderr, "FAIL encode %d\n", enc);
        return 1;
    }
    int16_t out[SL_CODEC2_PCM_SAMPLES];
    int dec = sl_codec2_decode(frame, sizeof(frame), out, SL_CODEC2_PCM_SAMPLES);
    if (dec != SL_CODEC2_PCM_SAMPLES) {
        fprintf(stderr, "FAIL decode %d\n", dec);
        return 1;
    }
    int64_t energy = 0;
    for (int i = 0; i < SL_CODEC2_PCM_SAMPLES; ++i) {
        energy += out[i] < 0 ? -out[i] : out[i];
    }
    if (energy <= 0) {
        fprintf(stderr, "FAIL energy\n");
        return 1;
    }
    printf("PASS codec2_stub_length_energy\n");
    return 0;
}
