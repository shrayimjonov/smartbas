# 📘 Remittance Outside Service

Spring Boot microservice that integrates with the Leskar remittance platform to manage cross-border transfers. It provides REST APIs to get countries and fees, create persons, create/confirm/cancel transfers, check status, and payout operations. The service uses Redis for token storage, ClickHouse (via Flyway) for logging, and Kafka for streaming logs.

## 🧭 Table of Contents

- [Features](#features)
- [Tech Stack](#technologies)
- [Architecture](#architecture)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Clone & Build](#clone--build)
  - [Configuration](#configuration)
  - [Run](#run)
  - [Docker](#docker)
- [API Documentation](#api-documentation)
- [API Endpoints](#api-endpoints)
- [Response Wrapper](#response-wrapper)
- [Database & Migrations](#database_and_migrations)
- [Logging & Monitoring](#logging--monitoring)
- [Troubleshooting](#troubleshooting)

<a id="features"></a>
## 🔐 Features

- Integration with Leskar remittance platform
- Transfer lifecycle: create, confirm, cancel, payout, status, info
- Country and fee discovery endpoints
- Redis-backed token storage and refresh
- ClickHouse logging and Flyway migrations
- Kafka-based log streaming; masking of sensitive payload fields
- OpenAPI/Swagger UI

<a id="technologies"></a>
#### ⚙️ Technologies Used

|    Component     |              Technology              | Status | Version |
|:----------------:|:------------------------------------:|:------:|:-------:|
|     Backend      |      Java 17, Spring Boot 3.4.11     |   ✅    |  3.4.11 |
|       Port       |               7101 (default)         |   ✅    |    -    |
|    Build Tool    |                Maven                 |   ✅    |  3.9.11 |
|     Database     |              ClickHouse              |   ✅    |  Latest |
|    Migrations    |                Flyway                |   ✅    | 10.20.1 |
|     API Docs     |           OpenAPI/Swagger            |   ✅    |  3.4.11 |
|   Event Stream   |                Kafka                 |   ✅    | Latest  |
|  Token Storage   |                Redis                 |   ✅    | Latest  |
|  Configuration   |           Vault (Spring Cloud)       |   ✅    |  4.3.0  |
|  Serialization   |              Jackson                 |   ✅    | Latest  |
| API Testing Tool |          Postman / Insomnia          |   ✅    |    -    |
|    IDE/Editor    |            IntelliJ IDEA             |   ✅    |    -    |
|   Architecture   |             Microservice             |   ✅    |    -    |
|  Infrastructure  |          Docker + Docker Compose     |   ✅    |    -    |

![Java](https://img.shields.io/badge/Java-17-blue?logo=java)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.4.11-green?logo=spring)
![Maven](https://img.shields.io/badge/Maven-3.9.11-red?logo=apache-maven)
![Docker](https://img.shields.io/badge/Docker-24.0.5-blue?logo=docker)
![Kafka](https://img.shields.io/badge/Kafka-Latest-white?logo=apache-kafka)
![Redis](https://img.shields.io/badge/Redis-Latest-red?logo=redis)

<a id="architecture"></a>
## 🧱 Architecture

The service follows a **layered microservices architecture** with clear separation of concerns:

```
┌─────────────────────────────────────────────────────────────┐
│                    Client Applications                       │
└─────────────────────────────────────────────────────────────┘
                              ↓
┌─────────────────────────────────────────────────────────────┐
│                   REST API Controller                        │
│              (LeskarController - @RestController)            │
└─────────────────────────────────────────────────────────────┘
                              ↓
┌─────────────────────────────────────────────────────────────┐
│                  Business Logic Layer                        │
│    (LeskarService, TokenStoreService, IntegrationService)   │
└─────────────────────────────────────────────────────────────┘
                              ↓
┌─────────────────────────────────────────────────────────────┐
│               Integration & External Services                │
│  (LeskarIntegrationClient, Connector, WebClient)            │
└─────────────────────────────────────────────────────────────┘
        ↓                      ↓                      ↓
    ┌──────────┐         ┌─────────┐         ┌──────────────┐
    │ Leskar   │         │ Redis   │         │ ClickHouse   │
    │ API      │         │ (Tokens)│         │ (Logging)    │
    └──────────┘         └─────────┘         └──────────────┘
                              ↓
                         ┌─────────┐
                         │ Kafka   │
                         │ (Events)│
                         └─────────┘
```

### Core Components

#### **Controllers**
- **LeskarController** (`endpoint/LeskarController.java`)
  - Handles all HTTP requests for remittance operations
  - Base path: `/api/v1/integration/leskar/transfer`
  - Wraps responses in `ResultData` for consistency

#### **Services**
- **LeskarService** - Business logic for Leskar operations
- **IntegrationService** - Abstraction layer for external integrations
- **TokenStoreService** - Token lifecycle management (refresh, cache, expiry)

#### **Integration Layer**
- **LeskarIntegrationClient** - Implements encrypted communication with Leskar
  - Handles AES-256 encryption for sensitive operations
  - Manages HMAC-SHA256 signatures for request/response verification
  - Automatic token refresh on expiry
- **IntegrationRouter** - Routes to appropriate integration client
- **Connector** - WebClient wrapper for making HTTP requests

#### **Data Models**
- **DTOs** in `aggregate/dto/leskarintegration/` - Structured request/response objects
- **ResultData** - Standard API response wrapper
- **ApiExchangeLog** - Event log model for ClickHouse

#### **Repositories**
- **TokenStoreRepository** - Redis repository for JWT tokens
- **ClickhouseRepository** - JDBC repository for event logging

#### **Logging & AOP**
- **ApiExchangeLogger** - Central logging component
  - Sends logs to Kafka for stream processing
  - Masks sensitive fields before logging
  - Truncates large payloads (max 5000 chars)
- **RestControllerLoggingFilter** - Intercepts inbound HTTP requests/responses
- **WebclientLoggingFilter** - Intercepts outbound Leskar API calls

#### **Configuration**
- **ApplicationProperties** - Externalized config for credentials and endpoints
- **WebClientConfiguration** - WebClient bean setup with thread pools
- **ClickhouseConfiguration** - ClickHouse JDBC and HikariCP setup
- **KafkaConfig** - Kafka producer/consumer configuration
- **RedisConfigure** - Redis template and connection factory
- **OpenAPIConfiguration** - Swagger/OpenAPI documentation setup

<a id="getting-started"></a>
## 🚀 Getting Started

<a id="prerequisites"></a>
### Prerequisites

- Java 17+
- Maven 3.8+
- Running instances or access to: Redis, Kafka, ClickHouse
- Optional: Vault for secrets

<a id="clone--build"></a>
### Clone & Build

```bash
git clone <repository-url>
cd remittance-outside
mvn clean install -DskipTests
```

<a id="configuration"></a>
### Configuration

Configuration is supplied via environment variables and/or Vault. Sensitive keys (credentials, endpoints, tokens) are intentionally omitted from this README. Contact DevOps or see secure configuration storage for details. Default server port is `7101`.

<a id="run"></a>
### Run

Run locally with Maven:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=test
```

Or run the built jar:

```bash
java -jar target/remittanceoutside-0.0.1-SNAPSHOT.jar --spring.profiles.active=test
```

<a id="docker"></a>
### Docker

`Dockerfile` builds the app in a multi-stage image. Example usage:

```bash
# Build image
docker build -t remittance-outside:local .

# Run container (map port 7101)
docker run --rm -p 7101:7101 --name remittance-outside \
  -e SPRING_PROFILES_ACTIVE=test \
  remittance-outside:local
```

`docker-compose.yml` exposes `7101:7101` for service `api`. Set the `image` field to the built image tag (for example, `remittance-outside:local`) before running compose.

<a id="api-documentation"></a>
## 📑 API Documentation

- Swagger UI: http://localhost:7101/swagger-ui/index.html
- OpenAPI JSON: http://localhost:7101/v3/api-docs

<a id="api-endpoints"></a>
## 🧩 API Endpoints

Base path: `/api/v1/integration/leskar/transfer` (all `POST`)

- `/countries` — get supported country list
- `/fees` — get fee list (body: `LeskarGetFeeRequest`)
- `/person/create` — create person (body: `LeskarCreatePersonRequest`)
- `/create` — create transfer (body: `LeskarCreateTransferRequest`)
- `/confirm` — confirm transfer (body: `LeskarConfirmTransferRequest`)
- `/cancel` — cancel transfer (body: `LeskarCancelTransferRequest`)
- `/person/info` — get person info (body: `LeskarPersonInfoRequest`)
- `/payout` — initiate payout (body: `LeskarPayoutRequest`)
- `/payout/confirm` — confirm payout (body: `LeskarPayoutConfirmRequest`)
- `/status` — get transfer status (body: `LeskarTransferStatusRequest`)
- `/info` — get transfer info (body: `LeskarTransferInfoRequest`)

<a id="response-wrapper"></a>
## 📦 Response Wrapper

All responses are wrapped in `ResultData<T>` with fields like `isSuccess`, `status`, `result`, `error`, and `timeStamp`. See `aggregate/model/ResultData.java`.

<a id="database_and_migrations"></a>
## 🗄️ Database & Migrations

- ClickHouse JDBC URL is configurable via env vars (see Configuration).
- Flyway is enabled with migrations located at `classpath:db/clickhouse/migration` (e.g., `V1__create_kafka_table.sql`).

<a id="logging--monitoring"></a>
## 📈 Logging & Monitoring

- Inbound/outbound API exchange logs are produced and can be sent to Kafka and stored in ClickHouse.
- Sensitive keys are masked according to `integration.logging-sensitive-data.sensitive-body-keys`.

## 🛠️ Troubleshooting

- Swagger UI not accessible: ensure app runs on port 7101 and dependencies are available.
- ClickHouse/Flyway errors: verify ClickHouse env variables and network reachability.
- Redis/Kafka connection issues: check host/port and credentials, confirm services are reachable from the container/host.


- **test** — Default profile, uses environment variables from Vault
- **local** — Local development profile with hardcoded values
- **prod** — Production profile with Vault integration

The profiles correspond to configuration files:
- `src/main/resources/application.yml` (base config)
- `src/main/resources/application-test.yml` (test profile)
- `src/main/resources/application-local.yml` (local profile)
- `src/main/resources/application-prod.yml` (production profile)

<a id="gs-run"></a>
### Run

#### Using Maven

```bash
# Run with test profile (default)
mvn spring-boot:run

# Run with local profile
mvn spring-boot:run -Dspring-boot.run.profiles=local

# Run with production profile
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

#### Using Java JAR

```bash
# Build first
mvn clean package

# Run with default (test) profile
java -jar target/remittanceoutside-0.0.1-SNAPSHOT.jar

# Run with local profile
java -jar target/remittanceoutside-0.0.1-SNAPSHOT.jar --spring.profiles.active=local

# Run with prod profile
java -jar target/remittanceoutside-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
```

#### Verify Application Started

```bash
# Check if service is running
curl http://localhost:7101/v3/api-docs | jq .

# Access Swagger UI
open http://localhost:7101/swagger-ui/index.html
```

<a id="gs-docker"></a>
### Docker

#### Build Docker Image

```bash
# Build the JAR first
mvn clean package -DskipTests

# Build Docker image
docker build -t davrbank/remittanceoutside:latest .

# Tag for registry
docker tag davrbank/remittanceoutside:latest davrbank/remittanceoutside:1.0.0
```

#### Run with Docker Compose

```bash
# Start all services
docker-compose up -d

# View logs
docker-compose logs -f remittance-outside

# Stop all services
docker-compose down

# Clean up (remove volumes)
docker-compose down -v
```

<a id="api-documentation"></a>
## 📚 API Documentation

OpenAPI/Swagger documentation is automatically available once the service is running.

### 🔗 Swagger UI
- `http://localhost:7101/swagger-ui/index.html`

### 🔗 OpenAPI JSON
- `http://localhost:7101/v3/api-docs`

### 🔗 OpenAPI YAML
- `http://localhost:7101/v3/api-docs.yaml`

**Note:** If a servlet context-path is configured (e.g., `/remittance`), the URLs will be:
- `http://localhost:7101/remittance/swagger-ui/index.html`
- `http://localhost:7101/remittance/v3/api-docs`

<a id="api-endpoints"></a>
## 🔗 API Endpoints

**Base path:** `/api/v1/integration/leskar/transfer`

All endpoints return a `ResultData<T>` wrapper with standardized response format.

---

<a id="transfer-management"></a>
### 💳 Transfer Management

#### Create Transfer

```http
POST /api/v1/integration/leskar/transfer/create
```

Creates a new remittance transfer request.

**Request Body:**
```json
{
  "source_doc_number": "DOC001",
  "source_account_number": "1234567890",
  "target_account_number": "9876543210",
  "originator_id": "ORIG123",
  "beneficiary_id": "BEN456",
  "purpose": "Personal remittance",
  "sof": "Salary",
  "transfer_type": "SWIFT",
  "target_country": "US",
  "target_currency": "USD",
  "source_currency": "UZS",
  "source_amount": 1000000,
  "additional": [
    {
      "key": "custom_field",
      "value": "custom_value"
    }
  ]
}
```

**Response:**
```json
{
  "successCode": 0,
  "isSuccess": true,
  "status": "SUCCESS",
  "result": {
    "signature": "hash...",
    "state": true,
    "response_time": "2025-12-10T10:30:00Z",
    "payload": {
      "status": "CREATED",
      "transfer_id": "TRF001"
    }
  },
  "timeStamp": 1733824200000
}
```

**Status Codes:**
- `200 OK` - Transfer created successfully
- `400 Bad Request` - Invalid input
- `401 Unauthorized` - Authentication failed
- `502 Bad Gateway` - Leskar API error

---

#### Confirm Transfer

```http
POST /api/v1/integration/leskar/transfer/confirm
```

Confirms a previously created transfer for processing.

**Request Body:**
```json
{
  "transfer_id": "TRF001",
  "callback_uri": "https://mybank.com/callback/transfer"
}
```

**Response:**
```json
{
  "successCode": 0,
  "isSuccess": true,
  "status": "SUCCESS",
  "result": {
    "payload": {
      "status": "CONFIRMED"
    }
  },
  "timeStamp": 1733824200000
}
```

---

#### Cancel Transfer

```http
POST /api/v1/integration/leskar/transfer/cancel
```

Cancels a pending transfer.

**Request Body:**
```json
{
  "transfer_id": "TRF001"
}
```

**Response:**
```json
{
  "successCode": 0,
  "isSuccess": true,
  "status": "SUCCESS",
  "result": {
    "payload": {
      "status": "CANCELLED"
    }
  },
  "timeStamp": 1733824200000
}
```

---

#### Check Transfer Status

```http
POST /api/v1/integration/leskar/transfer/status
```

Retrieves current status of a transfer.

**Request Body:**
```json
{
  "transfer_id": "TRF001"
}
```

**Response:**
```json
{
  "successCode": 0,
  "isSuccess": true,
  "status": "SUCCESS",
  "result": {
    "payload": {
      "status": "PENDING"
    }
  },
  "timeStamp": 1733824200000
}
```

---

#### Get Transfer Information

```http
POST /api/v1/integration/leskar/transfer/info
```

Retrieves detailed information about a transfer.

**Request Body:**
```json
{
  "transfer_id": "TRF001"
}
```

**Response:**
```json
{
  "successCode": 0,
  "isSuccess": true,
  "status": "SUCCESS",
  "result": {
    "payload": {
      "transfer_id": "TRF001",
      "status": "PENDING",
      "transfer_type": "SWIFT",
      "target_country": "US",
      "target_currency": "USD",
      "source_currency": "UZS",
      "source_amount": 1000000,
      "target_amount": 95.23,
      "rate": 0.00009523,
      "fees": [
        {
          "type": "TRANSFER_FEE",
          "amount": 5.00,
          "currency": "USD",
          "percent": 0.0
        }
      ]
    }
  },
  "timeStamp": 1733824200000
}
```

---

<a id="country--fee-information"></a>
### 🌍 Country & Fee Information

#### Get Available Countries

```http
POST /api/v1/integration/leskar/transfer/countries
```

Retrieves list of supported countries and available transfer types.

**Response:**
```json
{
  "successCode": 0,
  "isSuccess": true,
  "status": "SUCCESS",
  "result": {
    "payload": [
      {
        "country": "US",
        "available_types": [
          {
            "transfer_type": "SWIFT",
            "source_currency": "UZS",
            "target_currency": "USD",
            "limits": {
              "amount_min": 100,
              "amount_max": 10000
            }
          },
          {
            "transfer_type": "ACH",
            "source_currency": "UZS",
            "target_currency": "USD",
            "limits": {
              "amount_min": 50,
              "amount_max": 5000
            }
          }
        ]
      }
    ]
  },
  "timeStamp": 1733824200000
}
```

---

#### Get Transfer Fees

```http
POST /api/v1/integration/leskar/transfer/fees
```

Calculates fees for a potential transfer based on corridor and amount.

**Request Body:**
```json
{
  "source_currency": "UZS",
  "target_currency": "USD",
  "source_amount": 1000000,
  "transfer_type": "SWIFT",
  "target_country": "US",
  "originator_type": "INDIVIDUAL",
  "beneficiary_type": "INDIVIDUAL"
}
```

**Response:**
```json
{
  "successCode": 0,
  "isSuccess": true,
  "status": "SUCCESS",
  "result": {
    "payload": {
      "source_currency": "UZS",
      "target_currency": "USD",
      "source_amount": 1000000,
      "target_amount": 95.23,
      "rate": 0.00009523,
      "fees": [
        {
          "type": "TRANSFER_FEE",
          "amount": 5.00,
          "currency": "USD",
          "percent": 0.0
        },
        {
          "type": "CORRESPONDENT_BANK_FEE",
          "amount": 15.00,
          "currency": "USD",
          "percent": 0.0
        }
      ],
      "required_fields": {
        "beneficiary": ["account_number", "bank_code"],
        "originator": ["account_number"]
      }
    }
  },
  "timeStamp": 1733824200000
}
```

---

<a id="person-management"></a>
### 👤 Person Management

#### Create Person (Beneficiary/Originator)

```http
POST /api/v1/integration/leskar/transfer/person/create
```

Creates a new person record (encrypted request). This endpoint uses encrypted payload.

**Request Body:**
```json
{
  "first_name": "John",
  "middle_name": "Michael",
  "last_name": "Doe",
  "dob": "1990-01-15",
  "citizenship": "US",
  "occupation": "Engineer",
  "document": {
    "type": "PASSPORT",
    "number": "12345678",
    "expiry_date": "2030-01-15"
  },
  "address": {
    "line1": "123 Main St",
    "line2": "Apt 4B",
    "city": "New York",
    "state": "NY",
    "postcode": "10001",
    "country": "US"
  },
  "additional": [
    {
      "key": "phone",
      "value": "+1-555-1234"
    }
  ]
}
```

**Response:**
```json
{
  "successCode": 0,
  "isSuccess": true,
  "status": "SUCCESS",
  "result": {
    "payload": {
      "person_id": "PRS123456"
    }
  },
  "timeStamp": 1733824200000
}
```

**Note:** The request body will be encrypted with AES-256 using the partner key, and signed with HMAC-SHA256 using the agent secret.

---

#### Get Person Information

```http
POST /api/v1/integration/leskar/transfer/person/info
```

Retrieves information about a previously created person.

**Request Body:**
```json
{
  "person_id": "PRS123456"
}
```

**Response:**
```json
{
  "successCode": 0,
  "isSuccess": true,
  "status": "SUCCESS",
  "result": {
    "payload": {
      "person_id": "PRS123456",
      "person_status": "ACTIVE",
      "person_created": "2025-12-10T10:00:00Z",
      "encrypted_data": {
        "first_name": "John",
        "middle_name": "Michael",
        "last_name": "Doe",
        "dob": "1990-01-15",
        "citizenship": "US",
        "occupation": "Engineer",
        "document": {
          "type": "PASSPORT",
          "number": "12345678",
          "expiry_date": "2030-01-15"
        },
        "address": {
          "line1": "123 Main St",
          "line2": "Apt 4B",
          "city": "New York",
          "state": "NY",
          "postcode": "10001",
          "country": "US"
        }
      }
    }
  },
  "timeStamp": 1733824200000
}
```

---

#### Payout (Initiate Payment to Beneficiary)

```http
POST /api/v1/integration/leskar/transfer/payout
```

Initiates payout to the beneficiary.

**Request Body:**
```json
{
  "transfer_id": "TRF001",
  "beneficiary_id": "BEN456"
}
```

**Response:**
```json
{
  "successCode": 0,
  "isSuccess": true,
  "status": "SUCCESS",
  "result": {
    "payload": {
      "status": "PAYOUT_INITIATED"
    }
  },
  "timeStamp": 1733824200000
}
```

---

#### Confirm Payout

```http
POST /api/v1/integration/leskar/transfer/payout/confirm
```

Confirms the payout operation.

**Request Body:**
```json
{
  "transfer_id": "TRF001"
}
```

**Response:**
```json
{
  "successCode": 0,
  "isSuccess": true,
  "status": "SUCCESS",
  "result": {
    "payload": {
      "status": "PAYOUT_CONFIRMED"
    }
  },
  "timeStamp": 1733824200000
}
```

---

<a id="response-wrapper"></a>
## 📦 Response Wrapper

All API responses are wrapped in a standardized `ResultData` object for consistency.

### Success Response Structure

```json
{
  "successCode": 0,
  "isSuccess": true,
  "status": "SUCCESS",
  "result": {},
  "timeStamp": 1733824200000,
  "count": 1
}
```

**Fields:**
- `successCode` (Integer): 0 for success, -1 for error
- `isSuccess` (Boolean): true for success, false for error
- `status` (String): "SUCCESS", "FAILED", etc.
- `result` (Object): The actual response payload (type-dependent)
- `timeStamp` (Long): Unix timestamp in milliseconds
- `count` (Integer): Number of items (for list responses)

### Error Response Structure

```json
{
  "successCode": -1,
  "isSuccess": false,
  "status": "FAILED",
  "error": {
    "code": 400,
    "message": "Invalid transfer amount"
  },
  "timeStamp": 1733824200000
}
```

**Error Fields:**
- `code` (Integer): HTTP status code
- `message` (String): Human-readable error message

### Common Error Codes

| Code | Message                              | Cause                              |
|------|--------------------------------------|-----------------------------------|
| 400  | Invalid input                        | Malformed request body             |
| 401  | Unauthorized                         | Authentication/token failed        |
| 402  | Signature verification failed        | HMAC signature mismatch            |
| 404  | Transfer not found                   | Invalid transfer_id                |
| 502  | Bad Gateway                          | Leskar API unreachable             |
| 503  | Service Unavailable                  | Redis/Kafka/ClickHouse down        |
| 504  | Gateway Timeout                      | Leskar API timeout                 |

---

<a id="database_and_migrations"></a>
## 🗄 Database & Migrations

### ClickHouse Schema

Database migrations are managed by Flyway and located in:
```
src/main/resources/db/clickhouse/migration/
```

<a id="logging--monitoring"></a>
## 📊 Logging & Monitoring

### Logging Architecture

```
REST Request
    ↓
RestControllerLoggingFilter (captures inbound)
    ↓
LeskarController
    ↓
LeskarService / IntegrationService
    ↓
LeskarIntegrationClient
    ↓
WebclientLoggingFilter (captures outbound to Leskar)
    ↓
Leskar API
    ↓
Response → ApiExchangeLogger → Kafka → ClickHouse
```

### Log Events

**Four event types are logged:**

1. **INBOUND** - Client → Service requests
2. **OUTBOUND** - Service → Client responses
3. **THIRD_PARTY_REQUEST** - Service → Leskar requests
4. **THIRD_PARTY_RESPONSE** - Leskar → Service responses

### Event ID Tracing

Every request is assigned a unique `eventId` (UUID) that flows through:
- MDC (Mapped Diagnostic Context) for logging
- REST response headers
- Kafka messages
- ClickHouse logs

This enables end-to-end request tracing.

### Sensitive Data Masking

Configure sensitive fields to mask in logs via `application.yml`:

```yaml
integration:
  logging-sensitive-data:
    sensitive-body-keys:
      - password
      - token
      - secret
      - agent_secret
      - account_number
      - card_number
```

Any field matching these keys will be replaced with `******` before logging.

---

<a id="security"></a>
## 🔐 Security

### Authentication & Authorization

- **Bearer Token:** All Leskar API calls use Bearer JWT tokens
- **Token Refresh:** Automatic token refresh when expired
- **Token Caching:** Tokens stored in Redis with TTL

### Encryption

- **AES-256-CBC:** Encrypts sensitive request payloads for `person/create`
- **Random IV:** Each request uses a fresh 16-byte initialization vector
- **Key Derivation:** Encryption key = SHA256(partner_key)

### Signature Verification

- **HMAC-SHA256:** All Leskar requests and responses are signed
- **Request:** Signature = HMAC-SHA256(payload_json, agent_secret)
- **Response:** Service verifies response signature matches expected value
- **Replay Protection:** Signature verification prevents tampering


### Version Management

- **API Version:** `/api/v1/...`
- **Application Version:** Set in `pom.xml` (currently `0.0.1-SNAPSHOT`)
- **Spring Boot Version:** 3.4.11
- **Java Version:** 17

---

<a id="contact"></a>
## 📬 Contact

- **Organization:** Davrbank
- **Project:** Remittance Outside Service
- **Lead Developer:** Urunov Khamdamboy
- **Developer:** Rayimjonov Shukhratjon

---

## 📝 License

This project is proprietary to Davrbank.

## 🤝 Contributing

For contributions, please contact the development team.

---

**Last Updated:** December 10, 2025  
**Version:** 0.0.1-SNAPSHOT  
**Spring Boot:** 3.4.11  
**Java:** 17
