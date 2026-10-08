# Testing

## Run locally

```bash
export JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home"
export ANDROID_HOME="$HOME/Library/Android/sdk"

./gradlew testDebugUnitTest detekt ktlintCheck assembleDebug
bash native/tests/run_host_modem_tests.sh
# Optional signed release (requires keystore.properties + secrets/*.p12):
./gradlew assembleRelease
```

## Layout

| Path | Purpose |
|------|---------|
| `app/src/test/` | JVM unit tests |
| `app/src/androidTest/` | Instrumented / Compose UI |
| `native/tests/` | Modem FEC + Codec2 glue smoke |

## Manual cellular checklist (`t25`)

Use two physical devices with SecureLine debug/release APKs. Sign off before calling voice track “production ready”.

- [ ] Install APK on device A and B
- [ ] Complete PIN onboarding offline (skip Google)
- [ ] Pair via QR (`secureline://pair`) — fingerprints match
- [ ] Place cellular call; both Enter Secure Mode
- [ ] Modem sync within 45s **or** SyncTimeout UX correct
- [ ] SAS icons/digits match → Matches enables PTT
- [ ] Does not match aborts secure mode; cellular call remains
- [ ] Encrypted SMS round-trip; mismatch fingerprint blocks send
- [ ] (Optional) Link Google → Drive backup passphrase round-trip
- [ ] Language EN ↔ AR + RTL smoke

Sign-off: date ____ / devices ____ / tester ____  
*(Deferred until manual lab — no `v0.1.0` GitHub Release until this is signed.)*

## Codec2 note

JNI uses a Codec2-compatible frame glue (`codec2_stub`) for length/energy CI. Fetch upstream for NDK port:

```bash
bash scripts/fetch_codec2.sh
```
