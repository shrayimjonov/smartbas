
<a href = "https://internal.example.com/bas-group/bus/reportservices/zalogipoteka-service/-/blob/prod/README.md?ref_type=heads">
English</a>
/ <a href = "https://internal.example.com/bas-group/bus/reportservices/zalogipoteka-service/-/blob/prod/README_RU.md?ref_type=heads">Russian</a>

# 🏦 Zalog Ipoteka Service

---

## This is one of services of SMART BAS project which gets all data about Zalog Ipoteka


##  🧱 Table of Contents

- [Features](#features)
- [Technologies Used](#technologies-used)
- [Getting Started](#getting-started)
    - [Prerequisites](#prerequisites)
    - [Installation](#installation)
    - [Documentation](#documentation)
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
|      Port         |              7201                 |   ✅    |
|     Database      |             Oracle DB             |   ✅    |
| API Documentation |     Swagger, JavaDoc, Postman     |   ✅    |
|    Build Tool     |               Maven               |   ✅    |
|     Server OS     |               Linux               |   ✅    |
| API Testing Tool  |        Postman / Insomnia         |   ✅    |
|    IDE/Editor     |      IntelliJ IDEA / VS Code      |   ✅    |
|   Architecture    |           Microservice            |   ✅    |
|        CI         |         GitLab CI (Prod)          |   ✅    |
|       CI/CD       | GitLab CI/CD, ArgoCD (Production) |   ✅    |
|  Infrastructure   |  Docker (Test)Kubernetes(Prod)    |   ✅    |
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
2. **git clone** https://internal.example.com/bas-group/bus/reportservices/zalogipoteka-service.git
3. **Build the project with Maven** -> this command will download all dependencies and build the JAR file.


### Documentation 
The full API documentation is available via Swagger UI after running the application:

- **Swagger UI:** [http://localhost:7201/zalog/ipoteka/api/swagger-ui/index.html](http://localhost:7201/zalog/ipoteka/api/swagger-ui/index.html)
- **OpenAPI JSON:** [http://localhost:7201/zalog/ipoteka/api/v3/api-docs](http://localhost:7201/zalog/ipoteka/api/v3/api-docs)

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
<summary><b>Api of Contents </b></summary>
<a id="API contents" ></a>

<details open>
<summary><b>Endpoints </b>(click to expand or hide)</summary>
<a id="Endpoints" ></a>

| No |                   Api name                   |                          url                           |
|:--:|:--------------------------------------------:|:------------------------------------------------------:|
| 1  |              GET All ZALOG                   |        [/zalog/ipoteka/api/v1/reports/get/all]         |
| 2  |         GET ALL ZALOG INFO PAGEABLE          |      [/zalog/ipoteka/api/v1/reports/pagination]        |
| 3  |    GET ALL ZALOG INFO PAGEABLE AND SORTED    | [/zalog/ipoteka/api/v1/reports/pagination/sort]        |
| 4  |                DOWNLOAD EXCEL                |     [/zalog/ipoteka/api/v1/reports/download/excel]     | (#localhost-7201-api-get-all-download-report)                             |

</details>

<details open>
<!--GET All ZALOG by cadastreType and state  -->
<summary><b> GET All ZALOG</b>(click to expand or hide)</summary>
<a id="localhost-7201-api-get-all"></a>

#### http://localhost:7201/zalog/ipoteka/api/v1/reports/get/all?

<b>Method name: POST</b>

<details open>
<summary><b>REQUEST PARAM</b></summary>

```http
cadastreType: 1 #required
state:2 
startDate   
endDate
branch
pinfl
loanId
clientName
cadastreNum
```

</details>
<details open>
<summary><b>RESPONSE SUCCESS</b></summary>

```json
{
  "status": "success",
  "totalElements": 348,
  "error": "",
  "data": [

    {
      "PK": 67453,
      "BRANCH": "01122",
      "ID_ANKETA": 6839,
      "ACCOUNT": "14901000299017407001",
      "ID_CLIENT_ZAEM": "99017407",
      "FIO_ZAEM": "TESTOVA TEST TESTOVNA",
      "PINFL_INN_ZAEM": "30000000000000",
      "ID_CLIENT_ZALOG": "99017407",
      "FIO_ZALOG": "TESTOVA TEST TESTOVNA",
      "PINFL_INN_ZALOG": "30000000000000",
      "ADDRESS_NCI": "г. Ташкент, Sample район, ул. Sample, 1",
      "CERTIFICATE_NUM": "2-7334",
      "CERTIFICATE_DATE": "24.12.2018",
      "CADASTRE_TYPE": 1,
      "CADASTRE_NUM": "10:07:06:03:01:5008:0001:056",
      "REYESTR_NUM": "1-1007-18-08036",
      "SQUARE": 70,
      "OVNERSHIP": 1,
      "CADASTRE_AMOUNT": 7786962300,
      "ESTIMATE_INN": "207212342",
      "DOC_DATE": "25.12.2018",
      "GUAR_ID": "22",
      "GUAR_ADDRESS": "Sample шахар, Sample тумани, 1-уй",
      "GUAR_AMOUNT": 29563750000,
      "GUAR_CURR": "000",
      "STATUS": "Утвержден"
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
<!--END GET ALL ZALOG -->


<!--GET ALL ZALOG INFO PAGEABLE   -->
<details open>
<summary><b>GET ALL ZALOG INFO PAGEABLE</b>(click to expand or hide)</summary>
<a id="localhost-7201-api-get-all-pageable"></a>
         
#### http://localhost:7201/zalog/ipoteka/api/v1/reports/pagination?

<b>Method name: POST</b>
<details open>


<summary><b>REQUEST PARAM</b></summary>

```http

cadastreType: 1 #required
state:2  #required 
startDate   
endDate
branch:01122  #required
loanId
pinfl
clientName
cadastreNum
pageNumber:1  #required
pageSize:20   #required
```
</details>

<details open>
<summary><b>RESPONSE SUCCESS</b></summary>

```json

   {
    "totalPages": 18,
    "last": false,
    "pageNumber": 1,
    "pageSize": 20,
    "numberOfElements": 20,
    "first": true,
    "empty": false,
    "data": [
        {
          "PK": 8209,
          "BRANCH": "01122",
          "ID_ANKETA": 580,
          "ACCOUNT": "15501000400574474001",
          "ID_CLIENT_ZAEM": "00574474",
          "FIO_ZAEM": "МЧЖ \"SAMPLE COMPANY\"",
          "PINFL_INN_ZAEM": "303699164",
          "ID_CLIENT_ZALOG": "99005774",
          "FIO_ZALOG": "SAMPLEV SAMPLE SAMPLEVICH",
          "PINFL_INN_ZALOG": "30000000000000",
          "ADDRESS_NCI": "Sample тумани, Sample кўчаси, 1-уй",
          "CERTIFICATE_NUM": "TA2055412",
          "CERTIFICATE_DATE": "16.03.2016",
          "CADASTRE_TYPE": 1,
          "CADASTRE_NUM": "10:01:01:02:1007:49",
          "REYESTR_NUM": "14747",
          "SQUARE": 72,
          "OVNERSHIP": 1,
          "CADASTRE_AMOUNT": 30056900000,
          "ESTIMATE_INN": "304305506",
          "DOC_DATE": "19.03.2016",
          "GUAR_ID": "21",
          "GUAR_ADDRESS": "Sample вилояти, Sample тумани, Sample кўчаси, 1-уй",
          "GUAR_AMOUNT": 12500000000,
          "GUAR_CURR": "000",
          "STATUS": "Утвержден"
        },
      {
        "PK": 69646,
        "BRANCH": "01122",
        "ID_ANKETA": 7183,
        "ACCOUNT": "15501000300229998006",
        "ID_CLIENT_ZAEM": "00229998",
        "FIO_ZAEM": "МЧЖ \"EXAMPLE TRADE\"",
        "PINFL_INN_ZAEM": "302618644",
        "ID_CLIENT_ZALOG": "99007735",
        "FIO_ZALOG": "ESHMATOV TOSHMAT TESHABOY OGLI",
        "PINFL_INN_ZALOG": "30000000000000",
        "ADDRESS_NCI": "Sample тумани, Sample кўчаси, 1-уй",
        "CERTIFICATE_NUM": "TS0254640",
        "CERTIFICATE_DATE": "05.05.2017",
        "CADASTRE_TYPE": 1,
        "CADASTRE_NUM": "10:08:01:01:02:5075",
        "REYESTR_NUM": "9/139",
        "SQUARE": 74,
        "OVNERSHIP": 1,
        "CADASTRE_AMOUNT": 95457700000,
        "ESTIMATE_INN": "304305506",
        "DOC_DATE": "23.01.2019",
        "GUAR_ID": "22",
        "GUAR_ADDRESS": "Sample шахри, Sample тумани, 1-уй",
        "GUAR_AMOUNT": 56250000000,
        "GUAR_CURR": "000",
        "STATUS": "Утвержден"
      }
  ]
}
```

</details>
<details open>
<summary><b>RESPONSE ERROR</b></summary>
</details>
</details>
<!--END GET ALL ZALOG PAGEABLE-->



<!--GET ALL ZALOG INFO PAGEABLE AND SORTED-->
<details open>
<summary><b>GET ALL ZALOG INFO PAGEABLE AND SORTED</b>(click to expand or hide)</summary>
<a id="localhost-7201-api-get-all-pageable-sorted"></a>

#### http://localhost:7201/zalog/ipoteka/api/v1/reports/pagination/sort?

<b>Method name: POST</b>

<details open>
<summary><b>REQUEST PARAM</b></summary>

```http
cadastreType: 1 #required
state:2  #required 
startDate   
endDate
branch:01122  #required
loanId
pinfl
clientName
cadastreNum
pageNumber:1  #required
pageSize:20   #required
sort: 1 asc,2 desc
```
</details>

<details open>
<summary><b>RESPONSE SUCCESS</b></summary>

```json

{
  "status": "success",
  "totalElements": 776,
  "error": "",
  "totalPages": 8,
  "last": false,
  "pageNumber": 1,
  "pageSize": 100,
  "numberOfElements": 100,
  "first": true,
  "empty": false,
  "data": [
    
    {
      "PK": 67453,
      "BRANCH": "01122",
      "ID_ANKETA": 6839,
      "ACCOUNT": "14901000299017407001",
      "ID_CLIENT_ZAEM": "99017407",
      "FIO_ZAEM": "TESTOVA TEST TESTOVNA",
      "PINFL_INN_ZAEM": "30000000000000",
      "ID_CLIENT_ZALOG": "99017407",
      "FIO_ZALOG": "TESTOVA TEST TESTOVNA",
      "PINFL_INN_ZALOG": "30000000000000",
      "ADDRESS_NCI": "г. Ташкент, Sample район, ул. Sample, 1",
      "CERTIFICATE_NUM": "2-7334",
      "CERTIFICATE_DATE": "24.12.2018",
      "CADASTRE_TYPE": 1,
      "CADASTRE_NUM": "10:07:06:03:01:5008:0001:056",
      "REYESTR_NUM": "1-1007-18-08036",
      "SQUARE": 70,
      "OVNERSHIP": 1,
      "CADASTRE_AMOUNT": 7786962300,
      "ESTIMATE_INN": "207212342",
      "DOC_DATE": "25.12.2018",
      "GUAR_ID": "22",
      "GUAR_ADDRESS": "Sample шахар, Sample тумани, 1-уй",
      "GUAR_AMOUNT": 29563750000,
      "GUAR_CURR": "000",
      "STATUS": "Утвержден"
    },
    {
      "PK": 69646,
      "BRANCH": "01122",
      "ID_ANKETA": 7183,
      "ACCOUNT": "15501000300229998006",
      "ID_CLIENT_ZAEM": "00229998",
      "FIO_ZAEM": "МЧЖ \"EXAMPLE TRADE\"",
      "PINFL_INN_ZAEM": "302618644",
      "ID_CLIENT_ZALOG": "99007735",
      "FIO_ZALOG": "ESHMATOV TOSHMAT TESHABOY OGLI",
      "PINFL_INN_ZALOG": "30000000000000",
      "ADDRESS_NCI": "Sample тумани, Sample кўчаси, 1-уй",
      "CERTIFICATE_NUM": "TS0254640",
      "CERTIFICATE_DATE": "05.05.2017",
      "CADASTRE_TYPE": 1,
      "CADASTRE_NUM": "10:08:01:01:02:5075",
      "REYESTR_NUM": "9/139",
      "SQUARE": 74,
      "OVNERSHIP": 1,
      "CADASTRE_AMOUNT": 95457700000,
      "ESTIMATE_INN": "304305506",
      "DOC_DATE": "23.01.2019",
      "GUAR_ID": "22",
      "GUAR_ADDRESS": "Sample шахри, Sample тумани, 1-уй",
      "GUAR_AMOUNT": 56250000000,
      "GUAR_CURR": "000",
      "STATUS": "Утвержден"
    }
  ]
}
```


</details>
<details open>
<summary><b>RESPONSE ERROR</b></summary>
</details>
</details>
<!--END GET ALL ZALOG PAGEABLE AND SORTED-->



<!--GET REPORT EXCEL-->
<details open>
<summary><b>GET REPORT EXCEL</b>(click to expand or hide)</summary>
<a id="localhost-7201-api-get-all-download-report"></a>

####  http://localhost:7201/zalog/ipoteka/api/v1/reports/download/excel?

<b>Method name: GET</b>

<details open>
<summary><b>REQUEST PARAM</b></summary>

```http
cadastreType: 1 #required
state:2  #required 
startDate   
endDate
branch:01122  #required
loanId
clientName
cadastreNum
pinfl
```
</details>


<details open>
<summary><b>RESPONSE SUCCESS</b></summary>
returns binap(the content of the XML file) info and can be downloaded Excel file ZalogIpoteka_startDate_to_endDate.xlsx 
</details>
</details>
<!--END OF DOWNLOAD REPORT-->



## Contact

If you have any questions or suggestions, feel free to open an issue or reach out to:

*   **Guliza Tulkinova:** [user@example.com](mailto:user@example.com)
*   **Project Developer:** [GitHub account](https://github.com/guliza0707)









