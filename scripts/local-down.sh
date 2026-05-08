#!/usr/bin/env bash
set -Eeuo pipefail

COMPOSE_PROJECT_NAME="${COMPOSE_PROJECT_NAME:-notification-system-prod}"
APP_PORT="${APP_PORT:-18081}"
KAFKA_UI_PORT="${KAFKA_UI_PORT:-18082}"
MYSQL_PORT="${MYSQL_PORT:-13306}"
KAFKA_PORT="${KAFKA_PORT:-19092}"
PROMETHEUS_PORT="${PROMETHEUS_PORT:-19090}"

export COMPOSE_PROJECT_NAME APP_PORT KAFKA_UI_PORT MYSQL_PORT KAFKA_PORT PROMETHEUS_PORT

docker compose \
  --project-name "$COMPOSE_PROJECT_NAME" \
  -f docker-compose.yml \
  -f docker-compose.prod.yml \
  down "$@"
