# Recurring Payments API 
Recurring Payments API is a system for managing recurring subscriptions and automated payments. 
It allows users to subscribe to plans, process payments automatically, and manage their active subscriptions. 
The system also includes an admin panel for monitoring transactions and user subscriptions. 
## 🚀 Technologies 
- **Java 21**, **Spring Boot 3.x**, **Spring Security** 
- **Spring Data JPA**, **PostgreSQL**, **Flyway** 
- **Docker & Docker Compose** 
## 📌 Features 
- **User Authentication** (Spring Security, session-based) 
- **Subscription Management** (`POST /subscriptions`, `GET /subscriptions`, `DELETE /subscriptions/{id}`) 
- **Automated Payments** (`GET /payments`) 
- **Admin Panel** (`GET /admin/subscriptions`, `GET /admin/payments`) 
## 🛠️ Setup 

1️⃣ **Clone the repo** 
``` git clone git@github.com:LukaszSzumiec/recurring-payments-api.git && cd recurring-payments-api ``` 

2️⃣ **Run with Docker** 
``` docker-compose up ``` 

3️⃣ **Run locally** 
``` mvn spring-boot:run ``` 

## 📜 License MIT License
