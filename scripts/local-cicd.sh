#!/usr/bin/env bash
set -Eeuo pipefail

NO_CACHE=false
SKIP_DEPLOY=false

for arg in "$@"; do
  case "$arg" in
    --no-cache)
      NO_CACHE=true
      ;;
    --skip-deploy)
      SKIP_DEPLOY=true
      ;;
    *)
      echo "Unknown option: $arg" >&2
      echo "Usage: $0 [--no-cache] [--skip-deploy]" >&2
      exit 2
      ;;
  esac
done

log() {
  printf '\n[%s] %s\n' "$(date '+%H:%M:%S')" "$1"
}

if [[ "$NO_CACHE" == true ]]; then
  ./scripts/local-ci.sh --no-cache
else
  ./scripts/local-ci.sh
fi

if [[ "$SKIP_DEPLOY" == true ]]; then
  log "CD: skipped local deployment"
  exit 0
fi

./scripts/local-cd.sh

log "Pipeline completed successfully"
