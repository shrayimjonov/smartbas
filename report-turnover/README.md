<a href="./README_RU.md">Russian version</a>

# 🧾 Schot Oborot Service (schotoborot-service)

A Spring Boot microservice in the SMART BAS ecosystem. This repository contains the service skeleton, integration stubs (Eureka, Vault, Config), MinIO client setup, and operational tooling (Docker, Actuator).

## 🧭 Table of Contents
- [Overview](#overview)
- [Technologies](#technologies)
- [API Documentation](#api-documentation)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Clone & Build](#clone--build)
  - [Configuration](#configuration)
  - [Run](#run)
  - [Docker](#docker)
- [Observability](#observability)
- [Notes](#notes)
- [Roadmap](#roadmap)
- [Contact](#contact)

## Overview
Schot Oborot Service provides foundational components for data processing and file operations within SMART BAS. The service is prepared to register with a service registry, fetch secrets from a secret store, and interact with object storage.

## Technologies
| Component           | Technology / Version           | Note                                   |
|:--------------------|:-------------------------------|:----------------------------------------|
| Language            | Java 17                        | LTS                                     |
| Framework           | Spring Boot 3.4.2              | Web + Actuator                          |
| Cloud               | Spring Cloud 2024.0.0          | Config Client, Vault, Eureka, LoadBalancer, OpenFeign |
| Object Storage      | MinIO SDK 8.4.3                | S3-compatible client                    |
| Utilities           | Apache POI 5.2.3, Gson         | Excel and JSON                          |
| Build               | Maven                           | Project build                           |
| Containerization    | Docker                          | Application image                       |
| Testing             | spring-boot-starter-test        | Unit/integration tests                  |
| backend server port | 7207                           | Default port                            |

![Java](https://img.shields.io/badge/Java-17-blue?logo=java)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.4.2-green?logo=spring)
![Maven](https://img.shields.io/badge/Maven-3.x-red?logo=apache-maven)
![Docker](https://img.shields.io/badge/Docker-24.x-blue?logo=docker)

## API Documentation

All APIs are prefixed with `/v1/reports`.

### 1. Get All Pageable
**Endpoint:** `POST /get/all/page`

Returns a paginated list of report data.

**Headers:**
- `username`: (Optional) The username of the requester.

**Request Body (`SchotoborotFilter`):**
```json
{
  "startDate": "19.12.2025",
  "endDate": "19.12.2025",
  "account": "20208000100000000001",
  "pageNumber": 1,
  "pageSize": 20
}
```

**Response:** `PageableIntegrationResponseDTO<SchotoborotGetPageableResponse>`

---

### 2. Download Excel
**Endpoint:** `POST /download/excel`

Generates and downloads an Excel report.

**Headers:**
- `username`: (Optional) The username of the requester.

**Request Body (`SchotoborotFilter`):**
```json
{
  "startDate": "19.12.2025",
  "endDate": "19.12.2025",
  "account": "20208000100000000001",
  "pageNumber": 1,
  "pageSize": 20
}
```

**Response:** `ResponseEntity<ByteArrayResource>` (Excel File)

## Project Structure
- `src/main/java/uz/davrbank/schotoborotservice/SchotoborotServiceApplication.java` — application entry point
- `src/main/java/uz/davrbank/schotoborotservice/utils/StartProject.java` — startup banner and environment logging
- `src/main/java/uz/davrbank/schotoborotservice/aggregate/config/MinIOProperty.java` — MinIO client properties binding
- `src/main/resources/application.yml` — profile activation
- `src/main/resources/application-test.yml` — environment parameters (managed externally)
- `Dockerfile`, `docker-compose.yml` — containerization artifacts

## Getting Started

### Prerequisites
- Java 17+
- Maven 3.8+
- Docker (optional)

### Clone & Build
```bash
git clone <this-repository-url>
cd schotoborot

mvn clean install
```

### Configuration
- No properties or secrets are documented here to avoid exposing sensitive information.
- Use environment variables and/or your organization’s secret store (e.g., Vault) for configuration.
- Refer to your internal operations documentation for the required keys and values.

### Run
Run with Maven (example):
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=test
```

Run the packaged JAR (example):
```bash
java -jar target/schotoborot-service-0.0.1-SNAPSHOT.jar --spring.profiles.active=test
```

### Docker
Build and run locally (examples):
```bash
docker build -t schotoborot-service:local .
docker compose up -d
# or
docker-compose up -d
```

## Contact
- Owner: Davrbank
- Authors: Integration team (Urunov Khamdamboy)
