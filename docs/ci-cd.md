# CI/CD Local Production Simulation

This project includes a small CI/CD setup that can run in GitHub Actions and locally on your machine.

## Pipeline Stages

1. **Continuous Integration**
   - Checkout code
   - Set up Java 17
   - Run `./mvnw --batch-mode clean verify`
   - Publish the built jar as a workflow artifact

2. **Package**
   - Build the Docker image from the verified jar
   - Tag the image with the commit SHA and `latest`
   - Inspect the image so the workflow fails if packaging is broken

3. **Local Continuous Delivery Simulation**
   - Build the same production-style Docker image locally
   - Deploy MySQL, Kafka, Kafka UI, and the app with Docker Compose
   - Wait for `/actuator/health/readiness`
   - Run a smoke request against `/notifications/send`

## Run The Full Pipeline Locally

```bash
./scripts/local-cicd.sh
```

This leaves the stack running so you can inspect it:

- App: `http://localhost:18081`
- Send notifications: `http://localhost:18081/notifications/send`
- Readiness: `http://localhost:18081/actuator/health/readiness`
- Kafka UI: `http://localhost:18082`

Stop it with:

```bash
./scripts/local-down.sh
```

If you only want to validate CI and build the image:

```bash
./scripts/local-cicd.sh --skip-deploy
```

If you want to force a clean Docker image rebuild:

```bash
./scripts/local-cicd.sh --no-cache
```

## GitHub Actions

The workflow lives in `.github/workflows/ci-cd.yml`.

It runs on:

- Pull requests targeting `main` or `master`
- Pushes to `main` or `master`
- Manual `workflow_dispatch`

Pull requests run CI and Docker packaging. Pushes also run the local production contract job, which exercises the same local pipeline path used by `./scripts/local-cicd.sh`.

## Production-Like Compose Override

`docker-compose.prod.yml` layers production-oriented behavior on top of the existing local Compose file:

- Uses an already-built application image
- Adds app readiness health checks
- Adds restart policies
- Keeps infrastructure service names the same as Kubernetes-style DNS names

This is still a local simulation, not cloud deployment. It gives you the feedback loop a production pipeline needs: tested code, immutable image, deployed environment, readiness check, and smoke test.

The simulation uses separate host ports so it can run beside your normal development stack:

- App: `18081`
- Kafka UI: `18082`
- MySQL: `13306`
- Kafka: `19092`

Override any of them when needed:

```bash
APP_PORT=8081 KAFKA_UI_PORT=8082 ./scripts/local-cicd.sh
```
