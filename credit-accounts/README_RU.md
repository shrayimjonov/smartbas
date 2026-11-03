<a href = "https://internal.example.com/bas-group/bus/reportservices/creditaccounts-service/-/blob/test/README.md?ref_type=heads">
English</a>
/ <a href = "https://internal.example.com/bas-group/bus/reportservices/creditaccounts-service/-/blob/test/README_RU.md?ref_type=heads">Russian</a>

# 🏦 Сервис Кредитных Счетов

---

## Это один из сервисов проекта SMART BAS, который получает все данные о кредитных счетах и генерирует Excel-файл.

## 🧱 Содержание

* [Функции](#функции)
* [Используемые технологии](#используемые-технологии)
* [Начало работы](#начало-работы)

    * [Требования](#требования)
    * [Установка](#установка)
    * [Запуск приложения](#запуск-приложения)
* [API Endpoints](#api-endpoints)
* [Контакты](#контакты)

## 🔐 Функции

#### ⚙️ Используемые технологии

* Список технологий, используемых в проекте

<div>

---

|          Компоненты         |             Технологии            | Статус |
| :-------------------------: | :-------------------------------: | :----: |
|           Backend           |     Java 17, Spring Boot 3.4.2    |    ✅   |
|             Порт            |                7205               |    ✅   |
|         База данных         |             Oracle DB             |    ✅   |
|       Документация API      |     Swagger, JavaDoc, Postman     |    ✅   |
|            Сборка           |               Maven               |    ✅   |
|          OS сервера         |               Linux               |    ✅   |
| Инструмент тестирования API |         Postman / Insomnia        |    ✅   |
|         IDE/Редактор        |      IntelliJ IDEA / VS Code      |    ✅   |
|         Архитектура         |            Микросервис            |    ✅   |
|              CI             |          GitLab CI (Prod)         |    ✅   |
|            CI/CD            | GitLab CI/CD, ArgoCD (Production) |    ✅   |
|        Инфраструктура       |  Docker (Test)/Kubernetes (Prod)  |    ✅   |
|          Чистый код         |            SAST Scanner           |    ✅   |
|         Кэширование         |               Redis               |    ✅   |
|         Безопасность        |      OAuth 2.0, Keycloak, JWT     |    ✅   |
|              UI             |          Angular / React          |    ✅   |
|          Мониторинг         |             Portainer             |    ✅   |

---

</div>

![Java](https://img.shields.io/badge/Java-17-blue?logo=java)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.2.0-green?logo=spring)
![Maven](https://img.shields.io/badge/Maven-3.8.8-red?logo=apache-maven)
![Docker](https://img.shields.io/badge/Docker-24.0.5-blue?logo=docker)

## Начало работы

### Требования

Перед началом убедитесь, что у вас установлено:

* **Java Development Kit (JDK):** версия 17 или выше

    * [Скачать JDK](https://www.oracle.com/java/technologies/downloads/)
* **Maven:** версия 3.6 или выше

    * [Скачать Maven](https://maven.apache.org/download.cgi)
* **Git:** для клонирования репозитория

    * [Скачать Git](https://git-scm.com/downloads)
* **Docker & Docker Compose:** (опционально)

    * [Скачать Docker Desktop](https://www.docker.com/products/docker-desktop)

### Установка

1. **Клонируйте репозиторий:**

```bash
git clone https://internal.example.com/bas-group/bus/reportservices/creditaccounts-service.git
```

2. **Соберите проект с помощью Maven** – эта команда загрузит все зависимости и соберет JAR-файл.

### Запуск приложения

#### Вариант 1: Использование Maven (для разработки)

```bash
mvn spring-boot:run
```

#### Вариант 2: Запуск скомпилированного JAR

После сборки проекта (`mvn clean install`) можно запустить сгенерированный JAR.

#### Вариант 3: Использование Docker (рекомендуется для локальной разработки с сервисами типа PostgreSQL)

1. **Собрать Docker-образ:**

```bash
docker build -t my-awesome-spring-app
```

2. **Запустить с помощью Docker Compose:**

```bash
docker-compose up -d
```

Это запустит Spring Boot приложение вместе с любыми конфигурированными сервисами.

## API Endpoints

Приложение предоставляет следующие RESTful endpoints:

## Получить всю информацию о кредитных счетах

|  №  |        Список API       |
| :-: | :---------------------: |
|  1  | [Endpoints](#Endpoints) |

<details open>
<summary><b>Содержание API</b></summary>
<a id="API contents"></a>

<details open>
<summary><b>Endpoints</b> (клик для раскрытия/скрытия)</summary>
<a id="Endpoints"></a>

| No |                Api name                |                               url                                |
|:--:|:--------------------------------------:|:----------------------------------------------------------------:|
| 1  |         GET All CREDIT ACCOUNT         |            [/credit/accounts/api/v1/reports/get/all ]            |
| 2  |      GET All CREDIT ACCOUNT INFO PAGEABLE       |     [  /credit/accounts/api/v1/reports/get/all/pageable   ]      |
| 3  | GET All CREDIT ACCOUNT INFO PAGEABLE AND SORTED | [ /credit/accounts/api/v1/reports/get/all/pageable/sorted?iDate] |
| 4  |             DOWNLOAD EXCEL             |         [/credit/accounts/api/v1/reports/download/excel]         |

</details>

<details open>
<summary><b>GET All CREDIT ACCOUNTS</b> (клик для раскрытия/скрытия)</summary>
<a id="localhost-7205-api-get-all-credit-account"></a>

#### [http://localhost:7205/credit/accounts/api/v1/reports/get/all](http://localhost:7205/credit/accounts/api/v1/reports/get/all)?

<b>Метод: POST</b>

<details open>
<summary><b>Параметры запроса</b></summary>

```http
date
branch
state
valType
account
```

</details>

<details open>
<summary><b>Успешный ответ</b></summary>

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
<summary><b>Ошибка ответа</b></summary>
</details>
</details>
</details>
<!--ПОЛУЧЕНИЕ ВСЕХ КРЕДИТНЫХ СЧЕТОВ С ПАГИНАЦИЕЙ -->
<details open>
<summary><b>ПОЛУЧИТЬ ВСЕ КРЕДИТНЫЕ СЧЕТА (С ПАГИНАЦИЕЙ)</b> (нажмите, чтобы развернуть или скрыть)</summary>
<a id="localhost-7205-api-get-all-pageable"></a>

#### http://localhost:7205/credit/accounts/api/v1/reports/get/all/pageable?

<b>Метод: POST</b>
<details open>

<summary><b>ПАРАМЕТРЫ ЗАПРОСА</b></summary>


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
<summary><b>ОШИБКА ОТВЕТА</b></summary>
</details>
</details>
<!--КОНЕЦ ПОЛУЧЕНИЯ ВСЕХ КРЕДИТНЫХ СЧЕТОВ С ПАГИНАЦИЕЙ-->


<!--ПОЛУЧЕНИЕ ВСЕХ КРЕДИТНЫХ СЧЕТОВ С ПАГИНАЦИЕЙ И СОРТИРОВКОЙ-->
<details open>
<summary><b>ПОЛУЧИТЬ ВСЕ КРЕДИТНЫЕ СЧЕТА (С ПАГИНАЦИЕЙ И СОРТИРОВКОЙ)</b> (нажмите, чтобы развернуть или скрыть)</summary>
<a id="localhost-7205-api-get-all-pageable-sorted"></a>

#### http://localhost:7205/credit/accounts/api/v1/reports/get/all/pageable/sorted?

<b>Метод: POST</b>

<details open>
<summary><b>ПАРАМЕТРЫ ЗАПРОСА</b></summary>

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
<summary><b>ОШИБКА ОТВЕТА</b></summary>
</details>
</details>
<!--КОНЕЦ ПОЛУЧЕНИЯ ВСЕХ КРЕДИТНЫХ СЧЕТОВ С ПАГИНАЦИЕЙ И СОРТИРОВКОЙ-->

<!--СКАЧАТЬ EXCEL-ОТЧЁТ-->
<details open>
<summary><b>СКАЧАТЬ EXCEL-ОТЧЁТ</b> (нажмите, чтобы развернуть или скрыть)</summary>
<a id="localhost-7205-api-get-all-download-report"></a>

#### http://localhost:7205/credit/accounts/api/v1/reports/download/excel?

<b>Метод: GET</b>

<details open>
<summary><b>ПАРАМЕТРЫ ЗАПРОСА</b></summary>


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
<summary><b>УСПЕШНЫЙ ОТВЕТ</b></summary>
Возвращается бинарный поток (содержимое Excel-файла).  
Файл может быть загружен со следующими именами:  
- `Credit_accounts_(%s).xlsx` — для указанного филиала  
- `Credit_accounts_(All_branches).xlsx` — для всех филиалов
</details>
</details>
<!--КОНЕЦ СКАЧИВАНИЯ ОТЧЁТА-->





## Контакты

Если у вас есть вопросы или предложения, вы можете открыть issue или связаться с:

* **Guliza Tulkinova:** [user@example.com](mailto:user@example.com)
* **Разработчик проекта:** [GitHub](https://github.com/guliza0707)
