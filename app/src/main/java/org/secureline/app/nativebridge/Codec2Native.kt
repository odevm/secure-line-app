package org.secureline.app.nativebridge

object Codec2Native {
    const val PCM_SAMPLES = 160
    const val BYTES_PER_FRAME = 6

    init {
        System.loadLibrary("secureline_modem")
    }

    external fun encode(
        pcm: ShortArray,
        out: ByteArray,
    ): Int

    external fun decode(
        frames: ByteArray,
        pcmOut: ShortArray,
    ): Int
}
