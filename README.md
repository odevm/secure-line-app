# SecureLine

Open-source Android app for **end-to-end encrypted cellular voice** (PTT over the voice channel, no data required during the call) and **encrypted SMS**.

**Status:** early development (SemVer `0.1.0`). Sideload only — not distributed on Google Play.

## Features (planned / in progress)

- Offline-first identity (`localUserId` + SecureLine keypair)
- QR / `secureline://pair` public-key pairing; TOFU + SAS verification
- Encrypted SMS between paired contacts
- Optional Google Sign-In for Drive backup only
- FCM / Analytics / Crashlytics when online (no Google account required)
- App lock (PIN + optional biometric) with SQLCipher at rest
- EN / AR localization

## Requirements

- Android Studio Ladybug+ (or equivalent JDK 17)
- Android SDK 35; minSdk 26
- Two physical devices recommended for voice-modem testing

## Build

```bash
cp local.properties.example local.properties
# Ensure sdk.dir points at your Android SDK (Android Studio usually writes this)

export JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home"
./gradlew :app:assembleDebug
```

Release builds that enable Firebase expect `GOOGLE_SERVICES_JSON` / `GOOGLE_SERVICES_JSON_BASE64` in CI (never commit `app/google-services.json`).

## Versioning

- Semantic Versioning — see [semver.org](https://semver.org/)
- Changelog — [Keep a Changelog](https://keepachangelog.com/en/1.1.0/) in [CHANGELOG.md](CHANGELOG.md)
- GitHub release tags: `vMAJOR.MINOR.PATCH`

## Contributing

See [CONTRIBUTING.md](CONTRIBUTING.md) and [docs/COMMIT_GUIDE.md](docs/COMMIT_GUIDE.md).

## License

Apache License 2.0 — see [LICENSE](LICENSE).

## Security

See [SECURITY.md](SECURITY.md) and threat-model notes under `docs/` as they land.
