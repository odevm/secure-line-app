# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Added

- Optional Google account linking (Drive-gated) + FCM-ready without Google
- Analytics/Crashlytics consent gate and telemetry denylist scrubber
- SMS chat UI with number picker and fingerprint-mismatch send block
- Codec2 JNI glue (frame-compatible stub) + host length/energy smoke
- PTT secure pipeline orchestration (Codec2 → AES-GCM → modem)
- Device audio profile JSON load/save (`profiles/default.json`)
- Optional root audio gate with non-root fallback
- Firebase project wiring (`secureline-odevm`) via local `google-services.json` + CI secrets
- Release signing via local PKCS12 + GitHub Secrets (`docs/SECRETS.md`)
- Dual-device cellular checklist in `docs/TESTING.md` (sign-off pending manual lab)
- Docs: `SECRETS`, `ROOT`, expanded `TESTING` / `CONFIG`

### Changed

- Public GitHub Contact defaults to `odevm/secure-line-app`
- Telegram Contact set to [t.me/secureline_app](https://t.me/secureline_app)

## [0.1.0] - 2026-10-09

### Added

- Initial public project skeleton at SemVer `0.1.0` (app module, OSS meta, CI harness)
