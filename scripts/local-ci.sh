#!/usr/bin/env bash
set -Eeuo pipefail

APP_IMAGE="${APP_IMAGE:-notification-system}"
APP_TAG="${APP_TAG:-local}"
NO_CACHE=false

for arg in "$@"; do
  case "$arg" in
    --no-cache)
      NO_CACHE=true
      ;;
    *)
      echo "Unknown option: $arg" >&2
      echo "Usage: $0 [--no-cache]" >&2
      exit 2
      ;;
  esac
done

log() {
  printf '\n[%s] %s\n' "$(date '+%H:%M:%S')" "$1"
}

log "CI: running Maven verification"
./mvnw --batch-mode clean verify

log "CI: building Docker image ${APP_IMAGE}:${APP_TAG}"
if [[ "$NO_CACHE" == true ]]; then
  docker build --no-cache --tag "${APP_IMAGE}:${APP_TAG}" .
else
  docker build --tag "${APP_IMAGE}:${APP_TAG}" .
fi

docker image inspect "${APP_IMAGE}:${APP_TAG}" >/dev/null

log "CI completed successfully"
