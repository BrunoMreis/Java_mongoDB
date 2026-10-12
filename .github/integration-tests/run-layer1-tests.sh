#!/usr/bin/env bash
set -u

cd "$(dirname "$0")/../.."

if ! docker info >/dev/null 2>&1; then
  echo "ERROR: Docker is not running. Start Docker and try again."
  exit 1
fi

mvn verify -Dgroups=Layer1
test_exit=$?

echo
if [ "$test_exit" -eq 0 ]; then
  echo "✅ Layer 1 integration tests PASSED"
else
  echo "❌ Layer 1 integration tests FAILED"
fi
exit "$test_exit"
