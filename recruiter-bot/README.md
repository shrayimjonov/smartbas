<a href = "https://internal.example.com/hr-team/backend/recruiterbot/-/blob/prod/README.md">English</a>
### Jobs Davrbank Service

***1. About project***

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

|    Components     |          Technology           | Status |
|:-----------------:|:-----------------------------:|:------:|
|      Backend      |  Java 17,  Spring Boot 3.4.2  |   ✅    |
|     Database      |   JPA-Hibernate, Postgresql   |   ✅    |
| API Documentation | Swagger API, JavaDoc, POSTMAN |   ✅    |
|   Server Build    |             Maven             |   ✅    |
|    Server Test    |     Linux (127.0.0.1)      |   ✅    |
|    Backend Port   |             5638              |   ✅    |
| API testing tool  |      POSTMAN  / Insomnia      |   ✅    |
|       Tool        |       IntelliJ Idea/VSC       |   ✅    |
|   Architecture    |           Monolith            |   ✅    |
|        CI         |       gitlab CI (Test)        |   ✅    |
|       CI/CD       |   gitlab CI/CD argocd(Prod)   |   ✅    |
|       Infro       |    Docker (Test)/K8S(Prod)    |   ✅    |
|    Clean Code     |      SAST scanner(**/5)       |   ✅    |
|     Security      |              JWT              |   ✅    |
|        UI         |         Angular/React         |   ✅    |
|    Monitoring     |    Portainer(127.0.0.1)    |   ✅    |

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


| No |               Api lists                |
|:--:|:--------------------------------------:|
| 1  |    [Archive-controller](#Endpoints)    |
| 1  |     [Auth-controller](#Endpoints)      |
| 1  |  [Create Test-controller](#Endpoints)  |
| 1  |  [Department-controller](#Endpoints)   |
| 1  |  [File Upload-controller](#Endpoints)  |
| 1  |    [Message-controller](#Endpoints)    |
| 1  |  [Monitoring-controller](#Endpoints)   |
| 1  | [Photo upload -controller](#Endpoints) |
| 1  |     [User-controller](#Endpoints)      |
| 1  |   [User test-controller](#Endpoints)   |

<details open>
<summary><b>Api of Contents </b></summary>
<a id="API contents" ></a>

<details open>
<summary><b>Endpoints </b>(click to expand or hide)</summary>
<a id="Endpoints" ></a>

| No | Api Method |                            url                             |
|:--:|:----------:|:----------------------------------------------------------:|
| 1  |    GET     |         http://localhost:5638/api/archieve/search          |
| 2  |   DELETE   |          http://localhost:5638/api/archieve/{id}           
| 3  |    POST    |            http://localhost:5638/api/auth/login            |
| 4  |    POST    |           http://localhost:5638/api/auth/refresh           |
| 5  |    POST    |       http://localhost:5638/api/auth/change-password       |
| 6  |    POST    |           http://localhost:5638/api/tests/create           |
| 7  |    PUT     |        http://localhost:5638/api/tests/update/{id}         |
| 8  |    GET     |       http://localhost:5638/api/tests/{departmentId}       |
| 9  |    GET     |       http://localhost:5638/api/tests/availableTests       |
| 10 |   DELETE   |    http://localhost:5638/api/tests/{departmentId}/{id}     |
| 11 |   DELETE   |      http://localhost:5638/api/department/delete/{id}      |
| 12 |    GET     |       http://localhost:5638/api/department/find/{id}       |
| 13 |    GET     |            http://localhost:5638/api/department            |
| 14 |    POST    |         http://localhost:5638/api/v1/createResume          |
| 15 |    GET     |             http://localhost:5638/api/v1/{id}              |
| 16 |    POST    |          http://localhost:5638/api/messages/post           |
| 17 |    POST    |    http://localhost:5638/api/messages/postWithSchedule     |
| 18 |    GET     |          http://localhost:5638/api/messages/{id}           |
| 19 |    GET     |             http://localhost:5638/api/messages             |
| 20 |    PUT     |           http://localhost:5638/api/messages{id}           |
| 21 |   DELETE   |          http://localhost:5638/api/messages/{id}           |
| 22 |    GET     |         http://localhost:5638/api/messages/search          |
| 23 |    GET     |             http://localhost:5638/api/monitor              |
| 24 |    GET     |               http://localhost:5638/api/logs               |
| 25 |    POST    |             http://localhost:5638/api/v1/photo             |
| 26 |    GET     |          http://localhost:5638/api/v1/photo/{id}           |
| 27 |    POST    |              http://localhost:5638/api/users               |
| 28 |    POST    |         http://localhost:5638/api/v1/tests/submit          |
| 29 |    GET     |          http://localhost:5638/api/v1/tests/daily          |
| 31 |    GET     | http://localhost:5638/api/v1/tests/lastSixMonthsStatistics |
| 32 |    GET     |    http://localhost:5638/api/v1/tests/user-test-details    |
| 33 |    PUT     |       http://localhost:5638/api/v1/tests/notes/{id}        |
| 34 |    GET     |           http://localhost:5638/api/v1/tests/all           |


## Contact

If you have any questions or suggestions, feel free to open an issue or reach out to:

*   **Guliza Tulkinova:** [user@example.com](mailto:user@example.com)
*   **Project Developer:** [GitHub account](https://github.com/guliza0707)

</details>
</details>

