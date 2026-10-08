# Root / advanced audio

SecureLine never escalates silently.

| Status | Meaning |
|--------|---------|
| Not rooted | Non-root speakerphone path only |
| Available | `su` detected; user must opt in |
| Granted | User confirmed; optional direct uplink/downlink path |

When root is denied or revoked, behavior matches the non-root path (`RootAudioGate.useRootAudioPath() == false`).
