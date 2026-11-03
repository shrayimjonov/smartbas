# Bus-Treasury Service

> Microservice for currency wiring (accounting postings), templates, operations (BUY/SELL), document types, MFOs, payments, purposes, and admin queries.

- [English](#bus-treasury-service) / [Русский](./README_RU.md)
- Swagger UI: `/swagger-ui/index.html`
- OpenAPI JSON: `/v3/api-docs`

--- 

## 1) About the Project

### Configuration Table

<div>

|    Component     |               Technology                | Status |
|:----------------:|:---------------------------------------:|:------:|
| Backend          | Java 23, Spring Boot 3.4.1              |   ✅   |
| Database         | JPA/Hibernate, PostgreSQL               |   ✅   |
| API Docs         | Swagger (OpenAPI 3.x), Postman          |   ✅   |
| Build            | Maven                                   |   ✅   |
| Runtime (Prod)   | Linux, Docker/Kubernetes (ArgoCD)       |   ✅   |
| CI               | GitLab CI (Preprod)                     |   ✅   |
| CD               | GitLab CI/CD + ArgoCD (Prod)            |   ✅   |
| Caching          | Redis                                   |   ✅   |
| Security         | JWT / Auth                              |   ✅   |
| Monitoring       | Portainer (127.0.0.1)                |   ✅   |
| Clean Code       | SAST Scanner                            |   ✅   |
| IDE/Tools        | IntelliJ IDEA / VS Code                 |   ✅   |
| Architecture     | Microservice                            |   ✅   |

</div>


## 2) Running Locally

### Prerequisites
- Java 23
- Maven 3.9+

Docker/K8s manifests are handled by CI/CD (ArgoCD).

---
## 3) API Index

> Base path used below: **`/api/v1`**  
> If you deploy behind a gateway prefix (e.g., `/bus`), final path becomes `/bus/api/v1/...`.

| # | Area | Section |
|:-:|:----:|:-------:|
| 1 | Currency Operations | [Operation API](#operation-api) |
| 2 | Templates | [Template API](#template-api) |
| 3 | Wiring (Postings) | [Wiring API](#wiring-api) |
| 4 | Document Types | [DocType API](#doctype-api) |
| 5 | MFO (Branches) | [MFO API](#mfo-api) |
| 6 | Payments | [Payment API](#payment-api) |
| 7 | Purposes | [Purpose API](#purpose-api) |

---

### 3.1 Operation API (Currency BUY/SELL + listings)
**Base:** `/api/v1`
> `templateId` is resolved automatically by `CurrencyType` + `OperationType`.

| Method | URL | Description |
|:-----:|:----|:------------|
| POST | `/usd/buy` | Buy USD |
| POST | `/usd/sell` | Sell USD |
| POST | `/ruble/buy` | Buy RUB |
| POST | `/ruble/sell` | Sell RUB |
| POST | `/euro/buy` | Buy EUR |
| POST | `/euro/sell` | Sell EUR |
| GET  | `/all/wiring?page=0&size=20` | Paginated list of sent wirings |
| GET  | `/operations/latest?templateId={id}` | Latest operation with wirings |

**Request:**
```json
{
  "SUMMA": "100000",
  "COURSE": "12300",
  "CREATED_BY": "tokhirjon"
}
```
**Response:**
```json
{
  "ERROR_CODE": "0",
  "ERROR_NOTE": "SUCCESS. Все остаточные проводки получали финальные статусы. PROVEDEN=[1,2,3,4,5,6,7,8,9]",
  "TRANS_ID": "*******, *******",
  "GROUP_ID": 0
}
```

---

### 3.2 Template API
**Base:** `/api/v1/template`

| Method | URL | Description |
|:-----:|:----|:------------|
| POST | `/create?currencyType=USD` | Create template |
| GET  | `/get/{id}` | Get template by id |
| GET  | `/get/all` | All templates (DTO) |
| PUT  | `/update/{id}` | Update template |
| DELETE | `/{id}` | Delete template |
| GET  | `/all` | Raw ENTITY list |
| GET  | `/filter?currency=USD&operation=BUY` | Find by currency & operation |

---

### 3.3 Wiring API
**Base:** `/api/v1/wiring`

| Method | URL | Description |
|:-----:|:----|:------------|
| POST | `/create?wiringId={id}` | Create wiring |
| GET  | `/all` | All wirings |
| GET  | `/all/{templateId}` | Wirings by template |
| GET  | `/type/{wiringId}` | Wiring by id |
| PUT  | `/update/{id}` | Update wiring |
| DELETE | `/delete/{id}` | Delete wiring |

---

### 3.4 DocType API
**Base:** `/api/v1/doctype`

| Method | URL | Description |
|:-----:|:----|:------------|
| POST | `/create` | Create DocType |
| GET  | `/get/{id}` | Get by id |
| GET  | `/get/all` | All DocTypes |
| PUT  | `/update/{id}` | Update DocType |
| DELETE | `/{id}` | Delete DocType |

---

### 3.5 MFO API
**Base:** `/api/v1/mfo`

| Method | URL | Description |
|:-----:|:----|:------------|
| POST | `/create` | Create MFO |
| GET  | `/get/{id}` | Get by id |
| GET  | `/get/all` | All MFOs |

---

### 3.6 Payment API
**Base:** `/api/v1/payment`

| Method | URL | Description |
|:-----:|:----|:------------|
| POST | `/create` | Create Payment |
| GET  | `/get/{id}` | Get by id |
| GET  | `/get/all` | All Payments |
| DELETE | `/delete/by/id?id={id}` | Delete Payment |

---

### 3.7 Purpose API
**Base:** `/api/v1/purpose`

| Method | URL | Description |
|:-----:|:----|:------------|
| POST | `/create` | Create Purpose |
| GET  | `/get/all` | All Purposes |
| GET  | `/get/{id}` | Get by id |
| PUT  | `/update/{id}` | Update Purpose |
| DELETE | `/delete/{id}` | Delete Purpose |

---

## 4) Error Handling & Statuses
- `ERROR_CODE = "0"` → success
- `ERROR_NOTE` → descriptive message
- `STATUS` → operational state (`PROVEDEN`, `TIMEOUT`, etc.)

---

## 5) Versioning & Conventions
- Version prefix: `/api/v1`
- Enums: case-insensitive (`USD`, `EUR`, `RUB`, `BUY`, `SELL`)
- Dates: `yyyy-MM-dd HH:mm:ss`
- Pagination: Spring Pageable (`page`, `size`, `sort`)

---

## 6) Development Notes
- Annotated with Swagger (`@Tag`, `@Operation`).
- `@PermitAll` allows public endpoints (disable for production).
- `TemplateResolverService` automatically binds `templateId` from `(CurrencyType, OperationType)`.

---
