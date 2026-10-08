#include <jni.h>
#include <stdint.h>

#include "codec2_stub.h"
#include "secureline_modem.h"

JNIEXPORT jint JNICALL
Java_org_secureline_app_nativebridge_ModemNative_encode(
    JNIEnv *env,
    jclass clazz,
    jbyteArray payload,
    jbyteArray symbolsOut
) {
    (void)clazz;
    jsize payload_len = (*env)->GetArrayLength(env, payload);
    jsize symbols_cap = (*env)->GetArrayLength(env, symbolsOut);
    jbyte *payload_bytes = (*env)->GetByteArrayElements(env, payload, NULL);
    jbyte *symbols_bytes = (*env)->GetByteArrayElements(env, symbolsOut, NULL);
    int n = sl_modem_encode(
        (const uint8_t *)payload_bytes,
        (size_t)payload_len,
        (uint8_t *)symbols_bytes,
        (size_t)symbols_cap
    );
    (*env)->ReleaseByteArrayElements(env, payload, payload_bytes, JNI_ABORT);
    (*env)->ReleaseByteArrayElements(env, symbolsOut, symbols_bytes, 0);
    return n;
}

JNIEXPORT jint JNICALL
Java_org_secureline_app_nativebridge_ModemNative_decode(
    JNIEnv *env,
    jclass clazz,
    jbyteArray symbols,
    jint symbolsLen,
    jbyteArray payloadOut
) {
    (void)clazz;
    jsize payload_cap = (*env)->GetArrayLength(env, payloadOut);
    jbyte *symbols_bytes = (*env)->GetByteArrayElements(env, symbols, NULL);
    jbyte *payload_bytes = (*env)->GetByteArrayElements(env, payloadOut, NULL);
    int n = sl_modem_decode(
        (const uint8_t *)symbols_bytes,
        (size_t)symbolsLen,
        (uint8_t *)payload_bytes,
        (size_t)payload_cap
    );
    (*env)->ReleaseByteArrayElements(env, symbols, symbols_bytes, JNI_ABORT);
    (*env)->ReleaseByteArrayElements(env, payloadOut, payload_bytes, 0);
    return n;
}

JNIEXPORT jint JNICALL
Java_org_secureline_app_nativebridge_Codec2Native_encode(
    JNIEnv *env,
    jclass clazz,
    jshortArray pcm,
    jbyteArray out
) {
    (void)clazz;
    jsize pcm_len = (*env)->GetArrayLength(env, pcm);
    jsize out_cap = (*env)->GetArrayLength(env, out);
    jshort *pcm_els = (*env)->GetShortArrayElements(env, pcm, NULL);
    jbyte *out_els = (*env)->GetByteArrayElements(env, out, NULL);
    int n = sl_codec2_encode((const int16_t *)pcm_els, (size_t)pcm_len, (uint8_t *)out_els, (size_t)out_cap);
    (*env)->ReleaseShortArrayElements(env, pcm, pcm_els, JNI_ABORT);
    (*env)->ReleaseByteArrayElements(env, out, out_els, 0);
    return n;
}

JNIEXPORT jint JNICALL
Java_org_secureline_app_nativebridge_Codec2Native_decode(
    JNIEnv *env,
    jclass clazz,
    jbyteArray frames,
    jshortArray pcmOut
) {
    (void)clazz;
    jsize in_len = (*env)->GetArrayLength(env, frames);
    jsize pcm_cap = (*env)->GetArrayLength(env, pcmOut);
    jbyte *in_els = (*env)->GetByteArrayElements(env, frames, NULL);
    jshort *pcm_els = (*env)->GetShortArrayElements(env, pcmOut, NULL);
    int n = sl_codec2_decode((const uint8_t *)in_els, (size_t)in_len, (int16_t *)pcm_els, (size_t)pcm_cap);
    (*env)->ReleaseByteArrayElements(env, frames, in_els, JNI_ABORT);
    (*env)->ReleaseShortArrayElements(env, pcmOut, pcm_els, 0);
    return n;
}
