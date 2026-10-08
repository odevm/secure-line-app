#include <jni.h>
#include <stdint.h>

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
