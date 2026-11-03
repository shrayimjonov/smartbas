<a href="./README.md">English</a> / <a href="./README_RU.md">Russian</a>
# 📘 Journal Transaction Service

A Spring Boot microservice that provides **bank account journal transactions** for **audit and reporting purposes**.
The service acts as an integration layer between Smart BAS consumers and the
`BAS_HUB_REPORT` Oracle package via `journaltranshub-service`.

---

## 🧭 Table of Contents

- [Features](#features)
- [Technologies](#technologies)
- [Architecture](#architecture)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Clone & Build](#clone--build)
  - [Configuration](#configuration)
  - [Run](#run)
- [API Documentation](#api-documentation)
- [API Endpoints](#api-endpoints)
- [Response Wrapper](#response-wrapper)
- [Contact](#contact)

---

## 🔐 Features

- Retrieve bank account journal transactions
- Audit-oriented paginated reporting
- Filtering by:
  - Date range
  - Account balance code
  - Branch
- Pageable response format
- Integration with external HUB service (Feign)
- Centralized error handling
- CI/CD production ready

---

<a id="technologies"></a>
#### ⚙️ Technologies Used

<div>

---
| Component |                      Technology | Status |
|---------:|--------------------------------:|:------:|
| Backend |        Java 17, Spring Boot 3.x | ✅ |
| Port |                            7206 | ✅ |
| Build Tool |                           Maven | ✅ |
| External Integration | journaltranshub-service (Feign) | ✅ |
| API Docs |               Swagger / OpenAPI | ✅ |
| Service Discovery |                          Eureka | ✅ |
| Configuration |                           Vault | ✅ |
| Architecture |                    Microservice | ✅ |
| CI/CD |                    GitLab CI/CD | ✅ |

---

</div>

![Java](https://img.shields.io/badge/Java-17-blue?logo=java)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-green?logo=spring)
![Maven](https://img.shields.io/badge/Maven-3.9.11-red?logo=apache-maven)

---

## 🧱 Architecture

- Layered architecture:
- This service **does not access database directly**
- All journal transaction data is retrieved from **BAS_HUB_REPORT** package
- Communication with HUB service is handled via **Spring Cloud OpenFeign**

---

## 🚀 Getting Started

<a id="prerequisites"></a>
### Prerequisites

- Java 17+
- Maven 3.8+
- Access to Eureka Server
- Access to Vault
- Network access to HUB service

---

<a id="clone--build"></a>
### Clone & Build

```bash
# clone repository
git clone <repository-url>
cd journaltrans-service

# build
mvn clean install
```
<a id="configuration"></a>

## ⚙️ Configuration
### Server & Context Path
```bash
server:
  port: 7206
  servlet:
    context-path: /hub/api
```
### Service Name
```bash
spring:
  application:
    name: journaltrans-service
```
### Eureka
```bash
eureka:
  client:
    register-with-eureka: true
    fetch-registry: true
```
### Vault
```bash
spring:
  cloud:
    vault:
      authentication: approle
```
---

<a id="run"></a>
## Run
### Run using Maven:
```bash
mvn spring-boot:run
```

### Run as JAR:
```bash
java -jar target/journaltrans-service.jar
```
### Service will be available at:
```bash
http://<host>:7506/hub/api
```
<a id="api-documentation"></a>
## 📚 API Documentation
### Swagger UI will be available after application startup.
### Example URLs:
- Swagger UI:
```http://<host>:7506/hub/api/swagger-ui/index.html```

- OpenAPI JSON:
```http://<host>:7506/hub/api/v3/api-docs```

---

<a id="api-endpoints"></a>

## 🔗 API Endpoints
### 📄 Journal Transaction Report (Paginated)
| Method | Endpoint                              |
| ------ | ------------------------------------- |
| POST   | `/hub/api/journal/trans/get/all/page` |

### Full URL example (test):
```bash
http://127.0.0.1:7506/hub/api/journal/trans/get/all/page
```
## 🔸 Request Parameters
| Parameters |      pattern       |  validation     |
|------------|--------------------|-----------------|
|iStartDate  | (dd.MM.yyyy)       | # optional      |
|iEndDate    | (dd.MM.yyyy)       | # optional      | 
|iAccBalKod  |                    | # optional      |
|iBranch     |                    | # optional      |
|iPageNumber |                    | # required (>=1)|
|iPageSize   |                    | # required (>=1)|

## 🔸 Response (Success)
```bash
{
  "status": "success",
  "error": "",
  "totalElements": 23822,
  "totalPages": 11911,
  "last": false,
  "pageNumber": 1,
  "pageSize": 20,
  "numberOfElements": 20,
  "first": true,
  "empty": false,
  "data": [
    {
      "TRANSACTION_ID": "109586158114",
      "BANK_CL": "01046",
      "ACC_CL": "10101000300001046001",
      "BANK_CO": "01046",
      "ACC_CO": "19903000100001046001",
      "DEBET": 153600000,
      "CREDIT": 0,
      "PURPOSE": "0200 ...",
      "MODULE": "Подсистема \"Инкассация\"",
      "CREATED_EMP": "User Name",
      "CREATED_DATE": "03.01.2025",
      "CREATED_TIME": "09:01:33",
      "APPROVED_EMP": "User Name",
      "APPROVED_DATE": "03.01.2025",
      "APPROVED_TIME": "09:04:30"
    }
  ]
}
```

<a id="response-wrapper"></a>

## 📦 Response Wrapper

### All responses follow a standard wrapper structure:
```bash
{
  "status": "success | error",
  "error": "",
  "data": []
}
```
<a id="contact"></a>

## 📬 Contact

- Organization: Davr Bank – Smart BAS

- Service Developer: Bobur Boynazarov
