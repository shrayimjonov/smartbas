<a href="./README.md">English</a> / <a href="./README_RU.md">Russian</a>

# 📘 Journal Transaction Сервис

Spring Boot микросервис, предоставляющий **журнальные транзакции банковских счетов**
для **аудита и отчетности**.  
Сервис выступает в роли интеграционного слоя между потребителями Smart BAS и
Oracle-пакетом `BAS_HUB_REPORT` через сервис `journaltranshub-service`.

---

## 🧭 Содержание

- [Функциональные возможности](#features)
- [Технологии](#technologies)
- [Архитектура](#architecture)
- [Начало работы](#getting-started)
  - [Требования](#prerequisites)
  - [Клонирование и сборка](#clone--build)
  - [Конфигурация](#configuration)
  - [Запуск](#run)
- [API документация](#api-documentation)
- [API Endpoints](#api-endpoints)
- [Формат ответа](#response-wrapper)
- [Контакты](#contact)

---

<a id="features"></a>
## 🔐 Функциональные возможности

- Получение журнальных транзакций банковских счетов
- Постраничная выдача данных для аудита
- Фильтрация по:
  - диапазону дат
  - коду балансового счета
  - филиалу
- Pageable / Paginated формат ответа
- Интеграция с внешним HUB-сервисом (Feign)
- Централизованная обработка ошибок
- Готовность к CI/CD и production-среде

---

<a id="technologies"></a>
#### ⚙️ Используемые технологии

<div>

---
| Компонент | Технология | Статус |
|---------:|-----------:|:------:|
| Backend | Java 17, Spring Boot 3.x | ✅ |
| Порт | 7206 | ✅ |
| Система сборки | Maven | ✅ |
| База данных | Oracle DB | ✅ |
| Внешняя интеграция | journaltranshub-service (Feign) | ✅ |
| API документация | Swagger / OpenAPI | ✅ |
| Service Discovery | Eureka | ✅ |
| Конфигурация | Vault | ✅ |
| Архитектура | Микросервис | ✅ |
| CI/CD | GitLab CI/CD | ✅ |

---

</div>

![Java](https://img.shields.io/badge/Java-17-blue?logo=java)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-green?logo=spring)
![Maven](https://img.shields.io/badge/Maven-3.9.11-red?logo=apache-maven)

---

<a id="architecture"></a>
## 🧱 Архитектура

- Слоистая архитектура:
- Сервис **не работает напрямую с базой данных**
- Все данные получаются из Oracle-пакета **BAS_HUB_REPORT**
- Взаимодействие с HUB-сервисом реализовано через **Spring Cloud OpenFeign**

---

<a id="getting-started"></a>
## 🚀 Начало работы

<a id="prerequisites"></a>
### Требования

- Java 17+
- Maven 3.8+
- Доступ к Eureka Server
- Доступ к Vault
- Сетевой доступ к HUB-сервису

---

<a id="clone--build"></a>
### Клонирование и сборка

```bash
# клонирование репозитория
git clone <repository-url>
cd journaltrans-service

# сборка
mvn clean install
```
<a id="configuration"></a>

## ⚙️ Конфигурация
### Сервер и Context Path
```bash
server:
  port: 7206
  servlet:
    context-path: /hub/api
```
### Имя сервиса
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
<a id="run"></a>

## ▶️ Запуск
### Запуск через Maven
```bash
mvn spring-boot:run
```
## Запуск JAR-файла
```bash
java -jar target/journaltrans-service.jar
```
### Сервис будет доступен по адресу:
```bash
http://<host>:7506/hub/api
```
<a id="api-documentation"></a>

## 📚 API документация

### Swagger UI и OpenAPI будут доступны после запуска сервиса.

### Примеры URL:

#### Swagger UI
```bash
http://<host>:7506/hub/api/swagger-ui/index.html
```
#### OpenAPI JSON
```bash
http://<host>:7506/hub/api/v3/api-docs
```
<a id="api-endpoints"></a>

## 🔗 API Endpoints
### 📄 Отчет по журнальным транзакциям (Postgre / Paginated)
| Метод | Endpoint                              |
| ----- | ------------------------------------- |
| POST  | `/hub/api/journal/trans/get/all/page` |

### Полный пример URL (test)
```bash
http://127.0.0.1:7506/hub/api/journal/trans/get/all/page
```
## 🔸 Параметры запроса
| Параметр    | Формат     | Обязательность |
| ----------- | ---------- | -------------- |
| iStartDate  | dd.MM.yyyy | optional       |
| iEndDate    | dd.MM.yyyy | optional       |
| iAccBalKod  | String     | optional       |
| iBranch     | String     | optional       |
| iPageNumber | Integer    | required (>=1) |
| iPageSize   | Integer    | required (>=1) |

## 🔸 Ответ (Успешный)
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

## 📦 Формат ответа

### Все ответы возвращаются в едином формате:
```bash
{
  "status": "success | error",
  "error": "",
  "data": []
}
```
<a id="contact"></a>

## 📬 Контакты

- Организация: Davr Bank – Smart BAS

- Разработчик сервиса: Bobur Boynazarov
