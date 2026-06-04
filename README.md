# Payment Transfer Service

## Project Overview

This project implements a basic payment transfer service for a digital banking system.
The service allows users to transfer funds between accounts while ensuring balance validation, transaction integrity, and audit logging.

---

- Users should be able to initiate a transfer by providing both the source and destination account IDs, along with the transfer amount.
- The system must validate that the source account has sufficient funds before processing the transfer.
- Implement comprehensive error handling to ensure reliability and security.
- Record each successful transaction accurately for audit and tracking purposes.

---

## Technology Stack

* Java 21
* Spring Boot 3.5.x
* Spring Data JPA (Hibernate)
* PostgreSQL
* Flyway for database migrations
* Maven

---

## Environment Setup

### Option 1 - Docker (Recommended)

If Docker is available, the infrastructure can be started using:

```bash
docker-compose up -d
```

This will automatically set up the PostgreSQL database.

---

### Option 2 - Manual Setup

If Docker is not available, create the database manually:

```sql
CREATE DATABASE test_db;
```

---

## Environment Variables

The application requires the following environment variables:

```text
DB_USERNAME=postgres
DB_PASSWORD=<your_password>
```

Alternatively, default values can be used in `application.yml` for local development.

---

## Build the Project

To build the application:

```bash
mvn clean install
```

---

## Run the Application

To start the Spring Boot application:

```bash
mvn spring-boot:run
```

---

## Database Migrations

Database schema is managed using Flyway.

Migration scripts are located in:

```
src/main/resources/db/migration
```

Migrations are executed automatically on application startup.
