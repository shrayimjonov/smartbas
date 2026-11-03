<a href="./README.md">English version</a>

# 🧾 Сервис Korinapogasheniya (korinapogashenya-service)

Spring Boot микросервис в экосистеме SMART BAS. В репозитории представлены каркас сервиса, заглушки интеграций (Eureka, Vault, Config) и генерация Excel-отчетов.

## 🧭 Содержание
- [Обзор](#обзор)
- [Технологии](#технологии)
- [Документация API](#документация-api)
- [Структура проекта](#структура-проекта)
- [Начало работы](#начало-работы)
  - [Предварительные требования](#предварительные-требования)
  - [Клонирование и сборка](#клонирование-и-сборка)
  - [Конфигурация](#конфигурация)
  - [Запуск](#запуск)
  - [Docker](#docker)
- [Наблюдаемость](#наблюдаемость)
- [Заметки](#заметки)
- [Дорожная карта](#дорожная-карта)
- [Контакты](#контакты)

## Обзор
Сервис Korinapogasheniya предоставляет обработку данных и экспорт отчетов по погашениям в SMART BAS. Сервис подготовлен к регистрации в реестре сервисов и получению секретов из хранилища секретов.

## Технологии
| Компонент           | Технология / Версия           | Примечание                           |
|:--------------------|:------------------------------|:-------------------------------------|
| Язык                | Java 17                       | LTS                                   |
| Фреймворк           | Spring Boot 3.4.2             | Web + Actuator                        |
| Облако              | Spring Cloud 2024.0.0         | Config Client, Vault, Eureka, OpenFeign |
| Утилиты             | Apache POI 5.2.3              | Генерация Excel                       |
| Сборка              | Maven                          | Сборка проекта                        |
| Контейнеризация     | Docker                         | Образ приложения                      |
| Тестирование        | spring-boot-starter-test       | Юнит/интеграционные тесты             |
| backend server port | 7208                           | Порт по умолчанию                     |

![Java](https://img.shields.io/badge/Java-17-blue?logo=java)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.4.2-green?logo=spring)
![Maven](https://img.shields.io/badge/Maven-3.x-red?logo=apache-maven)
![Docker](https://img.shields.io/badge/Docker-24.x-blue?logo=docker)

## Документация API

Все API имеют префикс `/v1/reports`.

### 1. Получение всех данных с пагинацией
**Эндпоинт:** `POST /get/all/page`

Возвращает постраничный список данных отчета.

**Заголовки:**
- `username`: (Опционально) Имя пользователя, отправившего запрос.

**Тело запроса (`PogasheniyaFilter`):**
```json
{
  "repDate": "19.12.2025",
  "state": 1,
  "pageNumber": 1,
  "pageSize": 20
}
```

**Ответ:** `PageableIntegrationResponseDTO<PogasheniyaGetPageableResponse>`

---

### 2. Скачать Excel
**Эндпоинт:** `POST /download/excel`

Генерирует и скачивает Excel-отчет.

**Заголовки:**
- `username`: (Опционально) Имя пользователя, отправившего запрос.

**Тело запроса (`PogasheniyaFilter`):**
```json
{
  "repDate": "19.12.2025",
  "state": 1,
  "pageNumber": 1,
  "pageSize": 20
}
```

**Ответ:** `ResponseEntity<ByteArrayResource>` (Excel файл)

---

### 3. Скачать Excel Zip
**Эндпоинт:** `POST /download/excel/zip`

Генерирует и скачивает zip-архив с Excel страницами.

**Заголовки:**
- `username`: (Опционально) Имя пользователя, отправившего запрос.

**Тело запроса (`PogasheniyaFilter`):**
```json
{
  "repDate": "19.12.2025",
  "state": 1,
  "pageNumber": 1,
  "pageSize": 20
}
```

**Ответ:** `ResponseEntity<ByteArrayResource>` (ZIP файл)

## Структура проекта
- `src/main/java/uz/davrbank/korinapogashenyaservice/KorinapogashenyaServiceApplication.java` — точка входа приложения
- `src/main/java/uz/davrbank/korinapogashenyaservice/utils/StartProject.java` — баннер старта и логирование окружения
- `src/main/java/uz/davrbank/korinapogashenyaservice/aggregate/controller` — REST-контроллеры
- `src/main/java/uz/davrbank/korinapogashenyaservice/aggregate/service` — сервисы и генерация отчетов
- `src/main/java/uz/davrbank/korinapogashenyaservice/aggregate/integration` — интеграционный клиент хаба
- `src/main/resources/application.yml` — активация профиля
- `src/main/resources/application-test.yml` — параметры окружения (управляются внешне)
- `Dockerfile`, `docker-compose.yml` — артефакты контейнеризации

## Начало работы

### Предварительные требования
- Java 17+
- Maven 3.8+
- Docker (по желанию)

### Клонирование и сборка
```bash
git clone <this-repository-url>
cd korinapogasheniya

mvn clean install
```

### Конфигурация
- В документации умышленно не приводятся свойства и секреты, чтобы исключить утечки чувствительной информации.
- Используйте переменные окружения и/или корпоративное хранилище секретов (например, Vault) для конфигурации.
- За списком необходимых ключей и значений обратитесь к внутренней операционной документации.

### Запуск
Запуск через Maven (пример):
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=test
```

Запуск JAR (пример):
```bash
java -jar target/korinapogashenya-service-0.0.1-SNAPSHOT.jar --spring.profiles.active=test
```

### Docker
Сборка и запуск локально (примеры):
```bash
docker build -t korinapogashenya-service:local .
docker compose up -d
# или
docker-compose up -d
```

## Контакты
- Владелец: Davrbank
- Авторы: Команда интеграции (Шухратжон Райимжонов)
