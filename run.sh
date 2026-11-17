#!/usr/bin/env bash
set -euo pipefail

cd "$(dirname "$0")"
JAR="target/penilaian-akademik-1.0.0.jar"

if [ ! -f "$JAR" ]; then
    echo "Jar belum ada. Jalankan: mvn clean package" >&2
    exit 1
fi

if [ -n "${NIRI_SOCKET:-}" ] || [ "${XDG_CURRENT_DESKTOP:-}" = "niri" ]; then
    export _JAVA_AWT_WM_NONREPARENTING=1
fi

exec java -jar "$JAR" "$@"
