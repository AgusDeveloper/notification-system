# Local Orchestration Lab

This project is a small way to practice distributed-system ideas without AWS. The app is one service, Kafka is the message broker, and Kafka UI lets you inspect topics and events.

The Kafka container uses `bitnamilegacy/kafka:3.9.0` because the old `bitnami/kafka:3.9.0` tag no longer resolves from Docker Hub. This keeps the same local-learning configuration style while avoiding a missing image tag.

## Mental Model

- Container: a packaged process plus its runtime dependencies.
- Docker Compose: runs several containers together on one machine.
- Kubernetes: schedules containers and keeps the desired state running.
- Kafka: stores event streams. Your app publishes `NotificationSentEvent` messages to the `notification-events` topic.
- MySQL: stores current banking state such as users and loan applications.
- Service discovery: containers and pods call each other by stable service names like `kafka:9092`.

## Level 1: Run Only Kafka Locally

Use this when you want to run the Spring Boot app from your IDE.

```bash
docker compose up kafka kafka-ui
```

Run the app from the IDE or terminal. The default app config uses `localhost:9092`, so it can connect to Kafka exposed on your Mac.

```bash
./mvnw spring-boot:run
```

Test the flow:

```bash
curl http://localhost:8081/notifications/send
```

Open Kafka UI:

```text
http://localhost:8082
```

Look for the `notification-events` topic and inspect its messages.

## Level 2: Run App + MySQL + Kafka With Docker Compose

Build the jar first because the Dockerfile copies `target/*.jar`.

```bash
./mvnw clean package
docker compose up --build
```

Then call:

```bash
curl http://localhost:8081/notifications/send
```

In this mode the app container talks to Kafka using the Compose service name:

```text
KAFKA_BOOTSTRAP_SERVERS=kafka:9092
```

It also talks to MySQL using:

```text
SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/notification_system
```

## Level 3: Run App + Kafka In Local Kubernetes

This works with Docker Desktop Kubernetes or Minikube.

Build the image:

```bash
./mvnw clean package
docker build -t notification-system:1.0 .
```

If you use Minikube, load the image into the cluster:

```bash
minikube image load notification-system:1.0
```

Apply the manifests:

```bash
kubectl apply -f k8s/namespace.yaml
kubectl apply -f k8s/kafka.yaml
kubectl apply -f k8s/configmap.yaml
kubectl apply -f k8s/deployment.yaml
kubectl apply -f k8s/service.yaml
kubectl apply -f k8s/kafka-ui.yaml
```

Check the pods:

```bash
kubectl get pods -n development
```

Forward ports to your Mac:

```bash
kubectl port-forward -n development service/notification-system 8081:8081
kubectl port-forward -n development service/kafka-ui 8082:8080
```

Test again:

```bash
curl http://localhost:8081/notifications/send
```

## What To Notice

- `Deployment` describes stateless app replicas.
- `StatefulSet` gives Kafka stable identity and storage.
- `Service` gives pods a stable DNS name.
- `ConfigMap` injects environment-specific configuration.
- Readiness probes decide when traffic can be sent to a pod.
- Liveness probes decide when Kubernetes should restart a stuck pod.

## When To Choose Which Tool

- Use plain Maven when learning application code.
- Use Docker Compose when learning service collaboration on one machine.
- Use Kubernetes when learning production-style orchestration concepts.
- Use managed cloud services later when you want reliability, scaling, IAM, networking, and operations practice.

## Optional: DynamoDB Local

The Compose file also includes DynamoDB Local behind a profile. It is not connected to the Spring Boot app yet; use it when you want to practice key-value/document modeling separately.

```bash
docker compose --profile dynamodb up dynamodb-local
```

Local endpoint:

```text
http://localhost:8000
```
