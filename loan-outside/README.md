<a href="./README_RU.md">Russian version</a>

# GetOutsideLoan Service (getoutsideloan-service)

A Spring Boot microservice in the SMART BAS ecosystem. This repository contains the service implementation for outside loan report retrieval and Excel export, together with Eureka, Vault, Config, Feign, and MinIO-related configuration.

## Table of Contents
- [Overview](#overview)
- [Technologies](#technologies)
- [API Documentation](#api-documentation)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Clone & Build](#clone--build)
  - [Configuration](#configuration)
  - [Run](#run)
- [Observability](#observability)
- [Notes](#notes)
- [Contact](#contact)

## Overview
GetOutsideLoan Service provides pageable data retrieval and Excel report export for the outside loan portfolio in SMART BAS. The service is prepared to use service discovery, centralized configuration, Vault secrets, and Feign-based hub integration.

## Technologies
| Component           | Technology / Version           | Note                                   |
|:--------------------|:-------------------------------|:----------------------------------------|
| Language            | Java 21                        | Project target version                  |
| Framework           | Spring Boot 3.4.12             | Web + Validation + Actuator             |
| Cloud               | Spring Cloud 2024.0.3          | Config Client, Vault, Eureka, OpenFeign |
| Utilities           | Apache POI 5.2.3               | Excel generation                        |
| API Docs            | Springdoc OpenAPI 2.8.8        | Swagger UI                              |
| Object Storage      | MinIO Java SDK 8.4.3           | Config prepared                         |
| Build               | Maven Wrapper                  | `mvnw` / `mvnw.cmd`                     |
| backend server port | 7211                           | Default port                            |

![Java](https://img.shields.io/badge/Java-21-blue?logo=java)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.4.12-green?logo=spring)
![Maven](https://img.shields.io/badge/Maven-3.x-red?logo=apache-maven)

## API Documentation

Application context path: `/get/outside/loan/api`  
Controller prefix: `/v1/reports`  
Full base path: `/get/outside/loan/api/v1/reports`

Swagger UI is available at `/get/outside/loan/api/swagger-ui.html`.

### 1. Get All Pageable
**Endpoint:** `POST /get/all/page`

Returns a paginated list of outside loan report data.

**Headers:**
- `username`: (Optional) The username of the requester.

**Request Body (`GetOutsideLoanFilter`):**
```json
{
  "startDate": "01.01.2026",
  "endDate": "31.01.2026",
  "branch": "01037",
  "state": 1,
  "pageNumber": 1,
  "pageSize": 20
}
```

**Validation rules:**
- `startDate` and `endDate` are required and must be in `dd.MM.yyyy` format.
- `endDate` must not be earlier than `startDate`.
- `state` is required and must be one of `0`, `1`, `2`.
- `pageNumber` defaults to `1` when not provided or invalid.
- `pageSize` defaults to `20` when not provided or invalid.
- `pageSize` must not exceed `200000`.

**Response:** `PageableIntegrationResponseDTO<GetOutsideLoanPageableResponse>`

---

### 2. Download Excel
**Endpoint:** `POST /download/excel`

Generates and downloads an Excel report for the outside loan portfolio.

**Headers:**
- `username`: (Optional) The username of the requester.

**Request Body (`GetOutsideLoanFilter`):**
```json
{
  "startDate": "01.01.2026",
  "endDate": "31.01.2026",
  "branch": "01037",
  "state": 1,
  "pageNumber": 1,
  "pageSize": 20
}
```

**Notes:**
- For Excel export, the service forces `pageNumber=1` and `pageSize=200000`, then fetches all hub pages before generating the file.
- If no data is returned, the service responds with `404`.

**Response:** `ResponseEntity<ByteArrayResource>` (Excel file)

## Getting Started

### Prerequisites
- Java 21+
- Maven 3.8+

### Clone & Build
```bash
git clone <this-repository-url>
cd GetoutsideloanService

./mvnw clean install
```

On Windows:
```bash
mvnw.cmd clean install
```

### Configuration
- Runtime profiles are split into `application-test.yml` and `application-prod.yml`.
- Sensitive values should be managed through environment variables and/or Vault.
- Eureka, Feign, and MinIO settings are already structured in the profile files.
- The default active profile in this repository is `test`.

### Run
Run with Maven:
```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=test
```

On Windows:
```bash
mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=test
```

Run the packaged JAR:
```bash
java -jar target/GetoutsideloanService-0.0.1-SNAPSHOT.jar --spring.profiles.active=test
```

## Observability
- Actuator web endpoints expose `health`, `info`, and `metrics`.
- Vault health checks are disabled in profile configuration.
- Service logging is configured for `uz.davrbank.getoutsideloanservice`.

## Notes
- The service integrates with `getoutsideloanhub-service` through OpenFeign and Eureka.
- Excel files are generated in code with Apache POI rather than by loading an `.xlsx` template at runtime.
- MinIO configuration is present, but the current business flow does not upload generated reports to object storage.

## Contact
- Owner: Davrbank
- Authors: Integration team
