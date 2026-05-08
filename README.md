# Notification System - Spring Boot

## 🧠 Overview

This project is a simple Spring Boot application designed to demonstrate the core principles of Object-Oriented Programming (OOP) in a real-world scenario.

It implements a notification system capable of handling multiple notification types (e.g., Email and SMS) using a clean, extensible, and maintainable design.

## 🛠️ Tech Stack

- Java 17
- Spring Boot
- Maven
- Kafka
- Docker
- Kubernetes

---

## Local Distributed-Systems Lab

This project can run locally with Kafka so you can practice microservices and orchestration concepts before using cloud tools.

- Docker Compose lab: app + Kafka + Kafka UI
- Local Kubernetes lab: app Deployment + Kafka StatefulSet + Services + ConfigMap
- Kafka UI: inspect the `notification-events` topic and messages

Start with [docs/local-orchestration.md](docs/local-orchestration.md).

For the banking loan CRUD flow, use [docs/banking-loan-use-case.md](docs/banking-loan-use-case.md).

For CI/CD and local production simulation, use [docs/ci-cd.md](docs/ci-cd.md).

Useful local URLs:

- App: `http://localhost:8081/notifications/send`
- Kafka UI: `http://localhost:8082`
- Prometheus: `http://localhost:19090` when using the local CI/CD production simulation

---

## 🎯 Purpose

The goal of this project is to:

- Practice Object-Oriented Programming (OOP)
- Apply clean architecture principles
- Build a scalable and extensible system using Spring Boot
- Prepare for technical interviews by demonstrating real-world design thinking

---

## 🚀 Features

- Support for multiple notification types (Email, SMS)
- Abstract base class to define a common contract
- Polymorphic processing of notifications
- REST endpoint to trigger notifications
- Clean and modular project structure

---

## 🧠 OOP Principles Demonstrated

This project applies the four pillars of Object-Oriented Programming:

- **Encapsulation** → Private fields with controlled access
- **Abstraction** → Abstract class defining behavior
- **Inheritance** → Specialized notification types extend a base class
- **Polymorphism** → Different behaviors executed through a common interface

---

## 🏗️ Project Structure
src/main/java/com/example/notification

├── controller
│ └── NotificationController.java
│
├── service
│ └── NotificationService.java
│
├── model
│ ├── Notification.java
│ ├── EmailNotification.java
│ └── SmsNotification.java


---

## 🔄 Application Flow

1. The controller receives a request
2. A list of notifications is created
3. The service processes the list
4. Each notification executes its own behavior using polymorphism

---

## ▶️ How to Run

### 1. Clone the repository

git clone https://github.com/YOUR_USERNAME/notification-system-springboot.git

cd notification-system-springboot


### 2. Run the application

/mvnw spring-boot:run


### 3. Open in browser

http://localhost:8081/notifications/send


---

## 🧪 Example Output

Sending EMAIL: Hello Email
Sending SMS: Hello SMS


---

## 🔮 Future Improvements

- Implement Factory Pattern to remove object creation from controller
- Add validation and error handling
- Support additional notification types (Push, WhatsApp, etc.)
- Integrate with external messaging services

---

## 💡 Key Takeaway

This project demonstrates how OOP principles can be used to design flexible and scalable systems. By leveraging abstraction and polymorphism, new features can be added without modifying existing code.

---

## 👤 Author

Agustin Guadalupe

---

## 💬 Inspiration

"Programs must be written for people to read, and only incidentally for machines to execute." – Harold Abelson
