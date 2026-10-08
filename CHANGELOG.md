# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Added

- Repository foundation: `.gitignore`, Apache-2.0 license, OSS metafiles
- Android application shell (`org.secureline.app`) with Compose + Hilt
- Conventional Commits guide
- JUnit 5 / MockK / Truth test harness and `docs/TESTING.md`
- Detekt, ktlint, and GitHub Actions CI (lint + unit tests)
- Navigation stubs for all primary screens (instrumented route smoke)
- EN/AR strings, RTL-aware locale override, and `docs/I18N.md`
- `AppConfig` / BuildConfig wiring for `SECURELINE_*` public URLs (`docs/CONFIG.md`)
- `secureline://pair` URI parse/generate with lid validation (reject PSK)
- Local identity (UUID + X25519/Ed25519) and AES-256-GCM with nonce-reuse rejection
- NDK 4-FSK modem + Hamming(7,4) FEC spike with host bit-exact round-trip tests
- TOFU handshake frame codec (`SLH1`) and ContactBook multi-number merge
- SAS derive (4 emoji + 2×4 digits) and retention PERSISTENT/TIMED/EPHEMERAL rules
- PIN vault wrap/unwrap for SecureDb key + Unlock screen stub
- SemVer update-tag compare helper and encrypted SMS `SL1:` framer

## [0.1.0] - 2026-10-09

### Added

- Initial public project skeleton at SemVer `0.1.0`
