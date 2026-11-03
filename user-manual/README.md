# 📘 User Manual Service

A Spring Boot microservice for managing hierarchical User Manual sections and their document versions, including file
storage to MinIO. The service exposes APIs to create, update, delete, tree-fetch sections, manage version lifecycle (
Draft/Published/Archived), and upload documentation files.

## 🧭 Table of Contents

- [Features](#features)
- [Technologies](#technologies)
- [Architecture](#architecture)
- [Getting Started](#getting-started)
    - [Prerequisites](#prerequisites)
    - [Clone & Build](#clone--build)
    - [Configuration](#configuration)
    - [Run](#run)
    - [Docker](#docker)
- [API Documentation](#api-documentation)
- [API Endpoints](#api-endpoints)
    - [User Manual Sections](#user-manual-sections)
    - [User Manual Versions](#user-manual-versions)
    - [File Uploads](#file-uploads)
- [Response Wrapper](#response-wrapper)
- [Database & Migrations](#database_and_migrations)
- [Conventions](#conventions)
- [Contact](#contact)

##   🔐 Features

- Tree-structured User Manual sections (recursive parent/child).
- Platform segmentation via PlatformType (ADMIN, CABINET) for tree listing.
- Version management with status lifecycle: DRAFT, PUBLISHED, ARCHIVED.
- Get detail for a section (with its subtree) and version by ID.
- Upload files to MinIO:
    - Base64 (structured path: section/version/language/filename)
    - Multipart upload for a single file into structured path
    - Multipart upload for multiple files into common/ path (replaces existing by same name)
- Consistent API responses via ResultData wrapper.
- Database schema managed by Flyway.

<a id="technologies"></a>
#### ⚙️Technologies Used
- List of technologies used project build
<div>

---
|    Component     |              Technology              | Status |
|:----------------:|:------------------------------------:|:------:|
|     Backend      |      Java 17, Spring Boot 3.4.2      |   ✅    |
|       Port       |                 7201                 |   ✅    |
|    Build Tool    |                Maven                 |   ✅    |
|     Database     |              Oracle DB               |   ✅    |
|    Migrations    |                Flyway                |   ✅    |
|     API Docs     |           OpenAPI/Swagger            |   ✅    |
|  Object Storage  |                MinIO                 |   ✅    |
|    Packaging     | Docker (docker-compose.yml provided) |   ✅    |
| API Testing Tool |          Postman / Insomnia          |   ✅    |
|    IDE/Editor    |            IntelliJ IDEA             |   ✅    |
|   Architecture   |             Microservice             |   ✅    |
|  Infrastructure  |            Docker (Test)             |   ✅    |

---

</div>

![Java](https://img.shields.io/badge/Java-17-blue?logo=java)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.4.2-green?logo=spring)
![Maven](https://img.shields.io/badge/Maven-3.9.11-red?logo=apache-maven)
![Docker](https://img.shields.io/badge/Docker-24.0.5-blue?logo=docker)


<a id="architecture"></a>
## 🧱Architecture

- Layered design: controller -> service -> repository -> database.
- Entities:
    - UserManualSection: id, name (unique), description, parentId, platformType.
    - UserManualVersion: id, versionNumber (unique per section), sectionId, status.
- File storage to MinIO bucket under structured keys.

<a id="getting-started"></a>
## 🚀Getting Started

<a id="prerequisites"></a>
### Prerequisites

- Java 17+
- Maven 3.8+
- Oracle DB (or access to an instance)
- MinIO (or S3-compatible storage)
- Docker (optional, if using docker-compose)

<a id="clone--build"></a>
### Clone & Build

```bash
# clone
git clone <this-repository-url>
cd usermanual-service

# build
mvn clean install
```

<a id="configuration"></a>
### ⚙️ Configuration

#### Available Spring Profiles

- **test** — default profile (`src/main/resources/application.yml`)
- **local** — used for local development

---

### 📂 Key Configuration Files

- `src/main/resources/application-test.yml` — active by default
- `src/main/resources/application-local.yml` — used in local environment

---

### 🔧 Important Configuration Properties (local profile example)

#### **Server**
```yaml
server:
  port: 7401

spring:
  datasource:
    url: jdbc:oracle:thin:@localhost:1521/FREEPDB1
    username: <username>
    password: <password>
    
spring:
  flyway:
    enabled: true
    locations: classpath:db/migration/oracle

external:
  min-io:
    host: http://localhost:9000
    bucket: user-manual
    access-key: <your-access-key>
    secret-key: <your-secret-key>

spring:
  servlet:
    multipart:
      max-file-size: 100MB
      max-request-size: 100MB

```

Switch profile:


```bash
# run with local profile
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

<a id="run"></a>
### Run

- Test profile (default): server.port=7400
- Local profile: server.port=7401

Run as JAR after build:

```bash
java -jar target/usermanual-service-0.0.1-SNAPSHOT.jar --spring.profiles.active=local
```

<a id="docker"></a>
### Docker

A sample docker-compose.yml is provided. Typical usage:

```bash
docker compose up -d
# or
docker-compose up -d
```

Adjust environment variables and service dependencies inside docker-compose.yml as needed.

<a id="api-documentation"></a>
## 📚 API Documentation

Swagger UI va OpenAPI spetsifikatsiyalari servis ishga tushirilgandan so‘ng mavjud bo‘ladi.

Quyidagi URL manzillar profil va portga qarab farq qilishi mumkin:

### 🔗 Swagger UI
- `http://localhost:7400/swagger-ui/index.html`
- `http://localhost:7401/swagger-ui/index.html`

### 🔗 OpenAPI JSON
- `http://localhost:7400/v3/api-docs`
- `http://localhost:7401/v3/api-docs`


Note: If a servlet context-path is configured, Swagger will be served under that path.

<a id="api-endpoints"></a>
## 🔗 API Endpoints

**Base path:** `/api/v1`

---

<a id="user-manual-sections"></a>
### 📂 User Manual Sections

| No | Method | Endpoint | Description |
|----|--------|----------|-------------|
| 1 | GET | `/usermanual-section?platformType=ADMIN\|CABINET` | Returns the full section tree (roots + recursive children) filtered by platformType |
| 2 | GET | `/usermanual-section/{id}` | Returns details for a section and its entire subtree |
| 3 | POST | `/usermanual-section` | Creates a section (Body: UserManualSectionDTO) |
| 4 | PUT | `/usermanual-section/{id}` | Updates a section (Body: UserManualSectionDTO) |
| 5 | DELETE | `/usermanual-section/{id}` | Deletes a section |

---

### 📄 UserManualSectionDTO (request)

```json
{
  "title": "Getting Started",
  "description": "Introduction and setup",
  "parentId": null,
  "platformType": "ADMIN"
}
```

Sample requests:

```bash
# Get tree for ADMIN
curl -X GET "http://localhost:7401/api/v1/usermanual-section?platformType=ADMIN"

# Create section
curl -X POST "http://localhost:7401/api/v1/usermanual-section" \
  -H "Content-Type: application/json" \
  -d '{
        "title": "Getting Started",
        "description": "Intro and setup",
        "parentId": null,
        "platformType": "ADMIN"
      }'

# Get section by id
curl -X GET "http://localhost:7401/api/v1/usermanual-section/1"
```

<a id="user-manual-versions"></a>
### 📘 User Manual Versions

| No | Method | Endpoint | Description |
|----|--------|----------|-------------|
| 1 | GET | `/usermanual-version?status={DRAFT\|PUBLISHED\|ARCHIVED}&sectionId={id}` | List versions (status optional, defaults to PUBLISHED) |
| 2 | GET | `/usermanual-version/{id}` | Get version by ID |
| 3 | POST | `/usermanual-version` | Create version (Body: UserManualVersionDTO) |
| 4 | PUT | `/usermanual-version/{id}` | Update version (Body: UserManualVersionDTO) |
| 5 | DELETE | `/usermanual-version/{id}` | Delete version (only allowed when status=DRAFT) |
| 6 | PATCH | `/usermanual-version/{id}?status={DRAFT\|PUBLISHED\|ARCHIVED}` | Change version status (Allowed: DRAFT→PUBLISHED, PUBLISHED→ARCHIVED, ARCHIVED→PUBLISHED) |

---

### 📄 UserManualVersionDTO (request)

```json
{
  "version_number": "v1.0",
  "section_id": 1
}

```

Sample requests:

```bash
# List published versions for section 1
curl -X GET "http://localhost:7401/api/v1/usermanual-version?sectionId=1"

# Create version
curl -X POST "http://localhost:7401/api/v1/usermanual-version" \
  -H "Content-Type: application/json" \
  -d '{
        "version_number": "v1.0",
        "section_id": 1
      }'

# Update status to PUBLISHED
curl -X PATCH "http://localhost:7401/api/v1/usermanual-version/5?status=PUBLISHED"
```

<a id="file-uploads"></a>
### 📁 File Uploads

| No | Method | Endpoint | Description |
|----|--------|----------|-------------|
| 1 | POST | `/usermanual-section/upload/file/base64` | Upload file using Base64 JSON body |
| 2 | POST | `/usermanual-section/upload/file` | Multipart upload (file + section_paths[] + version + language) |
| 3 | POST | `/usermanual-section/upload/file/common` | Multipart upload of multiple files (saved into `common/{filename}` and replaces if exists) |

---

### 📄 Base64 Request Body Example

```json
{
  "file_name": "manual.pdf",
  "section_paths": [
    "Getting Started",
    "Installation"
  ],
  "version": "v1.0",
  "language": "en",
  "content": "<BASE64_STRING>"
}
```

Multipart (structured path) example:

```bash
curl -X POST "http://localhost:7401/api/v1/usermanual-section/upload/file" \
  -H "Content-Type: multipart/form-data" \
  -F "file=@./manual.pdf" \
  -F "section_paths=Getting Started" \
  -F "section_paths=Installation" \
  -F "version=v1.0" \
  -F "language=en"
```

Multipart (common folder) example:

```bash
curl -X POST "http://localhost:7401/api/v1/usermanual-section/upload/file/common" \
  -H "Content-Type: multipart/form-data" \
  -F "files=@./image1.png" \
  -F "files=@./image2.jpg"
```

<a id="response-wrapper"></a>
## 📦Response Wrapper

All responses are wrapped in ResultData:

```json
{
  "successCode": 0,
  "isSuccess": true,
  "status": "SUCCESS",
  "result": {},
  "timeStamp": 1732627200000,
  "count": 1
}
```

Errors example:

```json
{
  "successCode": -1,
  "isSuccess": false,
  "status": "FAILED",
  "error": {
    "code": 400,
    "message": "title is required"
  },
  "timeStamp": 1732627200000
}
```

<a id="database_and_migrations"></a>
## 🗄 Database & Migrations

- Flyway migration scripts are located in:  
  `src/main/resources/db/migration/oracle`

### 📌 V1__init.sql creates:

- **Global sequence:** `usermanual_seq`
- **Tables:**
    - `user_manual_section`
    - `user_manual_version`
- **Constraints:**
    - Unique section title
    - Unique version per section: `(version_number, section_id)`
    - Foreign key relations between sections and versions

**Note:**  
If you introduce backward-incompatible changes, always create a **new** migration file (e.g., `V2__*.sql`, `V3__*.sql`).  
Do **not** modify existing migration files in production environments.

---

<a id="conventions"></a>
## 📏 Conventions

- **PlatformType values:**
    - `ADMIN`
    - `CABINET`

- **UserManualVersionStatus values:**
    - `DRAFT`
    - `PUBLISHED`
    - `ARCHIVED`

- **Sorting:**  
  Section trees are sorted by `id` in ascending order at each level.

- **Deletion Rules:**  
  A section cannot be deleted if:
    - it has child sections
    - it has associated versions



<a id="contact"></a>
## 📬Contact

- Owner: Davrbank
- Project Developer: Urunov Khamdamboy, Bobur