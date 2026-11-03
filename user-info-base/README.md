# UserInfoBase Service

## Overview

**UserInfoBase** is an internal service used in **Davrbank** to retrieve and provide customer information obtained from the **NCI system**.
The service allows other internal systems to fetch customer-related data using **PINFL** and generate information such as:

* Credit liabilities of a customer
* Customer credit statistics
* Official documents related to credit obligations
* Customer data retrieved from NCI
* Document files generated for internal banking processes

The service acts as a **centralized information provider** for customer financial data and document generation.

---
API Documentation (Swagger)

Interactive API documentation is available via Swagger UI.

Swagger allows developers and integrators to:

View all available API endpoints

Test API requests directly from the browser

Provide JWT authentication tokens

See request and response structures

### Swagger UI
[Open Swagger UI](http://127.0.0.1:5638/swagger-ui/index.html)

### OpenAPI Specification
[Open API Docs](http://127.0.0.1:5638/v3/api-docs)


# System Architecture

The service integrates with several internal infrastructure components used within Davrbank:

### PostgreSQL (Production Database)

Customer and processed data are stored in PostgreSQL.

```text
jdbc:postgresql://127.0.0.1:5432/infobase_db
```

---

### Keycloak (Authentication & Authorization)

Authentication and authorization are managed via **Keycloak**.

```text
http://127.0.0.1:8080/realms/filebase
```

Keycloak is used for:

* Service authentication
* Token validation
* Secure access to internal APIs

---

### MinIO (Object Storage)

Generated documents and files are stored in **MinIO object storage**.

```text
https://internal.example.com/
```

MinIO is used for:

* Storing generated documents
* File retrieval
* Document management

---

# Main Functionality

The **UserInfoBase service** performs the following operations:

### 1. Customer Information Retrieval

Retrieves customer information from the **NCI system** using **PINFL**.

### 2. Credit Liability Information

Provides information about:

* Active credit obligations
* Credit balance
* Loan history
* Debt statistics

### 3. Document Generation

The service generates official documents related to customer credit information.

Examples:

* Credit liability documents
* Credit statistics reports
* Customer financial information documents

### 4. File Storage

Generated documents are stored in **MinIO** for secure access and later retrieval.

---

# Technologies Used

* **Java**
* **Spring Boot**
* **PostgreSQL**
* **MongoDB (for temporary storage if needed)**
* **Keycloak**
* **MinIO**
* **REST APIs**

---

# Security

Security is handled through **Keycloak authentication**.

All API requests require:

* Valid **JWT token**
* Proper authorization from the **filebase realm**

---

# Deployment Environment

| Component      | Environment |
| -------------- | ----------- |
| Database       | PostgreSQL  |
| Authentication | Keycloak    |
| File Storage   | MinIO       |
| Backend        | Spring Boot |

---

# Project Purpose

The goal of this project is to simplify access to **customer financial information** and provide **automated document generation** for internal banking systems.

The service enables:

* Fast retrieval of customer credit information
* Centralized access to NCI data
* Secure document storage and access
* Integration with internal banking services

---

# Developer

Davrbank Backend Development Team

---
