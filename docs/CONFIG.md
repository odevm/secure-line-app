# Build configuration

Developer wiring for `BuildConfig` / `AppConfig`. Operator howto for creating GitHub/Telegram accounts stays **out** of this repo (see project plan only).

## Environment / `local.properties` keys

| Key | BuildConfig field | Purpose |
|-----|-------------------|---------|
| `SECURELINE_GITHUB_REPO_URL` | `GITHUB_REPO_URL` | Public repo URL for Contact |
| `SECURELINE_GITHUB_OWNER` | `GITHUB_OWNER` | Releases API owner |
| `SECURELINE_GITHUB_REPO` | `GITHUB_REPO` | Releases API repo name |
| `SECURELINE_TELEGRAM_CHANNEL_URL` | `TELEGRAM_CHANNEL_URL` | Contact Telegram link (production: `https://t.me/secureline_app`) |
| `SECURELINE_FIREBASE_ENABLED` | `FIREBASE_ENABLED` | `true`/`false` — Firebase features |

Empty strings are valid: Contact rows show “Not configured”; update check no-ops safely.

## Injection order

1. Process environment (`SECURELINE_*`)
2. Else `local.properties` same key
3. Else empty / `false`

Never commit real secrets. `google-services.json` is CI-injected only.
