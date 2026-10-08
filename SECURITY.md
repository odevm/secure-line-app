# Security Policy

## Supported versions

Only the latest tagged release receives security fixes while the project is pre-1.0.

## Reporting a vulnerability

Please report security issues **privately** (do not open a public GitHub issue with exploit details).

Include:

- Affected version / commit
- Impact and reproduction steps
- Whether keys, messages, or backup material are exposed

We will acknowledge receipt when possible and coordinate disclosure.

## Hard rules for contributors

- Never commit `google-services.json`, keystores, or signing passwords
- Never log private keys, session keys, SAS secrets, or message plaintext
- Never send E2EE content in FCM, Analytics, or Crashlytics payloads
- Treat QR / deep-link payloads as untrusted input
