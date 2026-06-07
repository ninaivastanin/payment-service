# Payment Transfer Service

## Project Overview

This project implements a Basic Payment Transfer Service for a digital banking system.
The service allows users to transfer funds between accounts while ensuring balance validation, transaction integrity, and audit logging.

---

## Functional Requirements

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

---

## API Endpoints

### Accounts

#### Create Account

```http
POST /api/accounts
```

Example request:

```json
{
  "accountNumber": "RS123456789",
  "initialBalance": 1000,
  "currency": "EUR"
}
```

#### Get All Accounts

```http
GET /api/accounts
```

#### Get Account By Id

```http
GET /api/accounts/{id}
```

---

### Transfers

#### Transfer Funds

```http
POST /api/transfers
```

Example request:

```json
{
  "sourceAccountId": 1,
  "destinationAccountId": 2,
  "amount": 100
}
```

#### Get All Transfers

```http
GET /api/transfers
```

#### Get Transfers For Account

```http
GET /api/transfers/account/{accountId}
```

---

## Validation Rules

### Account Creation

* Account number must be unique.
* Initial balance cannot be negative.
* Currency is required.

### Transfer Processing

* Transfer amount must be greater than zero.
* Source account must exist.
* Destination account must exist.
* Source account must have sufficient funds.
* Transfers between the same account are not allowed.

---

## Error Handling

The application provides centralized exception handling and returns appropriate error responses for common failure scenarios:

* Account not found
* Duplicate account number
* Invalid initial balance
* Invalid transfer amount
* Insufficient funds

---

## Running Tests

To execute all tests:

```bash
mvn test
```

---

## Postman Collection

A ready-to-use Postman collection is available in:

```text
postman/payment-service.postman_collection.json
```

