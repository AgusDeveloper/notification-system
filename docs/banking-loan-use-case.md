# Banking Loan Use Case

This project now models a simple digital-bank flow:

1. A customer is created as a bank user.
2. The customer requests a loan.
3. The application stores user and loan state in MySQL.
4. The application publishes notification events to Kafka.
5. Kafka UI lets you inspect the emitted events.

## Why These Tools

- MySQL is good for transactional CRUD: users, loans, balances, payments, and state that must be consistent.
- Kafka is good for events: `USER_CREATED`, `LOAN_REQUESTED`, `LOAN_UPDATED`, and notification workflows.
- DynamoDB is good for high-scale key-value or document access, for example fast lookup of user notification preferences or loan read models.
- Datomic is good for immutable historical facts, auditability, and temporal queries. It is interesting for learning, but it is less common as a first Java/Spring CRUD database.

For this learning step, the implemented source of truth is MySQL. Kafka carries events. DynamoDB Local is included as an optional local lab but is not yet wired into the app.

## Run Locally

Build and start everything:

```bash
./mvnw clean package
docker compose up --build
```

Open Kafka UI:

```text
http://localhost:8082
```

## User CRUD

Create a user:

```bash
curl -X POST http://localhost:8081/bank/users \
  -H "Content-Type: application/json" \
  -d '{
    "fullName": "Ana Lopez",
    "email": "ana.lopez@example.com",
    "phoneNumber": "+525512345678"
  }'
```

List users:

```bash
curl http://localhost:8081/bank/users
```

Get one user:

```bash
curl http://localhost:8081/bank/users/1
```

Update a user:

```bash
curl -X PUT http://localhost:8081/bank/users/1 \
  -H "Content-Type: application/json" \
  -d '{
    "fullName": "Ana Lopez Perez",
    "email": "ana.lopez@example.com",
    "phoneNumber": "+525587654321",
    "status": "ACTIVE"
  }'
```

Delete a user:

```bash
curl -X DELETE http://localhost:8081/bank/users/1
```

## Loan Flow

Create a loan application:

```bash
curl -X POST http://localhost:8081/bank/loans \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1,
    "amount": 25000.00,
    "termMonths": 24,
    "purpose": "Home office equipment"
  }'
```

List all loans:

```bash
curl http://localhost:8081/bank/loans
```

List loans for one user:

```bash
curl "http://localhost:8081/bank/loans?userId=1"
```

Approve a loan:

```bash
curl -X PUT http://localhost:8081/bank/loans/1 \
  -H "Content-Type: application/json" \
  -d '{
    "amount": 25000.00,
    "termMonths": 24,
    "purpose": "Home office equipment",
    "status": "APPROVED"
  }'
```

Delete a loan:

```bash
curl -X DELETE http://localhost:8081/bank/loans/1
```

## Validate Integration

After each create, update, or delete command:

1. Open Kafka UI at `http://localhost:8082`.
2. Select the `local` cluster.
3. Open the `notification-events` topic.
4. Confirm that events such as `USER_CREATED` or `LOAN_REQUESTED` appear.

You can also inspect MySQL:

```bash
docker exec -it notification-mysql mysql -u notification_user -pnotification_password notification_system
```

Useful SQL:

```sql
select * from bank_users;
select * from loan_applications;
```

## Architecture Notes

The synchronous path is:

```text
HTTP request -> Controller -> Service -> MySQL transaction -> Kafka event
```

The event path is:

```text
Spring Kafka producer -> Kafka topic -> future consumers
```

In a larger microservices design, the loan service would own loan state, a user/customer service would own customer profile data, and a notification service would consume Kafka events to send email, SMS, or push notifications.
