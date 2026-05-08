#!/usr/bin/env bash
set -Eeuo pipefail

APP_IMAGE="${APP_IMAGE:-notification-system}"
APP_TAG="${APP_TAG:-local}"
COMPOSE_PROJECT_NAME="${COMPOSE_PROJECT_NAME:-notification-system-prod}"
APP_PORT="${APP_PORT:-18081}"
KAFKA_UI_PORT="${KAFKA_UI_PORT:-18082}"
MYSQL_PORT="${MYSQL_PORT:-13306}"
KAFKA_PORT="${KAFKA_PORT:-19092}"
PROMETHEUS_PORT="${PROMETHEUS_PORT:-19090}"
KEEP_EXISTING=false

export APP_IMAGE APP_TAG COMPOSE_PROJECT_NAME APP_PORT KAFKA_UI_PORT MYSQL_PORT KAFKA_PORT PROMETHEUS_PORT

for arg in "$@"; do
  case "$arg" in
    --keep-existing)
      KEEP_EXISTING=true
      ;;
    *)
      echo "Unknown option: $arg" >&2
      echo "Usage: $0 [--keep-existing]" >&2
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

wait_for_prometheus_target() {
  local url="http://localhost:${PROMETHEUS_PORT}/api/v1/query?query=up%7Bjob%3D%22notification-system%22%7D"
  local attempts="${1:-20}"

  for attempt in $(seq 1 "$attempts"); do
    if curl -fsS "$url" | grep -Eq '"value":\[[^]]+,"1"\]'; then
      return 0
    fi

    printf 'Waiting for Prometheus target notification-system to be UP (%s/%s)\n' "$attempt" "$attempts"
    sleep 3
  done

  echo "Timed out waiting for Prometheus to scrape notification-system" >&2
  compose ps
  compose logs --tail=120 prometheus
  return 1
}

log "CD: checking Docker image ${APP_IMAGE}:${APP_TAG}"
docker image inspect "${APP_IMAGE}:${APP_TAG}" >/dev/null

log "CD: deploying production-like stack with Docker Compose"
if [[ "$KEEP_EXISTING" == false ]]; then
  compose down --remove-orphans
fi
compose up -d

log "CD: waiting for the app readiness probe"
wait_for_health "http://localhost:${APP_PORT}/actuator/health/readiness"

log "CD: running smoke request"
curl -fsS "http://localhost:${APP_PORT}/notifications/send"
printf '\n'

log "CD: waiting for Prometheus readiness"
wait_for_health "http://localhost:${PROMETHEUS_PORT}/-/ready" 20

log "CD: checking Prometheus scrape target"
wait_for_prometheus_target

log "CD completed successfully"
compose ps
