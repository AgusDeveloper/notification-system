#!/usr/bin/env bash
set -Eeuo pipefail

APP_IMAGE="${APP_IMAGE:-notification-system}"
APP_TAG="${APP_TAG:-local}"
COMPOSE_PROJECT_NAME="${COMPOSE_PROJECT_NAME:-notification-system-prod}"
APP_PORT="${APP_PORT:-18081}"
KAFKA_UI_PORT="${KAFKA_UI_PORT:-18082}"
MYSQL_PORT="${MYSQL_PORT:-13306}"
KAFKA_PORT="${KAFKA_PORT:-19092}"
NO_CACHE=false
SKIP_DEPLOY=false

export APP_IMAGE APP_TAG COMPOSE_PROJECT_NAME APP_PORT KAFKA_UI_PORT MYSQL_PORT KAFKA_PORT

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

compose() {
  docker compose \
    --project-name "$COMPOSE_PROJECT_NAME" \
    -f docker-compose.yml \
    -f docker-compose.prod.yml \
    "$@"
}

wait_for_health() {
  local url="$1"
  local attempts="${2:-60}"

  for attempt in $(seq 1 "$attempts"); do
    if curl -fsS "$url" >/dev/null; then
      return 0
    fi

    printf 'Waiting for %s (%s/%s)\n' "$url" "$attempt" "$attempts"
    sleep 5
  done

  echo "Timed out waiting for $url" >&2
  compose ps
  compose logs --tail=120 notification-system
  return 1
}

log "CI: running Maven verification"
./mvnw --batch-mode clean verify

log "CI: building production Docker image ${APP_IMAGE}:${APP_TAG}"
if [[ "$NO_CACHE" == true ]]; then
  docker build --no-cache --tag "${APP_IMAGE}:${APP_TAG}" .
else
  docker build --tag "${APP_IMAGE}:${APP_TAG}" .
fi

docker image inspect "${APP_IMAGE}:${APP_TAG}" >/dev/null

if [[ "$SKIP_DEPLOY" == true ]]; then
  log "CD: skipped local deployment"
  exit 0
fi

log "CD: deploying production-like stack with Docker Compose"
compose down --remove-orphans
APP_IMAGE="$APP_IMAGE" APP_TAG="$APP_TAG" compose up -d

log "CD: waiting for the app readiness probe"
wait_for_health "http://localhost:${APP_PORT}/actuator/health/readiness"

log "CD: running smoke request"
curl -fsS "http://localhost:${APP_PORT}/notifications/send"
printf '\n'

log "Pipeline completed successfully"
compose ps
