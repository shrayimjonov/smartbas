# 📘 Сервис Remittance Outside

Spring Boot микросервис для интеграции с платформой денежных переводов Leskar. Предоставляет REST‑API для получения стран и тарифов, создания клиента, создания/подтверждения/отмены перевода, получения статуса и выплаты. Для хранения токенов используется Redis, для журналирования — ClickHouse (через Flyway), для стриминга логов — Kafka.

## 🧭 Содержание

- [Возможности](#features-ru)
- [Технологии](#technologies-ru)
- [Архитектура](#architecture)
- [Быстрый старт](#getting-started-ru)
  - [Требования](#prerequisites-ru)
  - [Клонирование и сборка](#clone--build-ru)
  - [Конфигурация](#configuration-ru)
  - [Запуск](#gs-run-ru)
  - [Docker](#gs-docker-ru)
- [API Документация](#api-documentation-ru)
- [Эндпоинты](#api-endpoints-ru)
- [Обёртка ответа](#response-wrapper-ru)
- [База данных и миграции](#database_and_migrations-ru)
- [Логирование и мониторинг](#logging--monitoring-ru)
- [Решение проблем](#troubleshooting-ru)

<a id="features-ru"></a>
## 🔐 Возможности

- Интеграция с платформой Leskar
- Жизненный цикл перевода: создание, подтверждение, отмена, выплата, статус, информация
- Эндпоинты для получения стран и тарифов
- Хранение и авто‑обновление токенов в Redis
- Журналирование в ClickHouse, миграции через Flyway
- Стриминг логов в Kafka; маскирование чувствительных полей
- OpenAPI/Swagger UI

<a id="technologies-ru"></a>
## 🔧 Технологии

- Java 17, Spring Boot 3.4.11
- Maven
- Redis, Kafka, ClickHouse
- Flyway (ClickHouse), WebFlux WebClient, Lombok
- Spring Cloud Vault (опционально для секретов)
- Порт по умолчанию: 7101

<a id="architecture"></a>
## 🧱 Architecture

Секция Architecture должна оставаться без изменений. Ниже размещена исходная версия из основного README:

```
┌─────────────────────────────────────────────────────────────┐
│                    Client Applications                       │
└─────────────────────────────────────────────────────────────┘
                              ↓
┌─────────────────────────────────────────────────────────────┐
│                   REST API Controller                        │
│              (LeskarController - @RestController)            │
└─────────────────────────────────────────────────────────────┘
                              ↓
┌─────────────────────────────────────────────────────────────┐
│                  Business Logic Layer                        │
│    (LeskarService, TokenStoreService, IntegrationService)   │
└─────────────────────────────────────────────────────────────┘
                              ↓
┌─────────────────────────────────────────────────────────────┐
│               Integration & External Services                │
│  (LeskarIntegrationClient, Connector, WebClient)            │
└─────────────────────────────────────────────────────────────┘
        ↓                      ↓                      ↓
    ┌──────────┐         ┌─────────┐         ┌──────────────┐
    │ Leskar   │         │ Redis   │         │ ClickHouse   │
    │ API      │         │ (Tokens)│         │ (Logging)    │
    └──────────┘         └─────────┘         └──────────────┘
                              ↓
                         ┌─────────┐
                         │ Kafka   │
                         │ (Events)│
                         └─────────┘
```

### Ключевые компоненты

- Контроллер: `endpoint/LeskarController.java` — все HTTP‑операции перевода; базовый путь `/api/v1/integration/leskar/transfer`; ответы оборачиваются в `ResultData`.
- Сервисы: `LeskarService`, `IntegrationService`, `TokenStoreService`.
- Интеграция: `LeskarIntegrationClient` (AES‑256 шифрование, HMAC‑SHA256 подписи, автообновление токена), `IntegrationRouter`, `Connector` (WebClient обёртка).
- Модели: DTO в `aggregate/dto/leskarintegration/`, `ResultData`, `ApiExchangeLog`.
- Репозитории: `TokenStoreRepository` (Redis), `ClickhouseRepository` (JDBC).
- Логирование и AOP: `ApiExchangeLogger`, `RestControllerLoggingFilter`, `WebclientLoggingFilter` (маскирование чувствительных полей, усечение больших payload’ов).
- Конфигурация: `ApplicationProperties`, `WebClientConfiguration`, `ClickhouseConfiguration`, `KafkaConfig`, `RedisConfigure`, `OpenAPIConfiguration`.

<a id="getting-started-ru"></a>
## 🚀 Быстрый старт

<a id="prerequisites-ru"></a>
### Требования

- Java 17+
- Maven 3.8+
- Доступные инстансы Redis, Kafka, ClickHouse
- (Опционально) Vault для секретов

<a id="clone--build-ru"></a>
### Клонирование и сборка

```bash
git clone <repository-url>
cd remittance-outside
mvn clean install -DskipTests
```

<a id="configuration-ru"></a>
### Конфигурация

Конфигурация передаётся через переменные окружения и/или Vault. Чувствительные ключи (логины, пароли, токены, URL) намеренно исключены из README. Обратитесь к DevOps или защищённому хранилищу конфигураций. Порт по умолчанию: `7101`.

<a id="gs-run-ru"></a>
### Запуск

Через Maven:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=test
```

JAR:

```bash
java -jar target/remittanceoutside-0.0.1-SNAPSHOT.jar --spring.profiles.active=test
```

<a id="gs-docker-ru"></a>
### Docker

`Dockerfile` — multi‑stage сборка.

```bash
# Сборка образа
docker build -t remittance-outside:local .

# Запуск контейнера
docker run --rm -p 7101:7101 --name remittance-outside \
  -e SPRING_PROFILES_ACTIVE=test \
  remittance-outside:local
```

`docker-compose.yml` публикует порт `7101:7101` для сервиса `api`. Перед запуском задайте поле `image` (например, `remittance-outside:local`).

<a id="api-documentation-ru"></a>
## API Документация

- Swagger UI: http://localhost:7101/swagger-ui/index.html
- OpenAPI JSON: http://localhost:7101/v3/api-docs

<a id="api-endpoints-ru"></a>
## Эндпоинты

Базовый путь: `/api/v1/integration/leskar/transfer` (все `POST`):

- `/countries` — список стран
- `/fees` — список тарифов (`LeskarGetFeeRequest`)
- `/person/create` — создание клиента (`LeskarCreatePersonRequest`)
- `/create` — создание перевода (`LeskarCreateTransferRequest`)
- `/confirm` — подтверждение перевода (`LeskarConfirmTransferRequest`)
- `/cancel` — отмена перевода (`LeskarCancelTransferRequest`)
- `/person/info` — информация о клиенте (`LeskarPersonInfoRequest`)
- `/payout` — инициировать выплату (`LeskarPayoutRequest`)
- `/payout/confirm` — подтверждение выплаты (`LeskarPayoutConfirmRequest`)
- `/status` — статус перевода (`LeskarTransferStatusRequest`)
- `/info` — информация о переводе (`LeskarTransferInfoRequest`)

<a id="response-wrapper-ru"></a>
## Обёртка ответа

Все ответы обёрнуты в `ResultData<T>` с полями `isSuccess`, `status`, `result`, `error`, `timeStamp` (см. `aggregate/model/ResultData.java`).

<a id="database_and_migrations-ru"></a>
## База данных и миграции

- JDBC ClickHouse настраивается через переменные окружения (см. раздел «Конфигурация»).
- Flyway включает миграции из `classpath:db/clickhouse/migration` (например, `V1__create_kafka_table.sql`).

Пример таблицы из миграции:

```sql
CREATE TABLE IF NOT EXISTS remittance_outside.remittance_api_exchange_log
(
    event_id        String,
    event_type      String,
    client_ip       Nullable(String),
    event_date      DateTime,
    url             String,
    http_method     String,
    event_header    Nullable(String),
    event_json      Nullable(String),
    additionals     Nullable(String),
    response_status Nullable(String),
    response_time   Nullable(Int64),
    exchange_type   String
)
ENGINE = ReplacingMergeTree(event_date)
PARTITION BY toDate(event_date)
ORDER BY (event_id, event_type)
SETTINGS index_granularity = 8192;
```

<a id="logging--monitoring-ru"></a>
## Логирование и мониторинг

- AOP перехватчики журналируют входящие/исходящие обмены.
- Чувствительные ключи маскируются согласно настройкам в защищённой конфигурации.
- Пакетная запись в ClickHouse реализована в `ClickhouseRepositoryImpl`.

<a id="troubleshooting-ru"></a>
## Решение проблем

- Swagger UI (404): проверьте, что приложение запущено и порт 7101 открыт.
- Подключения Redis/Kafka/ClickHouse: проверьте хост/порт/учётные данные и сетевую доступность.
- Ошибки миграций ClickHouse: проверьте настройки `spring.flyway.*` и креды.
- Топик Kafka отсутствует: `KafkaConfig` создаёт топик на основании `app.kafka.topic.*`.

## Лицензия

Приватный проект. Все права защищены.
