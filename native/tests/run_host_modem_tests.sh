#!/usr/bin/env bash
set -euo pipefail
ROOT="$(cd "$(dirname "$0")/../.." && pwd)"
BUILD="$ROOT/native/tests/build"
mkdir -p "$BUILD"
cc -std=c11 -Wall -Wextra -O2 \
  -I"$ROOT/native/modem/include" \
  -I"$ROOT/native/modem/src" \
  "$ROOT/native/modem/src/modem.c" \
  "$ROOT/native/modem/src/fec.c" \
  "$ROOT/native/modem/src/fsk.c" \
  "$ROOT/native/tests/modem_roundtrip_test.c" \
  -o "$BUILD/modem_roundtrip_test"
"$BUILD/modem_roundtrip_test"

cc -std=c11 -Wall -Wextra -O2 \
  -I"$ROOT/native/modem/include" \
  "$ROOT/native/modem/src/codec2_stub.c" \
  "$ROOT/native/tests/codec2_smoke_test.c" \
  -o "$BUILD/codec2_smoke_test"
"$BUILD/codec2_smoke_test"
echo "Host modem + codec2 smoke tests OK"
