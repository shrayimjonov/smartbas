# 📘 User Manual Service

Spring Boot-микросервис для управления иерархическими разделами пользовательской документации и версиями документов, включая хранение файлов в MinIO.  
Сервис предоставляет API для создания, обновления, удаления, получения дерева разделов, управления жизненным циклом версий (Draft / Published / Archived) и загрузки файлов.

---

## 🧭 Оглавление

- [Функциональность](#функциональность)
- [Технологии](#технологии)
- [Архитектура](#архитектура)
- [Быстрый старт](#быстрый-старт)
    - [Требования](#требования)
    - [Клонирование и сборка](#клонирование-и-сборка)
    - [Конфигурация](#конфигурация)
    - [Запуск](#запуск)
    - [Docker](#docker)
- [API Документация](#api-документация)
- [API Методы](#api-методы)
    - [Разделы документации](#разделы-документации)
    - [Версии документации](#версии-документации)
    - [Загрузка файлов](#загрузка-файлов)
- [Обёртка ответа](#обёртка-ответа)
- [База данных и миграции](#база-данных-и-миграции)
- [Конвенции](#конвенции)
- [Контакты](#контакты)

---

## 🔐 Функциональность

- Иерархические разделы документации (рекурсивная структура).
- Сегментация по платформам: `ADMIN`, `CABINET`.
- Управление жизненным циклом версий: `DRAFT`, `PUBLISHED`, `ARCHIVED`.
- Получение раздела с дочерними элементами.
- Загрузка файлов в MinIO:
    - Base64 загрузка по структурированному пути.
    - Multipart загрузка одного файла.
    - Multipart загрузка нескольких файлов в `common/` директорию.
- Единый формат ответа через `ResultData`.
- Управление схемой БД посредством Flyway.

---

## ⚙️ Технологии

| Компонент | Технология | Статус |
|----------|------------|--------|
| Backend | Java 17, Spring Boot 3.4.2 | ✅ |
| Port | 7201 | ✅ |
| Build Tool | Maven | ✅ |
| Database | Oracle | ✅ |
| Migration | Flyway | ✅ |
| API Docs | Swagger / OpenAPI | ✅ |
| Object Storage | MinIO | ✅ |
| Packaging | Docker | ✅ |
| IDE | IntelliJ IDEA | ✅ |
| Architecture | Microservice | ✅ |

![Java](https://img.shields.io/badge/Java-17-blue?logo=java)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.4.2-green?logo=spring)
![Maven](https://img.shields.io/badge/Maven-3.9.11-red?logo=apache-maven)
![Docker](https://img.shields.io/badge/Docker-24.0.5-blue?logo=docker)

---

## 🧱 Архитектура

- Многоуровневый подход: **controller → service → repository → database**.
- Основные сущности:
    - `UserManualSection`
    - `UserManualVersion`
- Хранение файлов в MinIO под структурированными ключами:
    - `section/version/language/filename`

---

## 🚀 Быстрый старт

### Требования

- Java 17+
- Maven 3.8+
- Oracle DB
- MinIO
- Docker (опционально)

---

### Клонирование и сборка

```bash
git clone <repository-url>
cd usermanual-service
mvn clean install

```
---

## ⚙️ Конфигурация
### Доступные Spring-профили

- test — профиль по умолчанию

- local — локальная разработка

## Основные файлы конфигурации

- src/main/resources/application-test.yml

- src/main/resources/application-local.yml

## Пример конфигурации (local):
```bash
server:
port: 7401

spring:
datasource:
url: jdbc:oracle:thin:@localhost:1521/FREEPDB1
username: <username>
password: <password>

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
## Запуск профиля

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

## Запуск JAR

```bash
java -jar target/usermanual-service.jar --spring.profiles.active=local
```

## 🐳 Docker

```bash
docker compose up -d
```

## 📚 API документация

### Swagger UI:
```bash
http://localhost:7400/swagger-ui/index.html
http://localhost:7401/swagger-ui/index.html
```
## OpenAPI JSON:
```bash
http://localhost:7400/v3/api-docs
http://localhost:7401/v3/api-docs
```

<a id="">API Методы</a>

## 🔗 API Методы
Базовый путь: <a id="">/api/v1</a>

| № | Метод  | Endpoint                                          | Описание                    |
| - | ------ | ------------------------------------------------- | --------------------------- |
| 1 | GET    | `/usermanual-section?platformType=ADMIN\|CABINET` | Получить дерево разделов    |
| 2 | GET    | `/usermanual-section/{id}`                        | Получить раздел + поддерево |
| 3 | POST   | `/usermanual-section`                             | Создать раздел              |
| 4 | PUT    | `/usermanual-section/{id}`                        | Обновить раздел             |
| 5 | DELETE | `/usermanual-section/{id}`                        | Удалить раздел              |

---
## 📄 UserManualSectionDTO

```bash
{
  "title": "Getting Started",
  "description": "Introduction and setup",
  "parentId": null,
  "platformType": "ADMIN"
}
```
---
## 📘 Версии документации

| № | Метод  | Endpoint                                                                 | Описание               |
| - | ------ | ------------------------------------------------------------------------ | ---------------------- |
| 1 | GET    | `/usermanual-version?status={DRAFT\|PUBLISHED\|ARCHIVED}&sectionId={id}` | Список версий          |
| 2 | GET    | `/usermanual-version/{id}`                                               | Получить версию        |
| 3 | POST   | `/usermanual-version`                                                    | Создать версию         |
| 4 | PUT    | `/usermanual-version/{id}`                                               | Обновить версию        |
| 5 | DELETE | `/usermanual-version/{id}`                                               | Удалить (только DRAFT) |
| 6 | PATCH  | `/usermanual-version/{id}?status=`                                       | Изменить статус версии |

---

## 📁 Загрузка файлов

| № | Метод | Endpoint                                 | Описание                        |
| - | ----- | ---------------------------------------- | ------------------------------- |
| 1 | POST  | `/usermanual-section/upload/file/base64` | Загрузка файла в формате Base64 |
| 2 | POST  | `/usermanual-section/upload/file`        | Multipart-загрузка              |
| 3 | POST  | `/usermanual-section/upload/file/common` | Загрузка нескольких файлов      |

---

## 📦 Обёртка ответа
```bash
{
  "successCode": 0,
  "isSuccess": true,
  "status": "SUCCESS",
  "result": {},
  "timeStamp": 1732627200000,
  "count": 1
}
```

---

### 🗄 База данных и миграции

### 📌V1__init.sql создаёт:**

- `user_manual_section`
- `user_manual_version`
- `usermanual_seq`

---

### 📏 Конвенции

**Типы платформ:**
- `ADMIN`
- `CABINET`

**Статусы версий:**
- `DRAFT`
- `PUBLISHED`
- `ARCHIVED`

**Сортировка:**
- По возрастанию значения `id` (ascending)

**Ограничения на удаление разделов:**
- Нельзя удалить раздел, если:
    - у него есть дочерние элементы
    - у него есть связанные версии

---
## 📬 Контакты

- Владелец: Davrbank

- Разработчики: Уринов Хамдамбой, Бобур