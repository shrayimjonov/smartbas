<a href = "https://internal.example.com/bas-group/bus/reportservices/zalogipoteka-service/-/blob/prod/README.md?ref_type=heads">
English</a>
/ <a href = "https://internal.example.com/bas-group/bus/reportservices/zalogipoteka-service/-/blob/prod/README_RU.md?ref_type=heads">Russian</a>

# 🏦 Сервис Залог Ипотека

---

## Это один из сервисов проекта SMART BAS, который получает все данные о Залог Ипотеке


## 🧱 Содержание

- [Особенности](#особенности)
- [Используемые технологии](#используемые-технологии)
- [Начало работы](#начало-работы)
    - [Предварительные требования](#предварительные-требования)
    - [Установка](#установка)
    - [ Документация](#документация)
    - [Запуск приложения](#запуск-приложения)
- [API Эндпоинты](#api-эндпоинты) (если применимо)
- [Контакты](#контакты)


## 🔐 Особенности

#### ⚙️Используемые технологии

- Список технологий, использованных для построения проекта:

<div>

|      Компоненты      |          Технология          | Статус |
|:--------------------:|:----------------------------:|:------:|
|       Backend        |    Java 17,  Spring Boot     |   ✅    |
|         Порт         |             7201             |   ✅    |
|   Документация API   | Swagger API + (Hub), POSTMAN |   ✅    |
|    Сборка сервера    |            Maven             |   ✅    |
|        Сервер        |     Linux Preprod()/Prod     |   ✅    |
|      Инструмент      |      IntelliJ Idea/VCS       |   ✅    |
|     Архитектура      |         Microservice         |   ✅    |
|          CI          |       gitlab CI (Prod)       |   ✅    |
|        CI/CD         |     gitlab CI/CD (Prod)      |   ✅    |
|   Контейнеризация    |      Docker (Test/Prod)      |   ✅    |
|     Безопасность     |     OAuth.2/KeyCloak/JWT     |   ✅    |
|      Мониторинг      |        Grafana(Prod)         |   ✅    |
|     Логирование      |         Kafka(Prod)          |   ✅    |
|     Кэширование      |            Redis             |   ✅    |
|         REST         |         FeignClient          |   ✅    |
|        MinIO         |       Хранение файлов        |   ✅    |
| Регистрация сервисов |            Eureka            |   ✅    |

</div>

![Java](https://img.shields.io/badge/Java-17-blue?logo=java)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.2.0-green?logo=spring)
![Maven](https://img.shields.io/badge/Maven-3.8.8-red?logo=apache-maven)
![Docker](https://img.shields.io/badge/Docker-24.0.5-blue?logo=docker)


## Начало работы

### Предварительные требования

Перед началом убедитесь, что у вас установлены следующие инструменты:

*   **Java Development Kit (JDK):** Версия 17 или выше.
    *   [Скачать JDK](https://www.oracle.com/java/technologies/downloads/)
*   **Maven:** Версия 3.6 или выше.
    *   [Скачать Maven](https://maven.apache.org/download.cgi)
*   **Git:** Для клонирования репозитория.
    *   [Скачать Git](https://git-scm.com/downloads)
*   **Docker & Docker Compose:** (Необязательно, если вы хотите использовать Docker для локального развертывания)
    *   [Скачать Docker Desktop](https://www.docker.com/products/docker-desktop)

### Установка

1.  **Клонируйте репозиторий:**
2. **git clone** https://internal.example.com/bas-group/bus/reportservices/zalogipoteka-service.git
3. **Соберите проект с помощью Maven** — эта команда загрузит все зависимости и соберёт JAR-файл.


### Документация

- **Swagger UI:** [http://localhost:7201/zalog/ipoteka/api/swagger-ui/index.html](http://localhost:7201/zalog/ipoteka/api/swagger-ui/index.html)
- **OpenAPI JSON:** [http://localhost:7201/zalog/ipoteka/api/v3/api-docs](http://localhost:7201/zalog/ipoteka/api/v3/api-docs)

### Запуск приложения

#### Вариант 1: Используя Maven (для разработки)

```
 * mvn spring-boot:run
```
#### Вариант 2: Запуск исполняемого JAR

После сборки проекта (`mvn clean install`) можно запустить сгенерированный JAR-файл:

#### Вариант 3: Используя Docker (рекомендуется для локальной разработки с дополнительными сервисами, например PostgreSQL)

1. **Соберите Docker-образ:**
   `docker build -t my-awesome-spring-app`
2. **Запустите с помощью Docker Compose:**  
   `docker-compose up -d`  
   → Это запустит Spring Boot приложение вместе с другими сервисами (например, PostgreSQL).


## API Эндпоинты

Приложение предоставляет следующие RESTful эндпоинты:

## Получить все данные о залоге

| № |        Список API         |
|:--:|:------------------------:|
| 1  | [Эндпоинты](#эндпоинты) |

<details open>
<summary><b>Содержание API</b></summary>
<a id="API contents" ></a>

<details open>
<summary><b>Эндпоинты</b> (нажмите, чтобы развернуть или скрыть)</summary>
<a id="эндпоинты" ></a>

| No |                   Api name                   |                          url                           |
|:--:|:--------------------------------------------:|:------------------------------------------------------:|
| 1  |              GET All ZALOG                   |        [/zalog/ipoteka/api/v1/reports/get/all]         |
| 2  |         GET ALL ZALOG INFO PAGEABLE          |      [/zalog/ipoteka/api/v1/reports/pagination]        |
| 3  |    GET ALL ZALOG INFO PAGEABLE AND SORTED    | [/zalog/ipoteka/api/v1/reports/pagination/sort]        |
| 4  |                DOWNLOAD EXCEL                |     [/zalog/ipoteka/api/v1/reports/download/excel]     | (#localhost-7201-api-get-all-download-report)                             |

</details>

---

### 🔹 GET Все ЗАЛОГИ

<details open>
<!--GET Все ЗАЛОГИ по cadastreType и state  -->
<summary><b>ПОЛУЧИТЬ ВСЕ ЗАЛОГИ</b> (нажмите, чтобы развернуть или скрыть)</summary>
<a id="localhost-7201-api-get-all-zalog"></a>

#### http://localhost:7201/zalog/ipoteka/api/v1/reports/get/all?

<b>Метод: POST</b>


<details open>
<summary><b>ПАРАМЕТРЫ ЗАПРОСА</b></summary>

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
<summary><b>УСПЕШНЫЙ ОТВЕТ</b></summary>

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
<summary><b>ОШИБКА ОТВЕТА</b></summary>
</details>
</details>
</details>
<!--КОНЕЦ ПОЛУЧЕНИЯ ВСЕХ ЗАЛОГОВ-->-->

### 🔹 ПОЛУЧИТЬ ВСЮ ИНФОРМАЦИЮ О ЗАЛОГАХ (ПОСТРАНИЧНО)

<!--ПОЛУЧИТЬ ВСЮ ИНФОРМАЦИЮ О ЗАЛОГАХ (ПОСТРАНИЧНО)-->
<details open>
<summary><b>ПОЛУЧИТЬ ВСЮ ИНФОРМАЦИЮ О ЗАЛОГАХ (ПОСТРАНИЧНО)</b> (нажмите, чтобы развернуть или скрыть)</summary>
<a id="localhost-7201-api-get-all-pageable"></a>

#### http://localhost:7201/zalog/ipoteka/api/v1/reports/pagination?

<b>Метод: POST</b>
<details open>

<summary><b>ПАРАМЕТРЫ ЗАПРОСА</b></summary>


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
<summary><b>УСПЕШНЫЙ ОТВЕТ</b></summary>

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
<summary><b>ОШИБКА ОТВЕТА</b></summary>
</details>
</details>
<!--КОНЕЦ ПОЛУЧЕНИЯ ВСЕЙ ИНФОРМАЦИИ О ЗАЛОГАХ (ПОСТРАНИЧНО)-->


### 🔹 ПОЛУЧИТЬ ВСЮ ИНФОРМАЦИЮ О ЗАЛОГАХ (ПОСТРАНИЧНО С СОРТИРОВКОЙ)

<!--ПОЛУЧИТЬ ВСЮ ИНФОРМАЦИЮ О ЗАЛОГАХ (ПОСТРАНИЧНО С СОРТИРОВКОЙ)-->
<details open>
<summary><b>ПОЛУЧИТЬ ВСЮ ИНФОРМАЦИЮ О ЗАЛОГАХ (ПОСТРАНИЧНО С СОРТИРОВКОЙ)</b> (нажмите, чтобы развернуть или скрыть)</summary>
<a id="localhost-7201-api-get-all-pageable-sorted"></a>

#### http://localhost:7201/zalog/ipoteka/api/v1/reports/pagination?

<b>Метод: POST</b>

<details open>
<summary><b>ПАРАМЕТРЫ ЗАПРОСА</b></summary>

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
<summary><b>УСПЕШНЫЙ ОТВЕТ</b></summary>

```json

{
  "status": "success",
  "totalElements": 348,
  "error": "",
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
<summary><b>ОШИБКА ОТВЕТА</b></summary>
</details>
</details>
<!--КОНЕЦ ПОЛУЧЕНИЯ ВСЕЙ ИНФОРМАЦИИ О ЗАЛОГАХ (ПОСТРАНИЧНО)-->


### 🔹 ПОЛУЧИТЬ ОТЧЁТ В ФОРМАТЕ EXCEL
<!--ПОЛУЧИТЬ ОТЧЁТ В ФОРМАТЕ EXCEL-->
<details open>
<summary><b>ПОЛУЧИТЬ ОТЧЁТ В ФОРМАТЕ EXCEL</b> (нажмите, чтобы развернуть или скрыть)</summary>
<a id="localhost-7201-api-get-all-download-report"></a>

#### http://localhost:7201/zalog/ipoteka/api/v1/reports/download-report?

<b>Метод: GET</b>

<details open>
<summary><b>ПАРАМЕТРЫ ЗАПРОСА</b></summary>

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
<summary><b>УСПЕШНЫЙ ОТВЕТ</b></summary>
возвращает бинарные данные (содержимое XML и ZIP-файла), которые можно скачать как Excel-файл  
**ZalogIpoteka_Report_Type_1_State_2.xlsx**
</details>
</details>
<!--КОНЕЦ ЗАГРУЗКИ ОТЧЁТА-->

## Контакты

Если у вас есть вопросы или предложения, не стесняйтесь создать issue или связаться со мной:

*   **Гулиза Тулкинова:** [user@example.com](mailto:user@example.com)
*   **Ответственный за проект:** [GitHub аккаунт](https://github.com/guliza0707)









