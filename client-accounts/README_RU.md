<a href = "https://internal.example.com/bas-group/bus/reportservices/clientaccounts-service/-/blob/prod/README.md?ref_type=heads">
English</a>
/ <a href = "https://internal.example.com/bas-group/bus/reportservices/clientaccounts-service/-/blob/prod/README_RU.md?ref_type=heads">Russian</a>

# 📌 clientaccounts-service

SMART BAS PROJECT

---

## ✅ About the Project

|    Components     |            Technology             | Status |
|:-----------------:|:---------------------------------:|:------:|
|      Backend      |    Java 17, Spring Boot 3.4.2     |   ✅    |
|  Backend (Port)   |               7204                |   ✅    |
|     Database      |             Oracle DB             |   ✅    |
| API Documentation |     Swagger, JavaDoc, Postman     |   ✅    |
|    Build Tool     |               Maven               |   ✅    |
|     Server OS     |               Linux               |   ✅    |
| API Testing Tool  |        Postman / Insomnia         |   ✅    |
|    IDE/Editor     |      IntelliJ IDEA / VS Code      |   ✅    |
|   Architecture    |           Microservice            |   ✅    |
|        CI         |         GitLab CI (Test)          |   ✅    |
|       CI/CD       | GitLab CI/CD, ArgoCD (Production) |   ✅    |
|  Infrastructure   | Docker (Test) / Kubernetes (Prod) |   ✅    |
|    Clean Code     |           SAST Scanner            |   ✅    |
|      Caching      |               Redis               |   ✅    |
|     Security      |     OAuth 2.0, Keycloak, JWT      |   ✅    |
|        UI         |          Angular / React          |   ✅    |
|    Monitoring     |             Portainer             |   ✅    |

---

## 📚 Table of Contents

## Getting started

### Prerequisites

Before you begin, ensure you have the following installed:

*   **Java Development Kit (JDK):** Version 17 or higher.
    *   [Download JDK](https://www.oracle.com/java/technologies/downloads/)
*   **Maven:** Version 3.6 or higher.
    *   [Download Maven](https://maven.apache.org/download.cgi)
*   **Git:** For cloning the repository.
    *   [Download Git](https://git-scm.com/downloads)
*   **Docker & Docker Compose:** (Optional, if you plan to use Docker for local setup/deployment)
    *   [Download Docker Desktop](https://www.docker.com/products/docker-desktop)
        **Docker & Docker Compose:** (Optional, if you plan to use Docker for local setup/deployment)
    *   [Download Docker Desktop](https://www.docker.com/products/docker-desktop)
### Installation

1.  **Clone the repository:**
2. **git clone** https://internal.example.com/bas-group/bus/reportservices/clientaccounts.git
3. **Build the project with Maven** -> this command will download all dependencies and build the JAR file.


### Documentation
The full API documentation is available via Swagger UI after running the application:

- **Swagger UI:** [http://localhost:7204/clientaccounts/api/swagger-ui/index.html](http://localhost:72047204/clientaccounts/api/swagger-ui/index.html)
- **OpenAPI JSON:** [http://localhost:7204/clientaccounts/api/v3/api-docs](http://localhost:7204/clientaccounts/api/v3/api-docs)

### Running the Application

#### Option 1: Using Maven (for development)

```
 * mvn spring-boot:run
```
#### Option 2: Running the Executable JAR

After building the project (`mvn clean install`), you can run the generated JAR:


#### Option 3: Using Docker (Recommended for local development with services like PostgreSQL)

1.    **Build Docker image:**
      `docker build -t my-awesome-spring-app`
2.    **Run with Docker Compose:** `docker-compose up -d` ->This will start the Spring Boot application along with any configured services (e.g., PostgreSQL).


## API Endpoints

The application exposes the following RESTful endpoints:

## Get All info

| No |         Api lists          |
|:--:|:--------------------------:|
| 1  | [Endpoints](#Endpoints) |

<details open>
<summary><b>API List</b> (click to expand)</summary>

| No |               API Name                |                                         URL                                          |
|:--:|:-------------------------------------:|:------------------------------------------------------------------------------------:|
| 1  |         Get all Account Code          |              [`client/accounts/api/v1/accode`](#client-accounts-accode)              |
| 2  |   Get all data (without Pagination)   |            [`/client/accounts/api/v1/get/all`](#client-accounts-get-all)             |
| 2  |    Get all data (with Pagination)     |         [`/client/accounts/api/v1/pagination`](#client-accounts-pagination)          |
| 3  | Get all data (with Pagination & Sort) | [`/client/accounts/api/v1/pagination/sort`](#client-accounts-pagination-sort) |
| 4  |           Export Excel File           |       [`/client/accounts/api/v1/export/excel`](#client-accounts-export-excel)        |

</details>

---

## 📌 API Details

---

### 1️⃣ Report Data AcCode <a id="client-accounts-accode"></a>

- **URL:** `/clientaccounts/api/v1/reports/get/all`
- **Method:** `GET`

**Request:**

```http
RequestParam:
```

**Response:**

```json
{
  "status": "success",
  "error": "",
  "totalElements": 3,
  "data": [
    "10107",
    "10791",
    "12401",
    "10397"
  ]
}

```

---

### 2️⃣ Report Data With Pagination <a id="client-accounts-get-all"></a>

- **URL:** `/client/accounts/api/v1/get/all`
- **Method:** `GET`

**Request:**

```http
RequestParam: 
String    iDate,
String    iBranch,
Integer   iClientType,
Integer   iState,
Integer   iValType,
String    iAccList,
String    iAccount,
String    iClientId
```

**Response:**

```json
{
  "status": "success",
  "error": "",
  "totalElements": 1,
  "data": [
    {
      "BRANCH": "00981",
      "ID_CLIENT": "12312312",
      "ACC_NAME": "ALIYEV ALIJON",
      "PINFL_INN": "12345678912345",
      "ACCOUNT": "12345678912345677896",
      "SALDO_NOM": 0.0,
      "SALDO_EQV": 0.0,
      "STATE": "Утвержден"
    }
  ]
}
```

---

### 3️⃣ Report Data With Pagination <a id="client-accounts-pagination"></a>

- **URL:** `/client/accounts/api/v1/pagination`
- **Method:** `GET`

**Request:**

```http
RequestParam:
String    iDate,
String    iBranch,
Integer   iClientType,
Integer   iState,
Integer   iValType,
String    iAccList,
String    iAccount,
String    iClientId,
Integer   iPageNumber,
Integer   iPageSize
```

**Response:**

```json
{
  "totalPages": 1,
  "last": true,
  "pageNumber": 1,
  "pageSize": 10,
  "numberOfElements": 1,
  "first": true,
  "empty": false,
  "totalElements": 1,
  "data": [
    {
      "BRANCH": "00981",
      "ID_CLIENT": "12312312",
      "ACC_NAME": "ALIYEV ALIJON",
      "PINFL_INN": "1234567891234",
      "ACCOUNT": "12345678912345678901",
      "SALDO_NOM": 0.0,
      "SALDO_EQV": 0.0,
      "STATE": "Утвержден"
    }
  ],
  "status": "success",
  "error": ""
}
```

---

### 4️⃣ Downloadable Report Excel File <a id="client-accounts-pagination-sort"></a>

- **URL:** `/client/accounts/api/v1/pagination/sort`
- **Method:** `GET`

**Request:**

```http
RequestParam: 
String    iDate,
String    iBranch,
Integer   iClientType,
Integer   iState,
Integer   iValType,
String    iAccList,
String    iAccount,
String    iClientId,
Integer   iPageNumber,
Integer   iPageSize,
String    iSort
```

**Response:**

```json
{
  "totalPages": 1,
  "last": true,
  "pageNumber": 1,
  "pageSize": 10,
  "numberOfElements": 1,
  "first": true,
  "empty": false,
  "totalElements": 1,
  "data": [
    {
      "BRANCH": "00981",
      "ID_CLIENT": "12312312",
      "ACC_NAME": "ALIYEV ALIJON",
      "PINFL_INN": "1234567891234",
      "ACCOUNT": "12345678912345678901",
      "SALDO_NOM": 0.0,
      "SALDO_EQV": 0.0,
      "STATE": "Утвержден"
    }
  ],
  "status": "success",
  "error": ""
}
```

### 5.  Downloadable Excel Report File <a id="client-accounts-export-excel"></a>

- **URL:** `/client/accounts/api/v1/export/excel`
- **Method:** `GET`

**Request:**

```http
RequestParam: 
String    iDate,
String    iBranch,
Integer   iClientType,
Integer   iState,
Integer   iValType,
String    iAccList,
String    iAccount,
String    iClientId,
Integer   iPageNumber,
Integer   iPageSize,
String    iSort
```

**Response:**

<details open>
<summary><b>RESPONSE SUCCESS</b></summary>
returns binap(the content of the XML file) info and can be downloaded Excel file ClientAccounts_date.xlsx 
</details>
<!--END OF DOWNLOAD REPORT-->
---

## 👤 Author

**Organization:** Davr Bank  
**Maintainer:** Hamdamboy Urunov, Shaxobiddin Nizomiddinov