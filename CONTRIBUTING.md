# Contributing to SecureLine

Thanks for helping improve SecureLine.

## Development setup

1. Clone the repository.
2. Copy `local.properties.example` → `local.properties` and set `sdk.dir` if needed.
3. Open the project in Android Studio or build with `./gradlew`.
4. Do **not** commit `local.properties`, keystores, or `app/google-services.json`.

## Workflow

1. Create a focused branch or commit per logical change (see [docs/COMMIT_GUIDE.md](docs/COMMIT_GUIDE.md)).
2. Add or update tests for domain/crypto/parser changes.
3. Run locally:

   ```bash
   ./gradlew test detekt ktlintCheck
   ```

4. Update `[Unreleased]` in `CHANGELOG.md` when user-visible behavior changes.
5. Open a PR with a clear summary and test plan.

## Code standards

- Kotlin, clean architecture (`ui` → `domain` → `data` / `platform` / `native`)
- No secrets or plaintext E2EE content in logs, Analytics, Crashlytics, or FCM bodies
- Prefer small PRs that map to plan tasks (`t01`–`t32`)

## Security reports

See [SECURITY.md](SECURITY.md).
