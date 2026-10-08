# Protocol (draft)

## Pair URI

`secureline://pair?v=1&lid=<uuid>&x25519=<b64url>&ed25519=<b64url>&fp=<short>&fid=<optional>&nums=<optional_b64url_csv>`

Reject: missing `lid`, any `psk`, wrong scheme/host, unsupported `v`.

## Handshake frame (`SLH1`)

See `HandshakeFrameCodec`: magic `SLH1`, version 1, flags, UUID, X25519(32), Ed25519(32), optional firebaseUid.

## SAS

HKDF-SHA256 info `secureline-sas-v1` → 4 emoji indices into the fixed 25-symbol table in `SasVerifier` + two 4-digit groups.

## SMS (`SL1:`)

Base64url frame: version | peer UUID | chunk index/count | nonce | AES-GCM ciphertext. AAD binds peer id and chunk metadata.

## Modem (spike)

4-FSK symbols + Hamming(7,4) FEC — see `native/modem`. Audio waveform mapping lands with later modem tasks.
