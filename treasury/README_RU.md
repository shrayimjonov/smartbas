# Сервис Bus-Treasury

> Микросервис для работы с валютными проводками (покупка/продажа), шаблонами, типами документов, МФО, платежами, назначениями и административными запросами.

- [Русский](#сервис-bus-treasury) / [English](./README.md)
- Swagger UI: `/swagger-ui/index.html`
- OpenAPI JSON: `/v3/api-docs`

---

## 1) О проекте

### Таблица конфигурации

<div>

| Компонент | Технология | Статус |
|:----------:|:-----------:|:------:|
| Backend | Java 23, Spring Boot 3.4.1 | ✅ |
| Database | JPA/Hibernate, PostgreSQL | ✅ |
| API Docs | Swagger (OpenAPI 3.x), Postman | ✅ |
| Build | Maven | ✅ |
| Runtime (Prod) | Linux, Docker/Kubernetes (ArgoCD) | ✅ |
| CI | GitLab CI (Preprod) | ✅ |
| CD | GitLab CI/CD + ArgoCD (Prod) | ✅ |
| Кэширование | Redis | ✅ |
| Безопасность | JWT / Auth | ✅ |
| Мониторинг | Portainer (127.0.0.1) | ✅ |
| Code Quality | SAST Scanner | ✅ |
| IDE / Tools | IntelliJ IDEA / VS Code | ✅ |
| Архитектура | Микросервис | ✅ |

</div>
---

## 2) Запуск локально

### Требования
- Java 23
- Maven 3.9+

---


## 3) Обзор API

> Базовый путь: **`/api/v1`**  
> Если используется gateway, путь становится `/bus/api/v1/...`.

| № | Раздел | Описание |
|:-:|:------:|:---------:|
| 1 | Операции с валютой | [Operation API](#operation-api) |
| 2 | Шаблоны | [Template API](#template-api) |
| 3 | Проводки | [Wiring API](#wiring-api) |
| 4 | Типы документов | [DocType API](#doctype-api) |
| 5 | МФО (филиалы) | [MFO API](#mfo-api) |
| 6 | Платежи | [Payment API](#payment-api) |
| 7 | Назначения | [Purpose API](#purpose-api) |

---

### 3.1 Operation API (Покупка/Продажа валют)

**Базовый путь:** `/api/v1`

| Метод | URL | Описание |
|:-----:|:----|:---------|
| POST | `/usd/buy` | Покупка USD |
| POST | `/usd/sell` | Продажа USD |
| POST | `/ruble/buy` | Покупка RUB |
| POST | `/ruble/sell` | Продажа RUB |
| POST | `/euro/buy` | Покупка EUR |
| POST | `/euro/sell` | Продажа EUR |
| GET | `/all/wiring?page=0&size=20` | Список всех отправленных проводок |
| GET | `/operations/latest?templateId={id}` | Последняя операция с проводками |

**Пример запроса:**
```json
{
  "SUMMA": "100000",
  "COURSE": "12300",
  "CREATED_BY": "tokhirjon"
}
```

**Пример ответа:**
```json
{
  "ERROR_CODE": "0",
  "ERROR_NOTE": "SUCCESS. Все остаточные проводки получали финальные статусы. PROVEDEN=[1,2,3,4,5,6,7,8,9]",
  "TRANS_ID": "*******, *******",
  "GROUP_ID": 0
}
```

---

### 3.2 Template API
**Базовый путь:** `/api/v1/template`

| Метод | URL | Описание |
|:-----:|:----|:---------|
| POST | `/create?currencyType=USD` | Создать шаблон |
| GET | `/get/{id}` | Получить шаблон по ID |
| GET | `/get/all` | Все шаблоны (DTO) |
| PUT | `/update/{id}` | Обновить шаблон |
| DELETE | `/{id}` | Удалить шаблон |
| GET | `/all` | Сырые записи (Entity) |
| GET | `/filter?currency=USD&operation=BUY` | Найти по валюте и операции |

---

### 3.3 Wiring API
**Базовый путь:** `/api/v1/wiring`

| Метод | URL | Описание |
|:-----:|:----|:---------|
| POST | `/create?wiringId={id}` | Создать проводку |
| GET | `/all` | Все проводки |
| GET | `/all/{templateId}` | Проводки по шаблону |
| GET | `/type/{wiringId}` | Проводка по ID |
| PUT | `/update/{id}` | Обновить проводку |
| DELETE | `/delete/{id}` | Удалить проводку |

---

### 3.4 DocType API
**Базовый путь:** `/api/v1/doctype`

| Метод | URL | Описание |
|:-----:|:----|:---------|
| POST | `/create` | Создать тип документа |
| GET | `/get/{id}` | Получить по ID |
| GET | `/get/all` | Все типы документов |
| PUT | `/update/{id}` | Обновить тип документа |
| DELETE | `/{id}` | Удалить тип документа |

---

### 3.5 MFO API
**Базовый путь:** `/api/v1/mfo`

| Метод | URL | Описание |
|:-----:|:----|:---------|
| POST | `/create` | Создать МФО |
| GET | `/get/{id}` | Получить по ID |
| GET | `/get/all` | Все МФО |

---

### 3.6 Payment API
**Базовый путь:** `/api/v1/payment`

| Метод | URL | Описание |
|:-----:|:----|:---------|
| POST | `/create` | Создать платеж |
| GET | `/get/{id}` | Получить по ID |
| GET | `/get/all` | Все платежи |
| DELETE | `/delete/by/id?id={id}` | Удалить по ID |

---

### 3.7 Purpose API
**Базовый путь:** `/api/v1/purpose`

| Метод | URL | Описание |
|:-----:|:----|:---------|
| POST | `/create` | Создать назначение |
| GET | `/get/all` | Все назначения |
| GET | `/get/{id}` | Получить по ID |
| PUT | `/update/{id}` | Обновить назначение |
| DELETE | `/delete/{id}` | Удалить назначение |

---

## 4) Обработка ошибок

- `ERROR_CODE = "0"` — операция успешна
- `ERROR_NOTE` — описание результата
- `STATUS` — состояние (`PROVEDEN`, `TIMEOUT` и т.д.)

---

## 5) Версионирование и соглашения

- Версия API: `/api/v1`
- Enum значения нечувствительны к регистру (`USD`, `EUR`, `RUB`, `BUY`, `SELL`)
- Формат дат: `yyyy-MM-dd HH:mm:ss`
- Пагинация: параметры `page`, `size`, `sort`

---

## 6) Примечания разработчика

- Все контроллеры аннотированы Swagger (`@Tag`, `@Operation`).
- `@PermitAll` используется только для открытых методов.
- `TemplateResolverService` автоматически выбирает шаблон по валюте и типу операции.

---
</div>
