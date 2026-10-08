# Testing

SecureLine uses a test pyramid: fast JVM unit tests for domain/crypto/parsers, instrumented Compose tests for navigation and critical UI, and native tests for modem/FEC/Codec2.

## Run locally

```bash
export JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home"
export ANDROID_HOME="$HOME/Library/Android/sdk"

./gradlew testDebugUnitTest
./gradlew detekt ktlintCheck
# Device or emulator required:
./gradlew connectedDebugAndroidTest
```

## Layout

| Path | Purpose |
|------|---------|
| `app/src/test/` | JVM unit tests (JUnit 5, MockK, Truth) |
| `app/src/androidTest/` | Instrumented / Compose UI tests |
| `native/tests/` | Modem / FEC / Codec2 (when native lands) |

## Coverage expectations

- Domain crypto, URI/QR parsers, SMS framer, retention, SAS: unit tests required before merge
- Navigation stubs and unlock/SAS/PTT state: instrumented smoke
- Dual-device cellular modem: manual checklist in this file (Track F / `t25`) — do not mark voice track done until signed off once

## Manual cellular checklist (`t25`)

- [ ] Two physical devices with SecureLine debug APK
- [ ] Cellular call connected; both enter Secure Mode
- [ ] Modem sync succeeds within 45s (or SyncTimeout UX verified)
- [ ] SAS icons/digits match; Matches enables PTT
- [ ] Does not match aborts secure mode; cellular call remains
- [ ] Encrypted SMS round-trip between paired contacts

Sign-off: date ____ / devices ____ / tester ____
