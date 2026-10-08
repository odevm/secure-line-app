package org.secureline.app.nativebridge

object ModemNative {
    init {
        System.loadLibrary("secureline_modem")
    }

    external fun encode(
        payload: ByteArray,
        symbolsOut: ByteArray,
    ): Int

    external fun decode(
        symbols: ByteArray,
        symbolsLen: Int,
        payloadOut: ByteArray,
    ): Int
}
