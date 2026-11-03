<a href = "https://internal.example.com/bas-group/bus/reportservices/zalogauto-service/-/blob/test/README.md?ref_type=heads">
English</a>
/ <a href = "https://internal.example.com/bas-group/bus/reportservices/zalogauto-service/-/blob/test/README_ru.md?ref_type=heads">Russian</a>

# 📌 zalogauto-service

SMART BAS PROJECT

---

## ✅ About the Project

|    Components     |            Technology             | Status |
|:-----------------:|:---------------------------------:|:------:|
|      Backend      |    Java 17, Spring Boot 3.4.2     |   ✅    |
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
2. **git clone** https://internal.example.com/bas-group/bus/reportservices/zalogauto-service.git
3. **Build the project with Maven** -> this command will download all dependencies and build the JAR file.


### Documentation
The full API documentation is available via Swagger UI after running the application:

- **Swagger UI:** [http://localhost:7200/zalog/auto/api/swagger-ui/index.html](http://localhost:7200/zalog/auto/api/swagger-ui/index.html)
- **OpenAPI JSON:** [http://localhost:7200/zalog/auto/api/v3/api-docs](http://localhost:7200/zalog/auto/api/v3/api-docs)

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

## Get All zalog info

| No |         Api lists          |
|:--:|:--------------------------:|
| 1  | [Endpoints](#Endpoints) |

<details open>
<summary><b>API List</b> (click to expand)</summary>

| No |                      API Name                       |                                        URL                                         |
|:--:|:---------------------------------------------------:|:----------------------------------------------------------------------------------:|
| 1  |          Get all data (by Period & status)          |         [`/zalog/auto/api/v1/reports/get/all`](#zalog-auto-report-get-all)         |
| 2  |           Get all data (with Pagination)            |     [`/zalog/auto/api/v1/reports/pagination/`](#zalog-auto-report-pagination)      |
| 3  |        Get all data (with Pagination & Sort)        | [`/zalog/auto/api/v1/reports/pagination/sort`](#zalog-auto-report-pagination-sort) |
| 4  | Downloadable Excel File Report (by Period & status) |           [`/zalog/auto/api/v1/reports/excel`](#zalog-auto-report-excel)           |

</details>

---

## 📌 API Details

---

### 1️⃣ Report Data All <a id="zalog-auto-report-get-all"></a>

- **URL:** `/zalog/auto/api/v1/reports/get/all`
- **Method:** `POST`

**Request:**

```http
RequestParam: 
String    startDate
String    endDate
Integer   state
Integer   branch
```

**Response:**

```json
{
  "status": "success",
  "error": "",
  "totalElements": 564,
  "data": [
    {
      "PK": 1112123,
      "BRANCH": "00981",
      "ID_ANKETA": 123456,
      "ACCOUNT": "14901000123456789012",
      "ID_CLIENT_ZAEM": "12345678",
      "FIO_ZAEM": "TESTOV TEST TESTOVICH",
      "PINFL_INN_ZAEM": "12345678912345",
      "ID_CLIENT_ZALOG": "12345678",
      "NAME_ZALOG": "TESTOV TEST TESTOVICH",
      "PINFL_INN_ZALOG": "12345678912345",
      "ADDRESS_NCI": "SAMPLE MFY, SAMPLE STREET",
      "CAR_TYPE": "Легковой автомобиль",
      "CAR_DATE_MADE": "2025",
      "CAR_MARKA": "Chevrolet",
      "CAR_MODEL": "Damas       ",
      "ENGINE_NUM": "F812312341234",
      "BODY_NUM": "XWB7T12Y123451234",
      "STATE_NUMBER": "01A111AA",
      "TEX_PASSPORT": "AAG 1234567",
      "DOC_DATE": "07.11.2025",
      "CHASSIS_NUMBER": "RAQAMSIZ",
      "TRANSPORT_TYPE": "Легковой автомобиль",
      "GUAR_ID": "23",
      "GUAR_ADDRESS": "SAMPLE MFY, SAMPLE STREET",
      "GUAR_AMOUNT": 1111111234,
      "GUAR_CURR": "000",
      "STATUS": "Утвержден"
    }
  ]
}
```

---

### 2️⃣ Report Data With Pagination <a id="zalog-auto-report-pagination"></a>

- **URL:** `/zalog/auto/api/v1/reports/pagination/`
- **Method:** `POST`

**Request:**

```http
RequestParam: state, pageNumber=1, pageSize=10
```

**Response:**

```json
{
  "totalPages": 12,
  "last": false,
  "pageNumber": 1,
  "pageSize": 50,
  "numberOfElements": 50,
  "first": true,
  "empty": false,
  "data": [
    {
      "PK": 1112123,
      "BRANCH": "00981",
      "ID_ANKETA": 123456,
      "ACCOUNT": "14901000123456789012",
      "ID_CLIENT_ZAEM": "12345678",
      "FIO_ZAEM": "TESTOV TEST TESTOVICH",
      "PINFL_INN_ZAEM": "12345678912345",
      "ID_CLIENT_ZALOG": "12345678",
      "NAME_ZALOG": "TESTOV TEST TESTOVICH",
      "PINFL_INN_ZALOG": "12345678912345",
      "ADDRESS_NCI": "SAMPLE MFY, SAMPLE STREET",
      "CAR_TYPE": "Легковой автомобиль",
      "CAR_DATE_MADE": "2025",
      "CAR_MARKA": "Chevrolet",
      "CAR_MODEL": "Damas       ",
      "ENGINE_NUM": "F812312341234",
      "BODY_NUM": "XWB7T12Y123451234",
      "STATE_NUMBER": "01A111AA",
      "TEX_PASSPORT": "AAG 1234567",
      "DOC_DATE": "07.11.2025",
      "CHASSIS_NUMBER": "RAQAMSIZ",
      "TRANSPORT_TYPE": "Легковой автомобиль",
      "GUAR_ID": "23",
      "GUAR_ADDRESS": "SAMPLE MFY, SAMPLE STREET",
      "GUAR_AMOUNT": 1111111234,
      "GUAR_CURR": "000",
      "STATUS": "Утвержден"
    }
  ],
  "status": "success",
  "totalElements": 1,
  "error": ""
}
```

---

### 3️⃣ Report Data With Pagination & Sort <a id="zalog-auto-report-pagination-sort"></a>

- **URL:** `/zalog/auto/api/v1/reports/pagination/sort`
- **Method:** `POST`

**Request:**

```http
RequestParam: state, pageNumber=1, pageSize=10, sort="1 asc, 2 desc, 7 desc"
```

**Response:**

```json
{
  "successCode": 200,
  "isSuccess": true,
  "status": "SUCCESS",
  "result": [
    {
      "PK": 1112123,
      "BRANCH": "00981",
      "ID_ANKETA": 123456,
      "ACCOUNT": "14901000123456789012",
      "ID_CLIENT_ZAEM": "12345678",
      "FIO_ZAEM": "TESTOV TEST TESTOVICH",
      "PINFL_INN_ZAEM": "12345678912345",
      "ID_CLIENT_ZALOG": "12345678",
      "NAME_ZALOG": "TESTOV TEST TESTOVICH",
      "PINFL_INN_ZALOG": "12345678912345",
      "ADDRESS_NCI": "SAMPLE MFY, SAMPLE STREET",
      "CAR_TYPE": "Легковой автомобиль",
      "CAR_DATE_MADE": "2025",
      "CAR_MARKA": "Chevrolet",
      "CAR_MODEL": "Damas       ",
      "ENGINE_NUM": "F812312341234",
      "BODY_NUM": "XWB7T12Y123451234",
      "STATE_NUMBER": "01A111AA",
      "TEX_PASSPORT": "AAG 1234567",
      "DOC_DATE": "07.11.2025",
      "CHASSIS_NUMBER": "RAQAMSIZ",
      "TRANSPORT_TYPE": "Легковой автомобиль",
      "GUAR_ID": "23",
      "GUAR_ADDRESS": "SAMPLE MFY, SAMPLE STREET",
      "GUAR_AMOUNT": 1111111234,
      "GUAR_CURR": "000",
      "STATUS": "Утвержден"
    }
  ],
  "error": null,
  "timeStamp": 1698765432112
}
```

---

### 4️⃣ Downloadable Report Excel File <a id="zalog-auto-report-excel"></a>

- **URL:** `/zalog/auto/api/v1/reports/excel`
- **Method:** `GET`

**Request:**

```http
RequestParam: state, branch
```
<details open>
<summary><b>RESPONSE SUCCESS</b></summary>
returns binap(the content of the XML file) info and can be downloaded Excel file ZalogAuto_from_startDate_to_endDate.xlsx 
</details>
<!--END OF DOWNLOAD REPORT-->
---

## 👤 Author

**Organization:** Davr Bank  
**Maintainer:** Hamdamboy Urunov, Shaxobiddin Nizomiddinov