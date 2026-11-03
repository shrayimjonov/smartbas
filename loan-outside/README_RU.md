<a href="./README.md">English version</a>

# Сервис GetOutsideLoan (getoutsideloan-service)

Spring Boot микросервис в экосистеме SMART BAS. В репозитории содержится реализация сервиса для получения отчетных данных по внешним кредитам и выгрузки Excel, а также конфигурация Eureka, Vault, Config, Feign и MinIO.

## Содержание
- [Обзор](#обзор)
- [Технологии](#технологии)
- [Документация API](#документация-api)
- [Начало работы](#начало-работы)
  - [Предварительные требования](#предварительные-требования)
  - [Клонирование и сборка](#клонирование-и-сборка)
  - [Конфигурация](#конфигурация)
  - [Запуск](#запуск)
- [Наблюдаемость](#наблюдаемость)
- [Заметки](#заметки)
- [Контакты](#контакты)

## Обзор
Сервис GetOutsideLoan предоставляет получение постраничных данных и экспорт Excel-отчета по портфелю полученных кредитов в SMART BAS. Сервис подготовлен для работы с service discovery, централизованной конфигурацией, Vault-секретами и Feign-интеграцией с хабом.

## Технологии
| Компонент           | Технология / Версия           | Примечание                            |
|:--------------------|:------------------------------|:--------------------------------------|
| Язык                | Java 21                       | Целевая версия проекта                |
| Фреймворк           | Spring Boot 3.4.12            | Web + Validation + Actuator           |
| Облако              | Spring Cloud 2024.0.3         | Config Client, Vault, Eureka, OpenFeign |
| Утилиты             | Apache POI 5.2.3              | Генерация Excel                       |
| Документация API    | Springdoc OpenAPI 2.8.8       | Swagger UI                            |
| Object Storage      | MinIO Java SDK 8.4.3          | Подготовлена конфигурация             |
| Сборка              | Maven Wrapper                 | `mvnw` / `mvnw.cmd`                   |
| backend server port | 7211                          | Порт по умолчанию                     |

![Java](https://img.shields.io/badge/Java-21-blue?logo=java)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.4.12-green?logo=spring)
![Maven](https://img.shields.io/badge/Maven-3.x-red?logo=apache-maven)

## Документация API

Context path приложения: `/get/outside/loan/api`  
Префикс контроллера: `/v1/reports`  
Полный базовый путь: `/get/outside/loan/api/v1/reports`

Swagger UI доступен по пути `/get/outside/loan/api/swagger-ui.html`.

### 1. Получение всех данных с пагинацией
**Эндпоинт:** `POST /get/all/page`

Возвращает постраничный список данных отчета по внешним кредитам.

**Заголовки:**
- `username`: (Опционально) Имя пользователя, отправившего запрос.

**Тело запроса (`GetOutsideLoanFilter`):**
```json
{
  "startDate": "01.01.2026",
  "endDate": "31.01.2026",
  "branch": "01037",
  "state": 1,
  "pageNumber": 1,
  "pageSize": 20
}
```

**Правила валидации:**
- `startDate` и `endDate` обязательны и должны быть в формате `dd.MM.yyyy`.
- `endDate` не может быть раньше `startDate`.
- `state` обязателен и должен быть одним из `0`, `1`, `2`.
- `pageNumber` по умолчанию становится `1`, если не передан или некорректен.
- `pageSize` по умолчанию становится `20`, если не передан или некорректен.
- `pageSize` не должен превышать `200000`.

**Ответ:** `PageableIntegrationResponseDTO<GetOutsideLoanPageableResponse>`

---

### 2. Скачать Excel
**Эндпоинт:** `POST /download/excel`

Генерирует и скачивает Excel-отчет по портфелю полученных кредитов.

**Заголовки:**
- `username`: (Опционально) Имя пользователя, отправившего запрос.

**Тело запроса (`GetOutsideLoanFilter`):**
```json
{
  "startDate": "01.01.2026",
  "endDate": "31.01.2026",
  "branch": "01037",
  "state": 1,
  "pageNumber": 1,
  "pageSize": 20
}
```

**Заметки:**
- Для Excel-выгрузки сервис принудительно устанавливает `pageNumber=1` и `pageSize=200000`, затем получает все страницы из хаба перед генерацией файла.
- Если данные отсутствуют, сервис возвращает `404`.

**Ответ:** `ResponseEntity<ByteArrayResource>` (Excel файл)
## Начало работы

### Предварительные требования
- Java 21+
- Maven 3.8+

### Клонирование и сборка
```bash
git clone <this-repository-url>
cd GetoutsideloanService

./mvnw clean install
```

Для Windows:
```bash
mvnw.cmd clean install
```

### Конфигурация
- Рабочие профили разделены на `application-test.yml` и `application-prod.yml`.
- Чувствительные значения должны управляться через переменные окружения и/или Vault.
- Настройки Eureka, Feign и MinIO уже структурированы в profile-файлах.
- Профиль по умолчанию в этом репозитории: `test`.

### Запуск
Запуск через Maven:
```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=test
```

Для Windows:
```bash
mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=test
```

Запуск JAR:
```bash
java -jar target/GetoutsideloanService-0.0.1-SNAPSHOT.jar --spring.profiles.active=test
```

## Наблюдаемость
- Actuator web endpoints публикуют `health`, `info` и `metrics`.
- Проверка здоровья Vault отключена в profile-конфигурации.
- Для сервиса настроено логирование `uz.davrbank.getoutsideloanservice`.

## Заметки
- Сервис интегрируется с `getoutsideloanhub-service` через OpenFeign и Eureka.
- Excel-файлы генерируются программно через Apache POI без загрузки `.xlsx` шаблона во время выполнения.
- Конфигурация MinIO присутствует, но текущий бизнес-процесс не загружает сформированные отчеты в object storage.

## Контакты
- Владелец: Davrbank
- Авторы: Команда интеграции
