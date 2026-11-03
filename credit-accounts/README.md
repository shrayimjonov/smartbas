
<a href = "https://internal.example.com/bas-group/bus/reportservices/creditaccounts-service/-/blob/test/README.md?ref_type=heads">
English</a>
/ <a href = "https://internal.example.com/bas-group/bus/reportservices/creditaccounts-service/-/blob/test/README_RU.md?ref_type=heads">Russian</a>

# 🏦 Credit Account Service

---

## This is one of services of SMART BAS project which gets all data about Credit Accounts and generates an excel file


##  🧱 Table of Contents

- [Features](#features)
- [Technologies Used](#technologies-used)
- [Getting Started](#getting-started)
    - [Prerequisites](#prerequisites)
    - [Installation](#installation)
    - [Running the Application](#running-the-application)
- [API Endpoints](#api-endpoints) (if applicable)
- [Contact](#contact)


##  🔐 Features 

#### ⚙️Technologies Used

- List of technologies used project build

<div>

---
|    Components     |            Technology             | Status |
|:-----------------:|:---------------------------------:|:------:|
|      Backend      |    Java 17, Spring Boot 3.4.2     |   ✅    |
|      Port         |               7205                |   ✅    |
|     Database      |             Oracle DB             |   ✅    |
| API Documentation |     Swagger, JavaDoc, Postman     |   ✅    |
|    Build Tool     |               Maven               |   ✅    |
|     Server OS     |               Linux               |   ✅    |
| API Testing Tool  |        Postman / Insomnia         |   ✅    |
|    IDE/Editor     |      IntelliJ IDEA / VS Code      |   ✅    |
|   Architecture    |           Microservice            |   ✅    |
|        CI         |         GitLab CI (Prod)          |   ✅    |
|       CI/CD       | GitLab CI/CD, ArgoCD (Production) |   ✅    |
|  Infrastructure   |   Docker (Test)Kubernetes(Prod)   |   ✅    |
|    Clean Code     |           SAST Scanner            |   ✅    |
|      Caching      |               Redis               |   ✅    |
|     Security      |     OAuth 2.0, Keycloak, JWT      |   ✅    |
|        UI         |          Angular / React          |   ✅    |
|    Monitoring     |             Portainer             |   ✅    |

---

</div>

![Java](https://img.shields.io/badge/Java-17-blue?logo=java)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.2.0-green?logo=spring)
![Maven](https://img.shields.io/badge/Maven-3.8.8-red?logo=apache-maven)
![Docker](https://img.shields.io/badge/Docker-24.0.5-blue?logo=docker)


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
2. **git clone** https://internal.example.com/bas-group/bus/reportservices/creditaccounts-service.git
3. **Build the project with Maven** -> this command will download all dependencies and build the JAR file.



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

## Get All credit account info 

| No |         Api lists          |
|:--:|:--------------------------:|
| 1  | [Endpoints](#Endpoints) |

<details open>
<summary><b>Api of Contents </b></summary>
<a id="API contents" ></a>

<details open>
<summary><b>Endpoints </b>(click to expand or hide)</summary>
<a id="Endpoints" ></a>

| No |                Api name                |                               url                                |
|:--:|:--------------------------------------:|:----------------------------------------------------------------:|
| 1  |         GET All CREDIT ACCOUNT         |            [/credit/accounts/api/v1/reports/get/all ]            |
| 2  |      GET All CREDIT ACCOUNT INFO PAGEABLE       |     [  /credit/accounts/api/v1/reports/get/all/pageable   ]      |
| 3  | GET All CREDIT ACCOUNT INFO PAGEABLE AND SORTED | [ /credit/accounts/api/v1/reports/get/all/pageable/sorted?iDate] |
| 4  |             DOWNLOAD EXCEL             |         [/credit/accounts/api/v1/reports/download/excel]         |

</details>
z
<details open>
<!--GET All CREDIT ACCOUNTS by date,branch,state,valType,account  -->                                                                                                                                     
<summary><b> GET All CREDIT ACCOUNTS</b>(click to expand or hide)</summary>
<a id="localhost-7205-api-get-all-credit-account"></a>

#### http://localhost:7205/credit/accounts/api/v1/reports/get/all?

<b>Method name: POST</b>

<details open>
<summary><b>REQUEST PARAM</b></summary>

```http
date
branch
state
valType
account
```

</details>
<details open>
<summary><b>RESPONSE SUCCESS</b></summary>

```json
{
  "status": "success",
  "totalElements": 32120,
  "error": "",
  "data": [
    {
      "BRANCH": "00427",
      "ANKETA_ID": "5832",
      "ACCOUNT": "27488900345167294018",
      "ACC_TYPE_ID": 31,
      "ACC_TYPE_NAME": "Счет просроченной  задолженности по процентам, срок погашения которых наступил",
      "SALDO_OUT": 0.0,
      "STATE": 2,
      "STATE_NAME": "Утвержден"
    },
    {
      "BRANCH": "00427",
      "ANKETA_ID": "74851",
      "ACCOUNT": "27488900678294531027",
      "ACC_TYPE_ID": 31,
      "ACC_TYPE_NAME": "Счет просроченной задолженности по процентам, срок погашения которых наступил",
      "SALDO_OUT": 0.0,
      "STATE": 2,
      "STATE_NAME": "Утвержден"
    }
    
]
}
```

</details>
<details open>
<summary><b>RESPONSE ERROR</b></summary>
</details>
</details>
</details>
<!--END GET ALL CREDIT ACCOUNTS -->


<!--GET ALL CREDIT ACCOUNTS INFO PAGEABLE   -->
<details open>
<summary><b>GET ALL CREDIT ACCOUNTS INFO PAGEABLE</b>(click to expand or hide)</summary>
<a id="localhost-7205-api-get-all-pageable"></a>
         
#### http://localhost:7205/credit/accounts/api/v1/reports/get/all/pageable?

<b>Method name: POST</b>
<details open>


<summary><b>REQUEST PARAM</b></summary>

```http

date
branch
state
valType
account
pageNumber
pageSize
```
</details>

<details open>
<summary><b>RESPONSE SUCCESS</b></summary>

```json

{
  "totalPages": 16060,
  "last": false,
  "pageNumber": 1,
  "pageSize": 2,
  "numberOfElements": 2,
  "first": true,
  "empty": false,
  "data": [
    {
      "BRANCH": "00427",
      "ANKETA_ID": "5832",
      "ACCOUNT": "27488900345167294018",
      "ACC_TYPE_ID": 31,
      "ACC_TYPE_NAME": "Счет просроченной  задолженности по процентам, срок погашения которых наступил",
      "SALDO_OUT": 0.0,
      "STATE": 2,
      "STATE_NAME": "Утвержден"
    },
    {
      "BRANCH": "00427",
      "ANKETA_ID": "74851",
      "ACCOUNT": "27488900678294531027",
      "ACC_TYPE_ID": 31,
      "ACC_TYPE_NAME": "Счет просроченной задолженности по процентам, срок погашения которых наступил",
      "SALDO_OUT": 0.0,
      "STATE": 2,
      "STATE_NAME": "Утвержден"
    }
  ]
}
```

</details>
<details open>
<summary><b>RESPONSE ERROR</b></summary>
</details>
</details>
<!--END GET ALL CREDIT ACCOUNTS PAGEABLE-->



<!--GET ALL CREDIT ACCOUNTS INFO PAGEABLE AND SORTED-->
<details open>
<summary><b>GET ALL CREDIT ACCOUNTS INFO PAGEABLE AND SORTED</b>(click to expand or hide)</summary>
<a id="localhost-7205-api-get-all-pageable-sorted"></a>

#### http://localhost:7205/credit/accounts/api/v1/reports/get/all/pageable/sorted?

<b>Method name: POST</b>

<details open>
<summary><b>REQUEST PARAM</b></summary>

```http
date
branch
state
valType
account
pageNumber
pageSize
sort:
```
</details>

<details open>
<summary><b>RESPONSE SUCCESS</b></summary>

```json

{
  "totalPages": 16060,
  "last": false,
  "pageNumber": 1,
  "pageSize": 2,
  "numberOfElements": 2,
  "first": true,
  "empty": false,
  "data": [
    {
      "BRANCH": "00427",
      "ANKETA_ID": "5832",
      "ACCOUNT": "27488900345167294018",
      "ACC_TYPE_ID": 31,
      "ACC_TYPE_NAME": "Счет просроченной  задолженности по процентам, срок погашения которых наступил",
      "SALDO_OUT": 0.0,
      "STATE": 2,
      "STATE_NAME": "Утвержден"
    },
    {
      "BRANCH": "00427",
      "ANKETA_ID": "74851",
      "ACCOUNT": "27488900678294531027",
      "ACC_TYPE_ID": 31,
      "ACC_TYPE_NAME": "Счет просроченной задолженности по процентам, срок погашения которых наступил",
      "SALDO_OUT": 0.0,
      "STATE": 2,
      "STATE_NAME": "Утвержден"
    }
  ]
}
```


</details>
<details open>
<summary><b>RESPONSE ERROR</b></summary>
</details>
</details>
<!--END GET ALL CREDIT ACCOUNTS PAGEABLE AND SORTED-->



<!--GET REPORT EXCEL-->
<details open>
<summary><b>GET REPORT EXCEL</b>(click to expand or hide)</summary>
<a id="localhost-7205-api-get-all-download-report"></a>

#### http://localhost:7205/credit/accounts/api/v1/reports/download/excel?

<b>Method name: GET</b>

<details open>
<summary><b>REQUEST PARAM</b></summary>

```http
date
branch
state
valType
account
pageNumber
pageSize
```
</details>


<details open>
<summary><b>RESPONSE SUCCESS</b></summary>
returns binap(the content of the XML file) info and can be downloaded Excel file Credit_accounts_(%s).xlsx by given branch and Credit_accounts_(All_branches).xlsx for all branches
</details>
</details>
<!--END OF DOWNLOAD REPORT-->



## Contact

If you have any questions or suggestions, feel free to open an issue or reach out to:

*   **Guliza Tulkinova:** [user@example.com](mailto:user@example.com)
*   **Project Developer:** [GitHub account](https://github.com/guliza0707)









