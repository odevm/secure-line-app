# Secrets & local credentials

Never commit `secrets/`, `keystore.properties`, `app/google-services.json`, or `local.properties`.

## Local (gitignored)

| Path | Purpose |
|------|---------|
| `secrets/SIGNING_NOTES.txt` | Passwords, aliases, Firebase IDs, GitHub secret checklist |
| `secrets/secureline-release.p12` | Release signing keystore |
| `secrets/*.base64` | Handy copies for pasting into GitHub Secrets |
| `keystore.properties` | Gradle signing config |
| `app/google-services.json` | Firebase Android client config |
| `local.properties` | SDK path + `SECURELINE_*` overrides |

## GitHub Actions secrets

| Secret | Value |
|--------|-------|
| `SIGNING_KEYSTORE_BASE64` | Base64 of `secrets/secureline-release.p12` |
| `SIGNING_KEY_ALIAS` | `secureline` |
| `SIGNING_STORE_PASSWORD` | From `SIGNING_NOTES.txt` |
| `SIGNING_KEY_PASSWORD` | Same as store password (PKCS12) |
| `GOOGLE_SERVICES_JSON_BASE64` | Base64 of `app/google-services.json` |

CI unit/lint jobs run with Firebase **off** unless `GOOGLE_SERVICES_JSON_BASE64` is set. Signed release job runs only when signing secrets exist.

## Firebase project

Created for this app: **secureline-odevm**  
Console: https://console.firebase.google.com/project/secureline-odevm/overview  

Still enable in Console (one-time): Authentication → Google provider.
