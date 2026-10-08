# Threat model (draft)

## Assets

- Private identity keys, session keys, SAS material, decrypted SMS/voice
- ContactBook bindings (`peerLocalUserId` ↔ phones)

## Adversaries

- Passive / network observer: hears modem tones / SMS ciphertext; must not learn plaintext
- Active MitM on first TOFU session: mitigated by mandatory SAS before `verified=true`; QR preferred
- Malicious QR: schema validation + fingerprint confirm
- Compromised device without unlock: PIN/biometric gate + SQLCipher (Keystore wrap)

## Non-goals

- Hiding that a call/SMS occurred
- Erasing OEM SMS DB copies when not default SMS app
