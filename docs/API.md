# API index (draft)

| Module | Entry points |
|--------|----------------|
| Pairing URI | `PairUriParser.parse` / `generate` |
| Handshake | `HandshakeFrameCodec.pack` / `unpack` |
| Crypto | `SecureLineCrypto.*`, `NonceTracker` |
| Contacts | `ContactBook.upsert` / `findByPhone` |
| SAS | `SasVerifier.derive` / `matches` |
| Retention | `RetentionEngine` |
| Lock | `PinVault.wrap` / `unwrap` |
| SMS | `SmsFramer.encryptAndChunk` / `decryptAndReassemble` |
| Update | `SemVer.parse`, `UpdateChecker.isNewer` |
| Backup | `BackupEnvelope.encrypt` / `decrypt` |
| FCM contract | `FcmPayloadContract.isAllowed` |
| Session | `SecureSessionController` |
| Modem JNI | `ModemNative.encode` / `decode` |
| Codec2 JNI | `Codec2Native.encode` / `decode` |
| Account | `AccountController` |
| Telemetry | `TelemetryScrubber` |
| SMS send gate | `SmsSendGate.decide` |
| PTT | `PttPipeline` |
| Device profile | `DeviceProfileStore` |
| Root audio | `RootAudioGate` |
