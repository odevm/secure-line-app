#!/usr/bin/env bash
# Optional: fetch upstream Codec2 for future real-lib linkage.
set -euo pipefail
ROOT="$(cd "$(dirname "$0")/.." && pwd)"
DEST="$ROOT/native/third_party/codec2"
if [ -d "$DEST/.git" ]; then
  echo "Codec2 already present at $DEST"
  exit 0
fi
mkdir -p "$ROOT/native/third_party"
git clone --depth 1 --branch 1.2.0 https://github.com/drowe67/codec2.git "$DEST"
echo "Fetched Codec2 1.2.0 — current app uses JNI stub with Codec2-compatible frame API until NDK port lands."
