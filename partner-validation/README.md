# PartnerValidationService

## About
`PartnerValidationService` is a Spring Boot payment-validation microservice. It validates partner transfer requests, manages merchant, merchant-account, and validation audit-log data in PostgreSQL.

In the broader payment flow described for this service, it sits in the path:

`third party -> gateway -> PartnerValidationService -> gateway -> davrhub -> NCI (ABS)`

Based on the current codebase, the implemented validation flow checks:

- merchant existence and active status
- outgoing merchant account existence
- merchant ownership of the outgoing account
- outgoing account active status
- amount greater than zero
- operation allowance from `allowed_operations`
- operation type allowance from `allowed_operations`
- branch code presence on the merchant account
- branch code match between request and merchant account
- branch allowance from `allowed_operations`

## Tech Stack

### Runtime and Build

| Component   | Version | Notes |
|-------------|---------|-------|
| Server port | `8081`  |  |
| Java        | `21`    | From `pom.xml` property `java.version` |
| Spring Boot | `3.4.12` | Parent BOM and managed dependency versions |
| Maven       | `3.9`   | Maven Wrapper is included in the repo |

### Dependencies from `pom.xml`

| Dependency | Version | Scope |
|----------|---------|-------|
| `org.springframework.boot:spring-boot-starter-web` | managed by Spring Boot `3.4.12` | main |
| `org.springframework.boot:spring-boot-starter-webflux` | managed by Spring Boot `3.4.12` | main |
| `org.springframework.boot:spring-boot-starter-validation` | managed by Spring Boot `3.4.12` | main |
| `org.springframework.boot:spring-boot-starter-data-jpa` | managed by Spring Boot `3.4.12` | main |
| `org.springframework.boot:spring-boot-starter-jdbc` | managed by Spring Boot `3.4.12` | main |
| `org.flywaydb:flyway-core` | managed by Spring Boot `3.4.12` | main |
| `org.flywaydb:flyway-database-postgresql` | managed by Spring Boot `3.4.12` | main |
| `org.postgresql:postgresql` | managed by Spring Boot `3.4.12` | runtime |
| `org.mapstruct:mapstruct` | `1.6.3` | main |
| `org.springdoc:springdoc-openapi-starter-webmvc-ui` | `2.8.8` | main |
| `org.springframework.cloud:spring-cloud-starter-vault-config` | managed by Spring Cloud `2024.0.3` | main |
| `org.projectlombok:lombok` | `1.18.38` | optional / annotation processing |
| `org.springframework.boot:spring-boot-starter-test` | managed by Spring Boot `3.4.12` | test |
| `org.springframework.boot:spring-boot-testcontainers` | managed by Spring Boot `3.4.12` | test |
| `io.projectreactor:reactor-test` | managed by Spring Boot `3.4.12` | test |
| `com.squareup.okhttp3:mockwebserver` | `5.3.0` | test |
| `org.testcontainers:junit-jupiter` | managed by Testcontainers BOM `1.21.3` | test |
| `org.testcontainers:postgresql` | managed by Testcontainers BOM `1.21.3` | test |

## Prerequisites

- Java `21`
- Maven Wrapper (`mvnw` / `mvnw.cmd`) or a compatible local Maven installation
- PostgreSQL

Notes:

- Profile-specific configuration files (datasource, Vault, gateway credentials) were removed from this portfolio copy;
  provide your own `application-<profile>.yml` or environment variables.
- `docker-compose.yml` is included for container builds.
- Test dependencies (Testcontainers, MockWebServer, reactor-test) are declared, but the test sources are not part of this copy.

## API Reference

### Common API Conventions

- Base path: `/api/v1/partner-validation-service`
- Common success envelope:

```json
{
  "success": true,
  "data": {},
  "error": null
}
```

- Common error envelope:

```json
{
  "success": false,
  "data": null,
  "error": {
    "timestamp": "2026-03-02T12:34:56.789Z",
    "status": 400,
    "error": "Bad Request",
    "message": "Validation message",
    "correlationId": "7f0f2d4a-6827-4d6c-9264-1da8e3d0b8fd"
  }
}
```

- `X-Correlation-ID`:
  - optional request header
  - if missing, the service generates one
  - the response header also contains `X-Correlation-ID`

Business validation failures for `POST /validate` are returned in the response body with HTTP `200`, using `data.success=false`. They are not currently mapped to HTTP `422` by the implemented validation flow.

---

### POST /api/v1/partner-validation-service/validate
**Description:** Validates a partner transfer request against merchant and merchant-account rules.

**Request**

- Headers:
  - `X-Correlation-ID` (optional)

```json
{
  "merchantExternalId": "merchant-ext-001",
  "operation": "TRANSFER",
  "operationType": "DOMESTIC",
  "branchCode": "0101",
  "merchantAccountNumber": "20208000900123456789",
  "beneficiaryAccountNumber": "20208000900999999999",
  "currency": "UZS",
  "amount": 1000.00
}
```

**Response**

```json
{
  "success": true,
  "data": {
    "success": true,
    "message": "Validation passed",
    "lastStep": "BRANCH_ALLOWED_FOR_OPERATION",
    "steps": [
      {
        "step": "ROW_INCOMING_REQUEST",
        "success": true,
        "message": "Validation request accepted"
      }
    ]
  },
  "error": null
}
```

**Error Responses**

| Status | Reason |
|--------|--------|
| `400` | Invalid request body, enum conversion failure, malformed JSON, bean validation failure |
| `500` | Unhandled internal server error |

---

### GET /api/v1/partner-validation-service/merchants
**Description:** Returns a paginated list of merchants.

**Request**

- Headers:
  - `X-Correlation-ID` (optional)
- Query parameters:
  - `page`
  - `size`
  - `search`
  - `orderBy`
  - `sortOrder`
  - `status`
  - `merchantExternalId`

Request body: none

**Response**

```json
{
  "success": true,
  "data": {
    "list": [
      {
        "id": "64a050bb-a58e-4db3-a5a4-4727df44009c",
        "merchantExternalId": "merchant-ext-001",
        "name": "Merchant LLC",
        "tin": "123456789",
        "status": "ACTIVE",
        "allowedOperations": [
          "TRANSFER",
          "TYPE:DOMESTIC",
          "BRANCH:0101"
        ]
      }
    ],
    "total": 1,
    "page": 0,
    "size": 20
  },
  "error": null
}
```

**Error Responses**

| Status | Reason |
|--------|--------|
| `400` | Invalid query parameter value |
| `500` | Unhandled internal server error |

---

### GET /api/v1/partner-validation-service/merchants/lookup
**Description:** Returns a non-paginated merchant list filtered by the same merchant filter fields.

**Request**

- Headers:
  - `X-Correlation-ID` (optional)
- Query parameters:
  - `search`
  - `orderBy`
  - `sortOrder`
  - `status`
  - `merchantExternalId`

Request body: none

**Response**

```json
{
  "success": true,
  "data": [
    {
      "id": "64a050bb-a58e-4db3-a5a4-4727df44009c",
      "merchantExternalId": "merchant-ext-001",
      "name": "Merchant LLC",
      "tin": "123456789",
      "status": "ACTIVE",
      "allowedOperations": [
        "TRANSFER",
        "TYPE:DOMESTIC"
      ]
    }
  ],
  "error": null
}
```

**Error Responses**

| Status | Reason |
|--------|--------|
| `400` | Invalid query parameter value |
| `500` | Unhandled internal server error |

---

### GET /api/v1/partner-validation-service/merchants/{id}
**Description:** Returns a single merchant by UUID.

**Request**

- Headers:
  - `X-Correlation-ID` (optional)
- Path parameters:
  - `id` - merchant UUID

Request body: none

**Response**

```json
{
  "success": true,
  "data": {
    "id": "64a050bb-a58e-4db3-a5a4-4727df44009c",
    "merchantExternalId": "merchant-ext-001",
    "name": "Merchant LLC",
    "tin": "123456789",
    "status": "ACTIVE",
    "allowedOperations": [
      "TRANSFER",
      "TYPE:DOMESTIC"
    ],
    "createdAt": "2026-03-02T12:00:00",
    "updatedAt": "2026-03-02T12:30:00"
  },
  "error": null
}
```

**Error Responses**

| Status | Reason |
|--------|--------|
| `400` | Invalid UUID |
| `404` | Merchant not found |
| `500` | Unhandled internal server error |

---

### POST /api/v1/partner-validation-service/merchants
**Description:** Creates a merchant.

**Request**

- Headers:
  - `X-Correlation-ID` (optional)

```json
{
  "id": null,
  "merchantExternalId": "merchant-ext-001",
  "name": "Merchant LLC",
  "tin": "123456789",
  "status": "ACTIVE",
  "allowedOperations": [
    "TRANSFER",
    "TYPE:DOMESTIC",
    "BRANCH:0101"
  ]
}
```

**Response**

```json
{
  "success": true,
  "data": "64a050bb-a58e-4db3-a5a4-4727df44009c",
  "error": null
}
```

**Error Responses**

| Status | Reason |
|--------|--------|
| `400` | Bean validation failure or duplicate `merchantExternalId` from service validation |
| `409` | Database constraint violation |
| `500` | Unhandled internal server error |

---

### PUT /api/v1/partner-validation-service/merchants/{id}
**Description:** Updates a merchant.

**Request**

- Headers:
  - `X-Correlation-ID` (optional)
- Path parameters:
  - `id` - merchant UUID

```json
{
  "id": null,
  "merchantExternalId": "merchant-ext-001",
  "name": "Merchant LLC Updated",
  "tin": "123456789",
  "status": "ACTIVE",
  "allowedOperations": [
    "TRANSFER",
    "TYPE:DOMESTIC"
  ]
}
```

**Response**

```json
{
  "success": true,
  "data": "64a050bb-a58e-4db3-a5a4-4727df44009c",
  "error": null
}
```

**Error Responses**

| Status | Reason |
|--------|--------|
| `400` | Invalid UUID, bean validation failure, duplicate `merchantExternalId` |
| `404` | Merchant not found |
| `409` | Database constraint violation |
| `500` | Unhandled internal server error |

---

### DELETE /api/v1/partner-validation-service/merchants/{id}
**Description:** Soft-deletes a merchant by setting its status to `BLOCKED`.

**Request**

- Headers:
  - `X-Correlation-ID` (optional)
- Path parameters:
  - `id` - merchant UUID

Request body: none

**Response**

```json
{
  "success": true,
  "data": {
    "message": "Merchant blocked successfully"
  },
  "error": null
}
```

**Error Responses**

| Status | Reason |
|--------|--------|
| `400` | Invalid UUID |
| `404` | Merchant not found |
| `500` | Unhandled internal server error |

---

### GET /api/v1/partner-validation-service/merchants/{merchantId}/accounts
**Description:** Returns a paginated list of merchant accounts for a merchant.

**Request**

- Headers:
  - `X-Correlation-ID` (optional)
- Path parameters:
  - `merchantId` - merchant UUID
- Query parameters:
  - `page`
  - `size`
  - `search`
  - `orderBy`
  - `sortOrder`
  - `status`
  - `currency`

Request body: none

**Response**

```json
{
  "success": true,
  "data": {
    "list": [
      {
        "id": "fd1ae6fe-5a4c-4f12-a4cd-f4043be4b130",
        "branchCode": "0101",
        "accountNumber": "20208000900123456789",
        "currency": "UZS",
        "absClientId": "ABS-001",
        "status": "ACTIVE"
      }
    ],
    "total": 1,
    "page": 0,
    "size": 20
  },
  "error": null
}
```

**Error Responses**

| Status | Reason |
|--------|--------|
| `400` | Invalid UUID or query parameter |
| `404` | Merchant not found |
| `500` | Unhandled internal server error |

---

### GET /api/v1/partner-validation-service/merchants/{merchantId}/accounts/lookup
**Description:** Returns a non-paginated list of merchant accounts for a merchant.

**Request**

- Headers:
  - `X-Correlation-ID` (optional)
- Path parameters:
  - `merchantId` - merchant UUID
- Query parameters:
  - `search`
  - `orderBy`
  - `sortOrder`
  - `status`
  - `currency`

Request body: none

**Response**

```json
{
  "success": true,
  "data": [
    {
      "id": "fd1ae6fe-5a4c-4f12-a4cd-f4043be4b130",
      "branchCode": "0101",
      "accountNumber": "20208000900123456789",
      "currency": "UZS",
      "absClientId": "ABS-001",
      "status": "ACTIVE"
    }
  ],
  "error": null
}
```

**Error Responses**

| Status | Reason |
|--------|--------|
| `400` | Invalid UUID or query parameter |
| `404` | Merchant not found |
| `500` | Unhandled internal server error |

---

### GET /api/v1/partner-validation-service/merchants/{merchantId}/accounts/{id}
**Description:** Returns a single merchant account by account UUID within a merchant.

**Request**

- Headers:
  - `X-Correlation-ID` (optional)
- Path parameters:
  - `merchantId` - merchant UUID
  - `id` - account UUID

Request body: none

**Response**

```json
{
  "success": true,
  "data": {
    "id": "fd1ae6fe-5a4c-4f12-a4cd-f4043be4b130",
    "merchantId": "64a050bb-a58e-4db3-a5a4-4727df44009c",
    "branchCode": "0101",
    "accountNumber": "20208000900123456789",
    "currency": "UZS",
    "absClientId": "ABS-001",
    "status": "ACTIVE",
    "createdAt": "2026-03-02T12:00:00",
    "updatedAt": "2026-03-02T12:30:00"
  },
  "error": null
}
```

**Error Responses**

| Status | Reason |
|--------|--------|
| `400` | Invalid UUID |
| `404` | Merchant or merchant account not found |
| `500` | Unhandled internal server error |

---

### POST /api/v1/partner-validation-service/merchants/{merchantId}/accounts
**Description:** Creates a merchant account for a merchant.

**Request**

- Headers:
  - `X-Correlation-ID` (optional)
- Path parameters:
  - `merchantId` - merchant UUID

```json
{
  "id": null,
  "merchantId": null,
  "branchCode": "0101",
  "accountNumber": "20208000900123456789",
  "currency": "UZS",
  "absClientId": "ABS-001",
  "status": "ACTIVE"
}
```

**Response**

```json
{
  "success": true,
  "data": "fd1ae6fe-5a4c-4f12-a4cd-f4043be4b130",
  "error": null
}
```

**Error Responses**

| Status | Reason |
|--------|--------|
| `400` | Invalid UUID, bean validation failure, duplicate account from service validation |
| `404` | Merchant not found |
| `409` | Database constraint violation |
| `500` | Unhandled internal server error |

---

### PUT /api/v1/partner-validation-service/merchants/{merchantId}/accounts/{id}
**Description:** Updates an existing merchant account.

**Request**

- Headers:
  - `X-Correlation-ID` (optional)
- Path parameters:
  - `merchantId` - merchant UUID
  - `id` - account UUID

```json
{
  "id": null,
  "merchantId": null,
  "branchCode": "0101",
  "accountNumber": "20208000900123456789",
  "currency": "UZS",
  "absClientId": "ABS-001",
  "status": "ACTIVE"
}
```

**Response**

```json
{
  "success": true,
  "data": "fd1ae6fe-5a4c-4f12-a4cd-f4043be4b130",
  "error": null
}
```

**Error Responses**

| Status | Reason |
|--------|--------|
| `400` | Invalid UUID, bean validation failure, duplicate account from service validation |
| `404` | Merchant or merchant account not found |
| `409` | Database constraint violation |
| `500` | Unhandled internal server error |

---

### DELETE /api/v1/partner-validation-service/merchants/{merchantId}/accounts/{id}
**Description:** Soft-deletes a merchant account by setting its status to `BLOCKED`.

**Request**

- Headers:
  - `X-Correlation-ID` (optional)
- Path parameters:
  - `merchantId` - merchant UUID
  - `id` - account UUID

Request body: none

**Response**

```json
{
  "success": true,
  "data": {
    "message": "Merchant account blocked successfully"
  },
  "error": null
}
```

**Error Responses**

| Status | Reason |
|--------|--------|
| `400` | Invalid UUID |
| `404` | Merchant or merchant account not found |
| `500` | Unhandled internal server error |

---

### GET /api/v1/partner-validation-service/logs
**Description:** Returns a paginated list of partner validation audit logs from PostgreSQL.

**Request**

- Headers:
  - `X-Correlation-ID` (optional)
- Query parameters:
  - `page`
  - `size`
  - `search`
  - `orderBy`
  - `sortOrder`
  - `correlationId`
  - `methodType`
  - `url`
  - `ipAddress`
  - `success`
  - `monitoringStep`
  - `requestDateFrom`
  - `requestDateTo`

Request body: none

**Response**

```json
{
  "success": true,
  "data": {
    "list": [
      {
        "id": "9784d8aa-bc2f-498d-a3f8-0b7b2cf95821",
        "correlationId": "7f0f2d4a-6827-4d6c-9264-1da8e3d0b8fd",
        "monitoringStep": "MERCHANT_ACTIVE",
        "request": "{\"merchantId\":\"merchant-ext-001\"}",
        "response": "{\"step\":\"MERCHANT_ACTIVE\",\"success\":true,\"message\":\"Merchant is active\"}",
        "methodType": "POST",
        "url": "/api/v1/partner-validation-service/validate",
        "requestDate": "2026-03-02T12:00:00",
        "responseDate": "2026-03-02T12:00:00",
        "ipAddress": "127.0.0.1",
        "success": true
      }
    ],
    "total": 1,
    "page": 0,
    "size": 20
  },
  "error": null
}
```

**Error Responses**

| Status | Reason |
|--------|--------|
| `400` | Invalid query parameter or date-time format |
| `500` | Unhandled internal server error |

---

### GET /api/v1/partner-validation-service/logs/lookup
**Description:** Returns a non-paginated list of partner validation audit logs from PostgreSQL.

**Request**

- Headers:
  - `X-Correlation-ID` (optional)
- Query parameters:
  - `search`
  - `orderBy`
  - `sortOrder`
  - `correlationId`
  - `methodType`
  - `url`
  - `ipAddress`
  - `success`
  - `monitoringStep`
  - `requestDateFrom`
  - `requestDateTo`

Request body: none

**Response**

```json
{
  "success": true,
  "data": [
    {
      "id": "9784d8aa-bc2f-498d-a3f8-0b7b2cf95821",
      "correlationId": "7f0f2d4a-6827-4d6c-9264-1da8e3d0b8fd",
      "monitoringStep": "MERCHANT_ACTIVE",
      "request": "{\"merchantId\":\"merchant-ext-001\"}",
      "response": "{\"step\":\"MERCHANT_ACTIVE\",\"success\":true,\"message\":\"Merchant is active\"}",
      "methodType": "POST",
      "url": "/api/v1/partner-validation-service/validate",
      "requestDate": "2026-03-02T12:00:00",
      "responseDate": "2026-03-02T12:00:00",
      "ipAddress": "127.0.0.1",
      "success": true
    }
  ],
  "error": null
}
```

**Error Responses**

| Status | Reason |
|--------|--------|
| `400` | Invalid query parameter or date-time format |
| `500` | Unhandled internal server error |

## Project Structure

```text
src/main/java/uz/davrbank/partnervalidationservice
├── client        # Outbound Gateway WebClient wrapper
├── common        # Shared API envelopes, base entities, filters, query helpers
├── config        # Datasource, WebClient, async, OpenAPI, and properties configuration
├── constants     # Shared API and header constants
├── exception     # Error DTOs and global exception handling
├── gateway       # Gateway OAuth token acquisition and in-memory token store
├── logging       # Audit log DTOs, entities, repositories, services, controller, enums
├── merchant      # Merchant/account controllers, services, DTOs, entities, repositories, mappers, filters
├── util          # Startup logging utility
└── validation    # Validation controller, DTOs, and core validation service

src/main/resources
├── application.yml
└── db
    └── migration     # Flyway V1–V4 scripts
```

## Monitoring Steps

The `MonitoringStep` enum defines validation/audit step names persisted to PostgreSQL.

| Monitoring Step | Meaning |
|----------------|---------|
| `ROW_INCOMING_REQUEST` | The incoming validation request was accepted for processing. The enum name is documented as-is from code. |
| `OUTGOING_ACCOUNT_EXISTS` | Confirms that the outgoing merchant account exists for the requested account number and currency. |
| `OUTGOING_ACCOUNT_BELONGTO_MERCHANT` | Confirms that the outgoing account belongs to the merchant. The enum name contains a typo and is documented as-is from code. |
| `ALLOWED_MERCHANT_ACCOUNT` | Confirms that the merchant account is allowed to be used, currently implemented as `AccountStatus.ACTIVE`. |
| `MERCHANT_ACTIVE` | Confirms that the merchant exists and has status `ACTIVE`. |
| `INCOMING_ACCOUNT_EXISTS` | Reserved enum value for an incoming / beneficiary account existence check. `# TODO: confirm intended runtime usage`, because the current validation service does not emit this step. |
| `ALLOWED_FOR_THIS_OPERATION` | Confirms that `allowed_operations` contains the plain operation token, for example `TRANSFER`. |
| `ALLOWED_FOR_THIS_TYPE` | Confirms that `allowed_operations` allows the requested `TYPE:<value>` scope, or that no type-specific scope is configured. |
| `BRANCH_ACCOUNT_CHECK` | Confirms that the merchant account has a branch code configured. |
| `AMOUNT_CHECK` | Confirms that the amount is greater than zero. |
| `BRANCH_ALLOWED_FOR_OPERATION` | Confirms that `allowed_operations` allows the requested `BRANCH:<value>` scope, or that no branch-specific scope is configured. |
| `MATCH_BRANCH_ACCOUNT` | Confirms that the request branch code matches the branch code stored on the merchant account. |

## Notes and Known Ambiguities

- `ValidationFailedException` exists in the codebase, but the current validation flow returns validation failures inside a `200 OK` response payload instead of throwing that exception.
- `beneficiaryAccountNumber` is present in `ValidationRequest`, but the current `PartnerValidationService` implementation does not validate beneficiary-account existence or ownership.
- A Gateway OAuth/WebClient infrastructure exists in the codebase, but the current validation flow does not invoke `GatewayClient`.
- Minimum supported Maven version is not declared in `pom.xml`. The repository includes the Maven Wrapper, so `mvnw` / `mvnw.cmd` is the safest entry point.
