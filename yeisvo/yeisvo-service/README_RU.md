<a href = "https://internal.example.com/bas-group/bus/reportservices/yeisvo-service/-/blob/prod/README.md?ref_type=heads">English</a>
/ <a href = "https://internal.example.com/bas-group/bus/reportservices/yeisvo-service/-/blob/prod/README_RU.md?ref_type=heads">
Russia</a>

### BAS Core  Service

***1. About project***
Этот проект построен с использованием микросервисной архитектуры.

Yeisvo Service взаимодействует с сервисом Yeisvo Hub и сервисом Auth Control через WebClient и REST.
Он выполняет роль интеграционного слоя, который определяет и управляет конечными точками (endpoints) сервиса Yeisvo.

Yeisvo Hub Service отвечает за работу с базой данных и взаимодействует с внешними системами, такими как Yeisvo и NCI.
Его основные задачи включают получение данных из внешних сервисов, их обработку и сохранение результатов в базу данных.
#### Таблица конфигурации

- Список технологий, используемых в проекте

<div>

|    Components     |          Technology          | Status |
|:-----------------:|:----------------------------:|:------:|
|      Backend      | Java 17,  Spring Boot 3.4.2  |   ✅    |
|     Database      |  JPA-Hibernate, Postgresql   |   ✅    |
| API Documentation | Swagger API + (Hub), POSTMAN |   ✅    |
|   Server Build    |            Maven             |   ✅    |
| API testing tool  |           POSTMAN            |   ✅    |
|       Tool        |        IntelliJ Idea         |   ✅    |
|   Architecture    |         Microservice         |   ✅    |
|       PORT        |             7100             |   ✅    |
|       CI/CD       |  gitlab CI/CD argocd(Prod)   |   ✅    |
|       Infro       |  Docker (Preprod)/K8S(Prod)  |   ✅    |
|    Clean Code     |      SAST scanner(**/5)      |   ✅    |            
|     Security      |     OAuth.2/KeyCloak/JWT     |   ✅    |
|        UI         |        Not Mentioned         |   ✅    |
|    Monitoring     |          Portainer           |   ✅    |

</div>

***2.  Структура проекта***

***3. About api***

| No |           Api lists           |
|:--:|:-----------------------------:|
| 1  | [contract-api](#contract-api) |

<details open>
<summary><b>Содержание API</b></summary>
<!--Yeisvo Service-->
<details open>
<summary><b>Yeisvo Service</b>(click to expand or hide)</summary>

<a id="mibcreditor-api" ></a>

### Сервис Контрактов

| No |                API name                |                                                   URL                                                    |
|:--:|:--------------------------------------:|:--------------------------------------------------------------------------------------------------------:|
| 1  |       ПОЛУЧИТЬ СТАТУС КОНТРАКТА        |              [localhost:7100/api/v1/contract/status](#localhost-7100/api/v1/contract/status)              |
| 2  |      ОБНОВЛЕНИЕ ДЕТАЛЕЙ КОНТРАКТА      |     [localhost:7100/api/v1/contract/update/details](#localhost-7100/api/v1/contract/update/details)      |
| 3  | ПОЛУЧИТЬ ДОКУМЕНТЫ ВЛАДЕЛЬЦА КОНТРАКТА |         [localhost:7100/api/v1/contract/owner/docs](#localhost-7100/api/v1/contract/owner/docs)          |
| 2  |       ПОЛУЧИТЬ ИСТОРИЮ КОНТРАКТА       |            [localhost:7100/api/v1/contract/history](#localhost-7100/api/v1/contract/history)             |
| 3  |      ПОЛУЧИТЬ КОНТРАКТ ПО ДЕТАЛЯМ      |                [localhost:7100/api/v1/contract/get](#localhost-7100/api/v1/contract/get)                 |
| 3  |     ПОЛУЧИТЬ БАНКОВСКИЕ ДОКУМЕНТЫS     |          [localhost:7100/api/v1/contract/bank/docs](#localhost-7100/api/v1/contract/bank/docs)           |
| 2  | ПОЛУЧИТЬ СПИСОК КОНТРАКТОВ (с обновл.) |            [localhost:7100/api/v1/contract/list](#localhost-7100/api/v1/core/contracts/list)             | 
| 3  |  ПОЛУЧИТЬ СПИСОК ИНН (Юр. Клиенты)     | [localhost:7100/api/v1/core/contr/contract/get-inn-list](#localhost-7100/api/v1/contract/get-inn-list)                                            |

<details open>
<summary><b>ПОЛУЧИТЬ СТАТУС КОНТРАКТА</b> (нажмите, чтобы открыть или скрыть)</summary>


<a id="localhos-7100-api-v1-contract-status"></a>

#### localhost-7100/api/v1/contract/status

<b>Method name:POST</b>

<details open>
<summary><b>ЗАПРОС</b></summary>

```json
{
  "bankInn": "*********",
  "bankId": "*****",
  "idn": "***********************"
}

```

</details>
<details open>
<summary><b>ОТВЕТ</b></summary>

```json
{
  "status": 0,
  "errorMsg": "OK",
  "timeStamp": "****-**-**T**:**:**.***Z",
  "gtkId": 0,
  "cntrStatus": 0
}

```

</details>
</details>
<!--END ПОЛУЧИТЬ СТАТУС КОНТРАКТА -->


<!--GET CONTRACT OWNER DOCS-->
<details open>
<summary><b>ПОЛУЧИТЬ ДОКУМЕНТЫ ВЛАДЕЛЬЦА КОНТРАКТА</b> (нажмите, чтобы открыть или скрыть)</summary>

<a id="localhost-7100-api-v1-core-contr-owner-docs"></a>


#### localhost-7100/api/v1/contract/owner-docs

<b>Method name: POST</b>

<details open>
<summary><b>ЗАПРОС</b></summary>

```
{
  "bankInn": "*********",
  "bankId": "*****",
  "idn": "***********************"
}

```

</details>
<details open>
<summary><b>ОТВЕТ</b></summary>

```json
{
  "status": 0,
  "errorMsg": "OK",
  "timeStamp": "****-**-**T**:**:**.***Z",
  "gtkId": 0,
  "contract": {
    "idn": "***********************",
    "rootIdn": "***********************",
    "cntrType": "**",
    "oldIdn": "***********************",
    "bankId": "*****",
    "bankInn": "*********",
    "mvesNo": "********",
    "mvesDate": "****-**-**T**:**:**.***Z",
    "govDcsnNo": "*****",
    "govDcsnDate": "****-**-**T**:**:**.***Z",
    "okpo": "********",
    "okonx": "**.**.*",
    "contractorUzParentCompany": "********",
    "contractorUzName": "*",
    "rgnCode": "*",
    "industrialZoneCode": 0,
    "contractorUzAddress": "*",
    "contractorUzPhone": "*",
    "cntrSubject": 0,
    "currCode1": "*",
    "currCode2": "*",
    "amount1": 0,
    "amount2": 0,
    "accCurrCode1": "*",
    "accCurrCode2": "*",
    "contractorForName": "*",
    "contractorForCountryCode": "*",
    "contractorForAddress": "*",
    "bankForName": "*",
    "bankForCountryCode": "*",
    "bankForAddress": "*",
    "bankForAttributes": "*",
    "centralBankLetterNo": "*",
    "centralBankLetterDate": "*",
    "creditRate": 0,
    "leasingFlag": 0,
    "productsName": "*",
    "docNo": "*",
    "docDate": "*",
    "execType": 0,
    "execDate": "*",
    "reestrRegNo": "*",
    "softwareName": "*",
    "cntrStatus": 0,
    "lockDate": "*",
    "status": 0,
    "cstDate": "*",
    "contractorUzInn": "*",
    "receivableTotal1": 0,
    "receivableOverdue1": 0,
    "receivableTotal2": 0,
    "receivableOverdue2": 0,
    "payableTotal1": 0,
    "payableOverdue1": 0,
    "payableTotal2": 0,
    "payableOverdue2": 0,
    "insTime": "*",
    "specifications": [
      {
        "docDate": "*",
        "amount": 0,
        "agrmNo": "*",
        "currCode": "*",
        "cstDate": "*",
        "docType": 0,
        "docNo": "*",
        "imExFlag": 0,
        "serialNo": 0,
        "insTime": "*",
        "oldNo": "*",
        "status": 0,
        "confirmations": [
          {
            "userName": "*",
            "confirm": 0,
            "orgType": 0,
            "reason": "*",
            "date": "*",
            "actionType": 0
          }
        ],
        "goods": [
          {
            "tnCode": "*",
            "itemsName": "*",
            "serialNo": 0,
            "unitCode": "*",
            "quantity": 0,
            "currCode": "*",
            "cost": 0,
            "amount": 0,
            "insTime": "*",
            "status": 0
          }
        ],
        "fare": {
          "currCode": "*",
          "amount": 0,
          "insTime": "*",
          "status": 0
        },
        "services": [
          {
            "serialNo": 0,
            "executorName": "*",
            "executorCountryCode": "*",
            "executionCountryCode": "*",
            "serviceName": "*",
            "serviceType": "*",
            "currCode": "*",
            "amount": 0,
            "selfMadeFlag": 0,
            "insTime": "*",
            "status": 0
          }
        ],
        "consignors": [
          {
            "serialNo": 0,
            "name": "*",
            "countryCode": "*",
            "inn": "*",
            "insTime": "*",
            "status": 0,
            "confirmations": [
              {
                "userName": "*",
                "confirm": 0,
                "orgType": 0,
                "reason": "*",
                "date": "*",
                "actionType": 0
              }
            ]
          }
        ],
        "consignees": [
          {
            "serialNo": 0,
            "name": "*",
            "countryCode": "*",
            "inn": "*",
            "insTime": "*",
            "status": 0,
            "confirmations": [
              {
                "userName": "*",
                "confirm": 0,
                "orgType": 0,
                "reason": "*",
                "date": "*",
                "actionType": 0
              }
            ]
          }
        ],
        "manufacturers": [
          {
            "serialNo": 0,
            "name": "*",
            "countryCode": "*",
            "inn": "*",
            "insTime": "*",
            "status": 0,
            "confirmations": [
              {
                "userName": "*",
                "confirm": 0,
                "orgType": 0,
                "reason": "*",
                "date": "*",
                "actionType": 0
              }
            ]
          }
        ],
        "deliveryTerms": [
          {
            "destination": "*",
            "incotermsCode": "*",
            "agrmNo": "*",
            "serialNo": 0,
            "insTime": "*",
            "status": 0,
            "confirmations": [
              {
                "userName": "*",
                "confirm": 0,
                "orgType": 0,
                "reason": "*",
                "date": "*",
                "actionType": 0
              }
            ]
          }
        ],
        "financeInfos": [
          {
            "companyForName": "*",
            "companyForCountryCode": "*",
            "companyForAddress": "*",
            "bankForName": "*",
            "bankForCountryCode": "*",
            "bankForAddress": "*",
            "bankForAttributes": "*",
            "agrmNo": "*",
            "serialNo": 0,
            "insTime": "*",
            "status": 0,
            "confirmations": [
              {
                "userName": "*",
                "confirm": 0,
                "orgType": 0,
                "reason": "*",
                "date": "*",
                "actionType": 0
              }
            ]
          }
        ],
        "agreements": [
          {
            "docNo": "*",
            "docDate": "*",
            "mvesDate": "*",
            "agrmSubject": "*",
            "currCode1": "*",
            "currCode2": "*",
            "accCurrCode1": "*",
            "accCurrCode2": "*",
            "contractorForName": "*",
            "contractorForCountryCode": "*",
            "contractorForAddress": "*",
            "bankForName": "*",
            "bankForCountryCode": "*",
            "bankForAddress": "*",
            "bankForAttributes": "*",
            "contractorUzName": "*",
            "rgnCode": "*",
            "contractorUzAddress": "*",
            "amount1": 0,
            "amount2": 0,
            "cntrSubject": 0,
            "productsName": "*",
            "execType": 0,
            "execDate": "*",
            "creditRate": 0,
            "cstDate": "*",
            "serialNo": 0,
            "insTime": "*",
            "industrialZoneCode": 0,
            "status": 0,
            "confirmations": [
              {
                "userName": "*",
                "confirm": 0,
                "orgType": 0,
                "reason": "*",
                "date": "*",
                "actionType": 0
              }
            ]
          }
        ],
        "endContract": {
          "docDate": "*",
          "remark": "*",
          "insTime": "*",
          "status": 0
        },
        "exportTerms": [
          {
            "exportType": 0,
            "exportDeadline": "*",
            "exportTerm": 0,
            "paymentTerm": 0,
            "paymentDeadline": "*",
            "leasingDeadline": 0,
            "leasingRate": 0,
            "deadlineType": 0,
            "agrmNo": "*",
            "insTime": "*",
            "status": 0,
            "confirmations": [
              {
                "userName": "*",
                "confirm": 0,
                "orgType": 0,
                "reason": "*",
                "date": "*",
                "actionType": 0
              }
            ]
          }
        ],
        "tollingTerms": [
          {
            "idCol": "*",
            "goodsTitle": "*",
            "currCode": "*",
            "currRate1": 0,
            "currRate2": 0,
            "serviceAmount1": 0,
            "serviceAmount2": 0,
            "docDate": "*",
            "rateType1": 0,
            "rateType2": 0,
            "goodsAmount": 0,
            "serialNo": 0,
            "insTime": "*",
            "status": 0,
            "confirmations": [
              {
                "userName": "*",
                "confirm": 0,
                "orgType": 0,
                "reason": "*",
                "date": "*",
                "actionType": 0
              }
            ]
          }
        ],
        "importTerms": [
          {
            "paymentMethod": "*",
            "paymentTerm": 0,
            "paymentDeadline": "*",
            "importTerm": 0,
            "importDeadline": "*",
            "importTermAfterAccr": 0,
            "agrmNo": "*",
            "insTime": "*",
            "status": 0,
            "confirmations": [
              {
                "userName": "*",
                "confirm": 0,
                "orgType": 0,
                "reason": "*",
                "date": "*",
                "actionType": 0
              }
            ]
          }
        ],
        "barterTerms": [
          {
            "paymentMethod": "*",
            "exportDeadline": "*",
            "exportTerm": 0,
            "importTerm": 0,
            "paymentTerm": 0,
            "agrmNo": "*",
            "insTime": "*",
            "status": 0,
            "confirmations": [
              {
                "userName": "*",
                "confirm": 0,
                "orgType": 0,
                "reason": "*",
                "date": "*",
                "actionType": 0
              }
            ]
          }
        ],
        "transfers": [
          {
            "transferType": 0,
            "agrmNo": "*",
            "newIdn": "*",
            "dstBankId": "*",
            "dstBankInn": "*",
            "dstExchInn": "*",
            "remark": "*",
            "serialNo": 0,
            "srcBankUserName": "*",
            "dstBankUserName": "*",
            "srcBankConfirmationFlag": 0,
            "dstBankConfirmationFlag": 0,
            "dstExchName": "*",
            "insTime": "*",
            "oldIdn": "*",
            "status": 0
          }
        ],
        "delegates": [
          {
            "reasonType": 0,
            "courtName": "*",
            "courtDscnNo": "*",
            "courtDscnDate": "*",
            "govDscnNo": "*",
            "govDscnDate": "*",
            "agrmNo": "*",
            "dstInn": "*",
            "dstBankId": "*",
            "dstOkpo": "*",
            "dstOkonx": "*",
            "dstName": "*",
            "dstRgnCode": "*",
            "dstIndustrialZoneCode": 0,
            "dstAddress": "*",
            "dstPhone": "*",
            "remark": "*",
            "serialNo": 0,
            "srcBankConfirmationFlag": 0,
            "dstBankConfirmationFlag": 0,
            "srcBankUserName": "*",
            "dstBankUserName": "*",
            "insTime": "*",
            "oldIdn": "*",
            "parentCompanyCode": "*",
            "newIdn": "*",
            "status": 0,
            "dstBankInn": "*"
          }
        ],
        "tradingPlatforms": [
          {
            "serialNo": 0,
            "docNo": 0,
            "name": "*",
            "url": "*",
            "countryCode": "*",
            "ownFlag": 0,
            "insTime": "*",
            "status": 0,
            "confirmations": [
              {
                "userName": "*",
                "confirm": 0,
                "orgType": 0,
                "reason": "*",
                "date": "*",
                "actionType": 0
              }
            ]
          }
        ]
      }
    ]
  }
}

```

</details>
<!--END ПОЛУЧИТЬ ДОКУМЕНТЫ ВЛАДЕЛЬЦА КОНТРАКТА-->
</details>

<!--GET CONTRACT LIST-->
<details open>
<summary><b>ПОЛУЧИТЬ СПИСОК КОНТРАКТОВ</b> (нажмите, чтобы открыть или скрыть)</summary>

<a id="localhost-7100-api-v1-core-contract-list"></a>

#### localhost-7100/api/v1/contract/update/details


**Method:** POST

<details open>
<summary><b>ЗАПРОС</b></summary>

```json
{
  "bankInn": "*********",
  "bankId": "*****",
  "idn": "***********************"
}
```

</details>

<details open>
<summary><b>ОТВЕТ</b></summary>

```json
{
  "status": "***",
  "errorMsg": "***",
  "timeStamp": "***",
  "gtkId": "***",
  "contract": {
    "id": "***",
    "idn": "***",
    "rootIdn": null,
    "cntrType": "***",
    "oldIdn": null,
    "bankId": "***",
    "bankInn": "***",
    "mvesNo": null,
    "mvesDate": null,
    "govDcsnNo": null,
    "govDcsnDate": null,
    "okpo": "***",
    "okonx": null,
    "contractorUzParentCompany": null,
    "contractorUzName": "***",
    "rgnCode": "***",
    "industrialZoneCode": null,
    "contractorUzAddress": "***",
    "contractorUzPhone": "***",
    "cntrSubject": "***",
    "currCode1": "***",
    "currCode2": null,
    "amount1": "***",
    "amount2": "***",
    "accCurrCode1": "***",
    "accCurrCode2": null,
    "contractorForName": "***",
    "contractorForCountryCode": "***",
    "contractorForAddress": "***",
    "bankForName": "***",
    "bankForCountryCode": "***",
    "bankForAddress": "***",
    "bankForAttributes": "***",
    "centralBankLetterNo": null,
    "centralBankLetterDate": null,
    "creditRate": null,
    "leasingFlag": null,
    "productsName": null,
    "docNo": "***",
    "docDate": "***",
    "execType": null,
    "execDate": null,
    "reestrRegNo": null,
    "softwareName": null,
    "cntrStatus": "***",
    "lockDate": null,
    "status": "***",
    "cstDate": null,
    "contractorUzInn": "***",
    "receivableTotal1": "***",
    "receivableOverdue1": "***",
    "receivableTotal2": "***",
    "receivableOverdue2": "***",
    "payableTotal1": "***",
    "payableOverdue1": "***",
    "payableTotal2": "***",
    "payableOverdue2": "***",
    "insTime": "***",
    "createdDate": null,
    "updatedDate": "***",

    "specifications": [
      {
        "docDate": "***",
        "amount": "***",
        "agrmNo": null,
        "currCode": "***",
        "cstDate": null,
        "docType": "***",
        "docNo": "***",
        "imExFlag": "***",
        "serialNo": "***",
        "insTime": "***",
        "oldNo": "***",
        "status": "***",
        "confirmations": [],
        "goods": [
          {
            "tnCode": "***",
            "itemsName": "***",
            "serialNo": "***",
            "unitCode": "***",
            "quantity": "***",
            "currCode": "***",
            "cost": "***",
            "amount": "***",
            "insTime": "***",
            "status": "***"
          }
        ],
        "fare": null,
        "services": []
      }
    ],

    "performedServices": [],

    "consignors": [
      {
        "serialNo": "***",
        "name": "***",
        "countryCode": "***",
        "inn": null,
        "insTime": "***",
        "status": "***",
        "confirmations": []
      }
    ],

    "consignees": [
      {
        "serialNo": "***",
        "name": "***",
        "countryCode": "***",
        "inn": "***",
        "insTime": "***",
        "status": "***",
        "confirmations": []
      }
    ],

    "manufacturers": [
      {
        "serialNo": "***",
        "name": "***",
        "countryCode": "***",
        "inn": null,
        "insTime": "***",
        "status": "***",
        "confirmations": []
      }
    ],

    "deliveryTerms": [
      {
        "destination": "***",
        "incotermsCode": "***",
        "agrmNo": null,
        "serialNo": "***",
        "insTime": "***",
        "status": "***",
        "confirmations": []
      }
    ],

    "financeInfos": [],
    "agreements": [],
    "endContract": null,
    "exportTerms": [],
    "tollingTerms": [],

    "importTerms": [
      {
        "paymentMethod": "***",
        "paymentTerm": null,
        "paymentDeadline": null,
        "importTerm": "***",
        "importDeadline": null,
        "importTermAfterAccr": null,
        "agrmNo": null,
        "insTime": "***",
        "status": "***",
        "confirmations": []
      }
    ],

    "barterTerms": [],
    "transfers": [],
    "delegates": [],
    "tradingPlatforms": [],

    "declarations": [
      {
        "g7a": "***",
        "g7c": "***",
        "g7b": "***",
        "g17": "***",
        "g24a": "***",
        "gdvipdate": "***",
        "gc3date": null,
        "g2name": "***",
        "g23": "***",
        "g8name": "***",
        "g9name": "***",
        "g1": "***",
        "g22a": "***",
        "g22b": "***",
        "g15": "***",
        "goods": [
          {
            "g32": "***",
            "g33": "***",
            "g42": "***",
            "g37c": "***",
            "g31name": "***",
            "g31amount": "***",
            "g41": "***",
            "g38": "***",
            "g37b": "***"
          }
        ]
      }
    ],

    "guarantees": [],
    "lettersOfCredit": [],
    "policies": [],
    "receivableDrops": [],
    "payableDrops": [],
    "penalties": [],
    "commissions": [],
    "taxAgencyPenalties": [],
    "receivableInfos": [],
    "funds": [],
    "refundsExp": [],
    "movesToEx": [],
    "movesFromEx": [],
    "paymentRefs": [],
    "taxes": [],
    "compensations": [],
    "sales": [],
    "leasePayments": [],

    "payments": [
      {
        "docDate": "***",
        "fundSourceType": "***",
        "currCode": "***",
        "amount": "***",
        "serialNo": "***",
        "docNo": "***",
        "userName": "***",
        "insTime": "***",
        "status": "***",
        "currRate": null,
        "goodsAmount1": "***",
        "goodsAmount2": null,
        "serviceAmount1": null,
        "serviceAmount2": null,
        "rateType": null
      }
    ],

    "refundsImp": [],
    "movesToIm": [],
    "movesFromIm": [],
    "exchangePayments": []
  }
}
```

</details>
</details>
<!--END ПОЛУЧИТЬ СПИСОК КОНТРАКТОВ-->

<details open>
<summary><b>ПОЛУЧИТЬ ИСТОРИЮ КОНТРАКТА</b> (нажмите, чтобы открыть или скрыть)</summary>

<a id="localhost-7100-api-v1-contract-history"></a>


#### localhost-7100/api/v1/contract/history

<b>Method name:POST</b>

<details open>
<summary><b>ЗАПРОС</b></summary>

```json
{
  "bankInn": "*********",
  "bankId": "*****",
  "idn": "***********************"
}

```

</details>
<details open>
<summary><b>ОТВЕТ</b></summary>

```json
{
  "status": 0,
  "errorMsg": "OK",
  "timeStamp": "2025-10-23T12:35:04.046Z",
  "gtkId": 15021918,
  "contract": {
    "idn": "207179114204743049190200005",
    "rootIdn": "201052966302916241219800003",
    "cntrType": "02",
    "oldIdn": "207179114204743049190200005",
    "bankId": "00898",
    "bankInn": "207179114",
    "mvesNo": "26600702",
    "mvesDate": "2025-10-23T12:35:04.046Z",
    "govDcsnNo": "ПП-2313",
    "govDcsnDate": "2025-10-23T12:35:04.046Z",
    "okpo": "02906897",
    "okonx": "20.30.0",
    "contractorUzParentCompany": "Вышестоящая организация отсутсвует",
    "contractorUzName": "GAMMA COLOR SERVICE OOO",
    "rgnCode": "27",
    "industrialZoneCode": 3,
    "contractorUzAddress": "27",
    "contractorUzPhone": "998901234567",
    "cntrSubject": 1,
    "currCode1": "860",
    "currCode2": "860",
    "amount1": 1000,
    "amount2": 1000,
    "accCurrCode1": "860",
    "accCurrCode2": "860",
    "contractorForName": "ТОО \"Sercan Investment Group\"",
    "contractorForCountryCode": "398",
    "contractorForAddress": "Республика Казахстан, Восточно-Казахстанская обл., Усть-Каменогорск, Самарское шоссе, 17",
    "bankForName": "АО \"Народный Банк Казахстана\"",
    "bankForCountryCode": "398",
    "bankForAddress": "Республика Казахстан",
    "bankForAttributes": "Р/С:KZ516010151000230954 БИКHSBKKZKX Cor.ac.:№8900372605 SWIFT BIC:IRVTUS3NXXX",
    "centralBankLetterNo": "222",
    "centralBankLetterDate": "2025-10-23T12:35:04.046Z",
    "creditRate": 1,
    "leasingFlag": 1,
    "productsName": "фланцевая заглушка для магистральных трубопровод",
    "docNo": "EX-41/2016",
    "docDate": "2025-10-23T12:35:04.046Z",
    "execType": 2,
    "execDate": "2025-10-23T12:35:04.046Z",
    "reestrRegNo": "0810016",
    "softwareName": "программное обеспечение",
    "cntrStatus": 0,
    "lockDate": "2025-10-23T12:35:04.046Z",
    "status": 0,
    "cstDate": "2025-10-23T12:35:04.046Z",
    "contractorUzInn": "202328794",
    "receivableTotal1": 1000,
    "receivableOverdue1": 1000,
    "receivableTotal2": 1000,
    "receivableOverdue2": 1000,
    "payableTotal1": 1000,
    "payableOverdue1": 1000,
    "payableTotal2": 1000,
    "payableOverdue2": 1000,
    "insTime": "2020-06-04T04:33:06.417Z",
    "specifications": [
      {
        "docDate": "2025-10-23T12:35:04.046Z",
        "amount": 1000,
        "agrmNo": "1",
        "currCode": "860",
        "cstDate": "2025-10-23T12:35:04.046Z",
        "docType": 0,
        "docNo": "1",
        "imExFlag": 1,
        "serialNo": 1,
        "insTime": "2020-06-04T04:33:06.417Z",
        "oldNo": "1",
        "status": 0,
        "confirmations": [
          {
            "userName": "Иванов И.И.",
            "confirm": 0,
            "orgType": 1,
            "reason": "введен по ошибке",
            "date": "2020-06-04T04:33:06.417Z",
            "actionType": 1
          }
        ],
        "goods": [
          {
            "tnCode": "0101210000",
            "itemsName": "Картофель продовольственный",
            "serialNo": 1,
            "unitCode": "796",
            "quantity": 50,
            "currCode": "860",
            "cost": 1000,
            "amount": 1000,
            "insTime": "2020-06-04T04:33:06.417Z",
            "status": 0
          }
        ],
        "fare": {
          "currCode": "860",
          "amount": 1000,
          "insTime": "2020-06-04T04:33:06.417Z",
          "status": 0
        },
        "services": [
          {
            "serialNo": 1,
            "executorName": "ЧП \"SAIF-OPTIMA\"",
            "executorCountryCode": "860",
            "executionCountryCode": "860",
            "serviceName": "Переработка зерна в муку",
            "serviceType": "12",
            "currCode": "860",
            "amount": 1000,
            "selfMadeFlag": 1,
            "insTime": "2020-06-04T04:33:06.417Z",
            "status": 0
          }
        ]
      }
    ],
    "performedServices": [
      {
        "docDate": "2025-10-23T12:35:04.046Z",
        "docNo": "479657",
        "docType": 1,
        "currCode1": "860",
        "amount1": 1000,
        "amount2": 1000,
        "currCode2": "860",
        "insTime": "2020-06-04T04:33:06.417Z",
        "imExFlag": 1,
        "status": 0,
        "confirmations": [
          {
            "userName": "Иванов И.И.",
            "confirm": 0,
            "orgType": 1,
            "reason": "введен по ошибке",
            "date": "2020-06-04T04:33:06.417Z",
            "actionType": 1
          }
        ],
        "services": [
          {
            "serialNo": 1,
            "executorName": "ЧП \"SAIF-OPTIMA\"",
            "executorCountryCode": "860",
            "executionCountryCode": "860",
            "serviceName": "Переработка зерна в муку",
            "serviceType": "12",
            "currCode": "860",
            "amount": 1000,
            "selfMadeFlag": 1,
            "insTime": "2020-06-04T04:33:06.417Z",
            "status": 0
          }
        ],
        "goods": [
          {
            "tnCode": "0101210000",
            "itemsName": "Картофель продовольственный",
            "serialNo": 1,
            "unitCode": "796",
            "quantity": 50,
            "currCode": "860",
            "cost": 1000,
            "amount": 1000,
            "insTime": "2020-06-04T04:33:06.417Z",
            "status": 0
          }
        ]
      }
    ],
    "consignors": [
      {
        "serialNo": 1,
        "name": "ООО \"Техномолпром\"",
        "countryCode": "840",
        "inn": "206013203",
        "insTime": "2020-06-04T04:33:06.417Z",
        "status": 0,
        "confirmations": [
          {
            "userName": "Иванов И.И.",
            "confirm": 0,
            "orgType": 1,
            "reason": "введен по ошибке",
            "date": "2020-06-04T04:33:06.417Z",
            "actionType": 1
          }
        ]
      }
    ],
    "consignees": [
      {
        "serialNo": 1,
        "name": "ООО \"Техномолпром\"",
        "countryCode": "840",
        "inn": "206013203",
        "insTime": "2020-06-04T04:33:06.417Z",
        "status": 0,
        "confirmations": [
          {
            "userName": "Иванов И.И.",
            "confirm": 0,
            "orgType": 1,
            "reason": "введен по ошибке",
            "date": "2020-06-04T04:33:06.417Z",
            "actionType": 1
          }
        ]
      }
    ],
    "manufacturers": [
      {
        "serialNo": 1,
        "name": "ООО \"Техномолпром\"",
        "countryCode": "840",
        "inn": "206013203",
        "insTime": "2020-06-04T04:33:06.417Z",
        "status": 0,
        "confirmations": [
          {
            "userName": "Иванов И.И.",
            "confirm": 0,
            "orgType": 1,
            "reason": "введен по ошибке",
            "date": "2020-06-04T04:33:06.417Z",
            "actionType": 1
          }
        ]
      }
    ],
    "deliveryTerms": [
      {
        "destination": "Янгиюль, Узбекистан",
        "incotermsCode": "02",
        "agrmNo": "1",
        "serialNo": 1,
        "insTime": "2020-06-04T04:33:06.417Z",
        "status": 0,
        "confirmations": [
          {
            "userName": "Иванов И.И.",
            "confirm": 0,
            "orgType": 1,
            "reason": "введен по ошибке",
            "date": "2020-06-04T04:33:06.417Z",
            "actionType": 1
          }
        ]
      }
    ],
    "financeInfos": [
      {
        "companyForName": "\"THE ALPINE COMMERCE GROUP LLC\"",
        "companyForCountryCode": "860",
        "companyForAddress": "11 Peace Valley Rd, Towaco, New Jersey,07082 USA",
        "bankForName": "TD BANK N.A.",
        "bankForCountryCode": "840",
        "bankForAddress": "430 North Beverwyck Road Parsippany, NJ 07054",
        "bankForAttributes": "4308134115SWIFT NUMBER:NRTHUS33XXX",
        "agrmNo": "1",
        "serialNo": 1,
        "insTime": "2020-06-04T04:33:06.417Z",
        "status": 0,
        "confirmations": [
          {
            "userName": "Иванов И.И.",
            "confirm": 0,
            "orgType": 1,
            "reason": "введен по ошибке",
            "date": "2020-06-04T04:33:06.417Z",
            "actionType": 1
          }
        ]
      }
    ],
    "agreements": [
      {
        "docNo": "1",
        "docDate": "2025-10-23T12:35:04.046Z",
        "mvesDate": "2025-10-23T12:35:04.046Z",
        "agrmSubject": "Изменение спецификации и условий контракта",
        "currCode1": "860",
        "currCode2": "860",
        "accCurrCode1": "860",
        "accCurrCode2": "860",
        "contractorForName": "ТОО \"Sercan Investment Group\"",
        "contractorForCountryCode": "398",
        "contractorForAddress": "Республика Казахстан, Восточно-Казахстанская обл., Усть-Каменогорск, Самарское шоссе, 17",
        "bankForName": "АО \"Народный Банк Казахстана\"",
        "bankForCountryCode": "398",
        "bankForAddress": "Республика Казахстан",
        "bankForAttributes": "Р/С:KZ516010151000230954 БИКHSBKKZKX Cor.ac.:№8900372605 SWIFT BIC:IRVTUS3NXXX",
        "contractorUzName": "ООО \"Adler Group Distibution\"",
        "rgnCode": "01",
        "contractorUzAddress": "РУз, Ташкентская область , букинский район, ул. Узбекистон, дом 3",
        "amount1": 1000,
        "amount2": 1000,
        "cntrSubject": 1,
        "productsName": "фланцевая заглушка для магистральных трубопровод",
        "execType": 2,
        "execDate": "2025-10-23T12:35:04.046Z",
        "creditRate": 1,
        "cstDate": "2025-10-23T12:35:04.046Z",
        "serialNo": 1,
        "insTime": "2020-06-04T04:33:06.417Z",
        "industrialZoneCode": 3,
        "status": 0,
        "confirmations": [
          {
            "userName": "Иванов И.И.",
            "confirm": 0,
            "orgType": 1,
            "reason": "введен по ошибке",
            "date": "2020-06-04T04:33:06.417Z",
            "actionType": 1
          }
        ]
      }
    ],
    "endContract": {
      "docDate": "2025-10-23T12:35:04.046Z",
      "remark": "в связи с отсутствием задолженности",
      "insTime": "2020-06-04T04:33:06.417Z",
      "status": 0
    },
    "exportTerms": [
      {
        "exportType": 1,
        "exportDeadline": "2025-10-23T12:35:04.046Z",
        "exportTerm": 120,
        "paymentTerm": 120,
        "paymentDeadline": "2025-10-23T12:35:04.046Z",
        "leasingDeadline": 3,
        "leasingRate": 1,
        "deadlineType": 1,
        "agrmNo": "1",
        "insTime": "2020-06-04T04:33:06.417Z",
        "status": 0,
        "confirmations": [
          {
            "userName": "Иванов И.И.",
            "confirm": 0,
            "orgType": 1,
            "reason": "введен по ошибке",
            "date": "2020-06-04T04:33:06.417Z",
            "actionType": 1
          }
        ]
      }
    ],
    "tollingTerms": [
      {
        "idCol": "string",
        "goodsTitle": "string",
        "currCode": "860",
        "currRate1": 1.09,
        "currRate2": 1.09,
        "serviceAmount1": 1000,
        "serviceAmount2": 1000,
        "docDate": "2025-10-23T12:35:04.046Z",
        "rateType1": 0,
        "rateType2": 0,
        "goodsAmount": 1000,
        "serialNo": 1,
        "insTime": "2020-06-04T04:33:06.417Z",
        "status": 0,
        "confirmations": [
          {
            "userName": "Иванов И.И.",
            "confirm": 0,
            "orgType": 1,
            "reason": "введен по ошибке",
            "date": "2020-06-04T04:33:06.417Z",
            "actionType": 1
          }
        ]
      }
    ],
    "importTerms": [
      {
        "paymentMethod": "04",
        "paymentTerm": 5,
        "paymentDeadline": "2025-10-23T12:35:04.046Z",
        "importTerm": 30,
        "importDeadline": "2025-10-23T12:35:04.046Z",
        "importTermAfterAccr": 90,
        "agrmNo": "1",
        "insTime": "2020-06-04T04:33:06.417Z",
        "status": 0,
        "confirmations": [
          {
            "userName": "Иванов И.И.",
            "confirm": 0,
            "orgType": 1,
            "reason": "введен по ошибке",
            "date": "2020-06-04T04:33:06.417Z",
            "actionType": 1
          }
        ]
      }
    ],
    "barterTerms": [
      {
        "paymentMethod": "06",
        "exportDeadline": "2025-10-23T12:35:04.046Z",
        "exportTerm": 10,
        "importTerm": 10,
        "paymentTerm": 10,
        "agrmNo": "1",
        "insTime": "2020-06-04T04:33:06.417Z",
        "status": 0,
        "confirmations": [
          {
            "userName": "Иванов И.И.",
            "confirm": 0,
            "orgType": 1,
            "reason": "введен по ошибке",
            "date": "2020-06-04T04:33:06.417Z",
            "actionType": 1
          }
        ]
      }
    ],
    "transfers": [
      {
        "transferType": 1,
        "agrmNo": "1",
        "newIdn": "207179114204743049190200005",
        "dstBankId": "01061",
        "dstBankInn": "201589828",
        "dstExchInn": "201589828",
        "remark": "Доп.Согл.№ 2 от 20.04.2017г.",
        "serialNo": 1,
        "srcBankUserName": "Иванов И.И.",
        "dstBankUserName": "Иванов И.И.",
        "srcBankConfirmationFlag": 1,
        "dstBankConfirmationFlag": 1,
        "dstExchName": "string",
        "insTime": "2020-06-04T04:33:06.417Z",
        "oldIdn": "207179114204743049190200005",
        "status": 0
      }
    ],
    "delegates": [
      {
        "reasonType": 1,
        "courtName": "Верховный суд Республики Узбекистан",
        "courtDscnNo": "2017/11-00",
        "courtDscnDate": "2025-10-23T12:35:04.046Z",
        "govDscnNo": "ПП-4388",
        "govDscnDate": "2025-10-23T12:35:04.046Z",
        "agrmNo": "1",
        "dstInn": "200837914",
        "dstBankId": "01061",
        "dstOkpo": "26825672",
        "dstOkonx": "20.30.0",
        "dstName": "Агрофирма \"DOLINA AGRO TRANZIT\"",
        "dstRgnCode": "30",
        "dstIndustrialZoneCode": 3,
        "dstAddress": "Таш. обл. Юкори. Чирчик. Пос. ИИК Ота Машино тракт",
        "dstPhone": "712072772",
        "remark": "Доп.Согл.№ 2 от 20.04.2017г.",
        "serialNo": 1,
        "srcBankConfirmationFlag": 1,
        "dstBankConfirmationFlag": 1,
        "srcBankUserName": "Иванов И.И.",
        "dstBankUserName": "Иванов И.И.",
        "insTime": "2020-06-04T04:33:06.417Z",
        "oldIdn": "207179114204743049190200005",
        "parentCompanyCode": "Хокимият г.Ташкента",
        "newIdn": "207179114204743049190200005",
        "status": 0,
        "dstBankInn": "201589828"
      }
    ],
    "tradingPlatforms": [
      {
        "serialNo": 1,
        "docNo": 1,
        "name": "Google",
        "url": "www.google.com",
        "countryCode": "840",
        "ownFlag": 1,
        "insTime": "2020-06-04T04:33:06.417Z",
        "status": 0,
        "confirmations": [
          {
            "userName": "Иванов И.И.",
            "confirm": 0,
            "orgType": 1,
            "reason": "введен по ошибке",
            "date": "2020-06-04T04:33:06.417Z",
            "actionType": 1
          }
        ]
      }
    ],
    "declarations": [
      {
        "g7a": "03015",
        "g7c": "0008999",
        "g7b": "2025-10-23T12:35:04.046Z",
        "g17": "840",
        "g24a": "21",
        "gdvipdate": "2025-10-23T12:35:04.046Z",
        "gc3date": "2025-10-23T12:35:04.046Z",
        "g2name": "ООО \"Водий Пахтаси\"",
        "g23": 9446.65,
        "g8name": "ООО Производственно-коммерческая компания ВИВА",
        "g9name": "ООО \"Водий Пахтаси\"",
        "g1": "ЭК10",
        "g22a": "840",
        "g22b": 1000,
        "g15": "840",
        "goods": [
          {
            "g32": 1,
            "g33": "3204110000",
            "g42": 1000,
            "g37c": "000",
            "g31name": "1)Вата хлопчатобумажная чесаная \"Люкс\", O'zDSt 1029:2003, в кипах -  вес: 20306 кг, ; 2)99 кипов;",
            "g31amount": 30,
            "g41": "166",
            "g38": 4400,
            "g37b": "00"
          }
        ]
      }
    ],
    "guarantees": [
      {
        "actionType": 0,
        "bankForName": "Gazprombank, Moscow, Russia",
        "docNo": "GC1783916002896",
        "docStartDate": "2025-10-23T12:35:04.046Z",
        "currCode": "860",
        "amount": 1000,
        "currRate": 1.0419,
        "amount1": 1000,
        "amount2": 1000,
        "docValDate": "2025-10-23T12:35:04.046Z",
        "docValDays": 120,
        "userName": "Иванов И.И.",
        "rateType": 0,
        "insTime": "2020-06-04T04:33:06.417Z",
        "status": 0,
        "guaranteeTimeChanges": [
          {
            "actionType": 0,
            "newDocValDate": "2025-10-23T12:35:04.046Z",
            "newDocValDays": 150,
            "reasonType": 4,
            "agrmNo": "1",
            "otherReason": "письмо клиента №UZGTL-GEN-6202 от 11.12.2017 г.",
            "serialNo": 1,
            "userName": "Иванов И.И.",
            "docValDate": "2025-10-23T12:35:04.046Z",
            "docValDays": 120,
            "insTime": "2020-06-04T04:33:06.417Z",
            "status": 0,
            "confirmations": [
              {
                "userName": "Иванов И.И.",
                "confirm": 0,
                "orgType": 1,
                "reason": "введен по ошибке",
                "date": "2020-06-04T04:33:06.417Z",
                "actionType": 1
              }
            ]
          }
        ],
        "guaranteeSumChanges": [
          {
            "actionType": 0,
            "newAmount": 1000,
            "currRate": 1.0419,
            "newAmount1": 1000,
            "newAmount2": 1000,
            "reasonType": 4,
            "agrmNo": "1",
            "otherReason": "письмо клиента №UZGTL-GEN-6202 от 11.12.2017 г.",
            "serialNo": 1,
            "userName": "Иванов И.И.",
            "rateType": 0,
            "amount": 1000,
            "insTime": "2020-06-04T04:33:06.417Z",
            "status": 0,
            "confirmations": [
              {
                "userName": "Иванов И.И.",
                "confirm": 0,
                "orgType": 1,
                "reason": "введен по ошибке",
                "date": "2020-06-04T04:33:06.417Z",
                "actionType": 1
              }
            ]
          }
        ],
        "confirmations": [
          {
            "userName": "Иванов И.И.",
            "confirm": 0,
            "orgType": 1,
            "reason": "введен по ошибке",
            "date": "2020-06-04T04:33:06.417Z",
            "actionType": 1
          }
        ]
      }
    ],
    "lettersOfCredit": [
      {
        "actionType": 0,
        "docNo": "459210918111304",
        "docStartDate": "2025-10-23T12:35:04.046Z",
        "currCode": "860",
        "amount": 1000,
        "currRate": 1.12,
        "amount1": 1000,
        "amount2": 1000,
        "docValDays": 120,
        "userName": "Иванов И.И.",
        "fundsAvlblFlag": 1,
        "fundsBoughtFlag": 1,
        "fundsWillBeBoughtFlag": 1,
        "rateType": 0,
        "insTime": "2020-06-04T04:33:06.417Z",
        "status": 0,
        "letterOfCreditSumChanges": [
          {
            "actionType": 0,
            "newAmount": 1000,
            "currRate": 1.012,
            "newAmount1": 1000,
            "newAmount2": 1000,
            "reasonType": 4,
            "agrmNo": "1",
            "otherReason": "письмо АО \"АГМК\" №11-0630 от 11.10.2018",
            "serialNo": 1,
            "userName": "Иванов И.И.",
            "rateType": 0,
            "amount": 1000,
            "insTime": "2020-06-04T04:33:06.417Z",
            "status": 0,
            "confirmations": [
              {
                "userName": "Иванов И.И.",
                "confirm": 0,
                "orgType": 1,
                "reason": "введен по ошибке",
                "date": "2020-06-04T04:33:06.417Z",
                "actionType": 1
              }
            ]
          }
        ],
        "letterOfCreditTimeChanges": [
          {
            "actionType": 0,
            "newDocValDays": 150,
            "reasonType": 4,
            "agrmNo": "1",
            "otherReason": "письмо АО \"АГМК\" №11-0630 от 11.10.2018",
            "serialNo": 1,
            "userName": "Иванов И.И.",
            "docValDays": 120,
            "insTime": "2020-06-04T04:33:06.417Z",
            "status": 0,
            "confirmations": [
              {
                "userName": "Иванов И.И.",
                "confirm": 0,
                "orgType": 1,
                "reason": "введен по ошибке",
                "date": "2020-06-04T04:33:06.417Z",
                "actionType": 1
              }
            ]
          }
        ],
        "confirmations": [
          {
            "userName": "Иванов И.И.",
            "confirm": 0,
            "orgType": 1,
            "reason": "введен по ошибке",
            "date": "2020-06-04T04:33:06.417Z",
            "actionType": 1
          }
        ]
      }
    ],
    "policies": [
      {
        "actionType": 0,
        "insuranceCompanyInn": "207135501",
        "docNo": "14/00/00-1905-02",
        "docDate": "2025-10-23T12:35:04.046Z",
        "currCode": "860",
        "amount": 1000,
        "currRate": 1.09,
        "amount1": 1000,
        "amount2": 1000,
        "docValDate": "2025-10-23T12:35:04.046Z",
        "docValDays": 120,
        "userName": "Иванов И.И.",
        "rateType": 1,
        "insuranceCompanyName": "GROSS INSURANCE OOO",
        "insTime": "2020-06-04T04:33:06.417Z",
        "status": 0,
        "policySumChanges": [
          {
            "actionType": 0,
            "newAmount": 1000,
            "currRate": 1.09,
            "newAmount1": 1000,
            "newAmount2": 1000,
            "reasonType": 4,
            "agrmNo": "1",
            "otherReason": "письмо АО \"АГМК\" №11-0630 от 11.10.2018",
            "serialNo": 1,
            "userName": "Иванов И.И.",
            "docDate": "2025-10-23T12:35:04.046Z",
            "rateType": 1,
            "amount": 1000,
            "insTime": "2020-06-04T04:33:06.417Z",
            "status": 0,
            "confirmations": [
              {
                "userName": "Иванов И.И.",
                "confirm": 0,
                "orgType": 1,
                "reason": "введен по ошибке",
                "date": "2020-06-04T04:33:06.417Z",
                "actionType": 1
              }
            ]
          }
        ],
        "policyTimeChanges": [
          {
            "actionType": 0,
            "newDocValDate": "2025-10-23T12:35:04.046Z",
            "newDocValDays": 150,
            "reasonType": 4,
            "agrmNo": "1",
            "otherReason": "письмо АО \"АГМК\" №11-0630 от 11.10.2018",
            "serialNo": 1,
            "userName": "Иванов И.И.",
            "docDate": "2025-10-23T12:35:04.046Z",
            "docValDate": "2025-10-23T12:35:04.046Z",
            "docValDays": 120,
            "insTime": "2020-06-04T04:33:06.417Z",
            "status": 0,
            "confirmations": [
              {
                "userName": "Иванов И.И.",
                "confirm": 0,
                "orgType": 1,
                "reason": "введен по ошибке",
                "date": "2020-06-04T04:33:06.417Z",
                "actionType": 1
              }
            ]
          }
        ],
        "confirmations": [
          {
            "userName": "Иванов И.И.",
            "confirm": 0,
            "orgType": 1,
            "reason": "введен по ошибке",
            "date": "2020-06-04T04:33:06.417Z",
            "actionType": 1
          }
        ]
      }
    ],
    "receivableDrops": [
      {
        "actionType": 0,
        "currCode": "860",
        "amount": 1000,
        "reasonType": 1,
        "courtName": "Верховный суд Республики Узбекистан",
        "courtDscnNo": "2017/11-00",
        "courtDscnDate": "2025-10-23T12:35:04.046Z",
        "dropAgency": "Налоговый коммитет",
        "dropAgencyDscnNo": "75-2017/1",
        "dropAgencyDscnDate": "2025-10-23T12:35:04.046Z",
        "govDscnNo": "ПП-4388",
        "govDscnDate": "2025-10-23T12:35:04.046Z",
        "serialNo": 1,
        "amount1": 1000,
        "amount2": 1000,
        "docDate": "2025-10-23T12:35:04.046Z",
        "userName": "Иванов И.И.",
        "remark": "Примечание",
        "insTime": "2020-06-04T04:33:06.417Z",
        "status": 0,
        "confirmations": [
          {
            "userName": "Иванов И.И.",
            "confirm": 0,
            "orgType": 1,
            "reason": "введен по ошибке",
            "date": "2020-06-04T04:33:06.417Z",
            "actionType": 1
          }
        ]
      }
    ],
    "payableDrops": [
      {
        "actionType": 0,
        "currCode": "860",
        "amount": 1000,
        "reasonType": 4,
        "agrmNo": "1",
        "courtName": "Верховный суд Республики Узбекистан",
        "courtDscnNo": "2017/11-00",
        "courtDscnDate": "2025-10-23T12:35:04.046Z",
        "dropAgency": "Налоговый коммитет",
        "dropAgencyDscnNo": "75-2017/1",
        "dropAgencyDscnDate": "2025-10-23T12:35:04.046Z",
        "otherReason": "Основание письмо АГМК №07-1126 от 04,12,2018 г",
        "serialNo": 1,
        "amount1": 1000,
        "amount2": 1000,
        "docDate": "2025-10-23T12:35:04.046Z",
        "userName": "Иванов И.И.",
        "insTime": "2020-06-04T04:33:06.417Z",
        "status": 0,
        "confirmations": [
          {
            "userName": "Иванов И.И.",
            "confirm": 0,
            "orgType": 1,
            "reason": "введен по ошибке",
            "date": "2020-06-04T04:33:06.417Z",
            "actionType": 1
          }
        ]
      }
    ],
    "penalties": [
      {
        "actionType": 0,
        "penaltyType": 1,
        "docDate": "2025-10-23T12:35:04.046Z",
        "currCode": "860",
        "amount": 1000,
        "remark": "Штраф (претензия) согалсно Акта сверки от 31.12.2016",
        "serialNo": 1,
        "userName": "Иванов И.И.",
        "insTime": "2020-06-04T04:33:06.417Z",
        "status": 0,
        "confirmations": [
          {
            "userName": "Иванов И.И.",
            "confirm": 0,
            "orgType": 1,
            "reason": "введен по ошибке",
            "date": "2020-06-04T04:33:06.417Z",
            "actionType": 1
          }
        ]
      }
    ],
    "commissions": [
      {
        "actionType": 0,
        "paymentSerialNo": 1,
        "amount": 1000,
        "amount1": 1000,
        "amount2": 1000,
        "serialNo": 1,
        "userName": "Иванов И.И.",
        "fundSerialNo": 1,
        "currRate": 1.0419,
        "rateType": 0,
        "insTime": "2020-06-04T04:33:06.417Z",
        "status": 0,
        "confirmations": [
          {
            "userName": "Иванов И.И.",
            "confirm": 0,
            "orgType": 1,
            "reason": "введен по ошибке",
            "date": "2020-06-04T04:33:06.417Z",
            "actionType": 1
          }
        ]
      }
    ],
    "taxAgencyPenalties": [
      {
        "actionType": 0,
        "docDate": "2025-10-23T12:35:04.046Z",
        "amountSoum": 1000,
        "currCode": "860",
        "receivableAmount": 1000,
        "courtName": "Верховный суд Республики Узбекистан",
        "courtDscnNo": "2017/11-00",
        "courtDscnDate": "2025-10-23T12:35:04.046Z",
        "serialNo": 1,
        "userName": "Иванов И.И.",
        "insTime": "2020-06-04T04:33:06.417Z",
        "status": 0,
        "confirmations": [
          {
            "userName": "Иванов И.И.",
            "confirm": 0,
            "orgType": 1,
            "reason": "введен по ошибке",
            "date": "2020-06-04T04:33:06.417Z",
            "actionType": 1
          }
        ]
      }
    ],
    "receivableInfos": [
      {
        "actionType": 0,
        "tempImpCurrCode": "860",
        "tempImpAmount": 1000,
        "customsMode": 1,
        "remark": "string",
        "serialNo": 1,
        "userName": "Иванов И.И.",
        "insTime": "2020-06-04T04:33:06.417Z",
        "status": 0,
        "confirmations": [
          {
            "userName": "Иванов И.И.",
            "confirm": 0,
            "orgType": 1,
            "reason": "введен по ошибке",
            "date": "2020-06-04T04:33:06.417Z",
            "actionType": 1
          }
        ]
      }
    ],
    "funds": [
      {
        "actionType": 0,
        "serialNo": 1,
        "docNo": "30000000000000",
        "docDate": "2025-10-23T12:35:04.046Z",
        "fundDestType": 1,
        "centralBankDscnNo": "352-2",
        "centralBankDscnDate": "2025-10-23T12:35:04.046Z",
        "bankForName": "TD BANK N.A.",
        "conditionType": 1,
        "letterOfCreditNo": "459210918111304",
        "guaranteeNo": "GC1783916002896",
        "policyNo": "14/00/00-1905-02",
        "currCode": "860",
        "amount": 1000,
        "currRate": 1.0419,
        "goodsAmount1": 1000,
        "goodsAmount2": 1000,
        "serviceAmount1": 1000,
        "serviceAmount2": 1000,
        "userName": "Иванов И.И.",
        "rateType": 0,
        "bankAccountant": "Иванов И.И.",
        "cashFlag": 0,
        "insTime": "2020-06-04T04:33:06.417Z",
        "status": 0,
        "confirmations": [
          {
            "userName": "Иванов И.И.",
            "confirm": 0,
            "orgType": 1,
            "reason": "введен по ошибке",
            "date": "2020-06-04T04:33:06.417Z",
            "actionType": 1
          }
        ]
      }
    ],
    "refundsExp": [
      {
        "actionType": 0,
        "docDate": "2025-10-23T12:35:04.046Z",
        "fundSerialNo": 1,
        "amount": 1000,
        "currRate": 1.09,
        "goodsAmount1": 1000,
        "goodsAmount2": 1000,
        "serviceAmount1": 1000,
        "serviceAmount2": 1000,
        "serialNo": 1,
        "moveFromExSerialNo": 1,
        "userName": "Иванов И.И.",
        "rateType": 0,
        "bankAccountant": "Иванов И.И.",
        "fundDestType": 3,
        "docNo": "30000000000000",
        "countryCode": "840",
        "offshoreCode": "112",
        "insTime": "2020-06-04T04:33:06.417Z",
        "status": 0,
        "confirmations": [
          {
            "userName": "Иванов И.И.",
            "confirm": 0,
            "orgType": 1,
            "reason": "введен по ошибке",
            "date": "2020-06-04T04:33:06.417Z",
            "actionType": 1
          }
        ]
      }
    ],
    "movesToEx": [
      {
        "actionType": 0,
        "fundSerialNo": 1,
        "sourceAmount": 1000,
        "moveFromExSerialNo": 1,
        "currRate": 1.02,
        "sourceGoodsAmount1": 1000,
        "sourceGoodsAmount2": 1000,
        "sourceServiceAmount1": 1000,
        "sourceServiceAmount2": 1000,
        "saleType": 1,
        "destinationIdn": "207179114204743049190200005",
        "fundDestType": 1,
        "conditionType": 1,
        "letterOfCreditNo": "459210918111304",
        "guaranteeNo": "GC1783916002896",
        "policyNo": "14/00/00-1905-02",
        "currCode": "860",
        "currRate12": 1.02,
        "destinationAmount": 1000,
        "accCurrRate": 1.02,
        "destinationGoodsAmount1": 1000,
        "destinationGoodsAmount2": 1000,
        "destinationServiceAmount1": 1000,
        "destinationServiceAmount2": 1000,
        "reasonType": 5,
        "sourceAgrmNo": "1",
        "destinationAgrmNo": "1",
        "courtName": "Верховный суд Республики Узбекистан",
        "courtDscnNo": "2017/11-00",
        "courtDscnDate": "2025-10-23T12:35:04.046Z",
        "otherReason": "Основание письмо АГМК №07-1126 от 04,12,2018 г",
        "docDate": "2025-10-23T12:35:04.046Z",
        "serialNo": 1,
        "userName": "Иванов И.И.",
        "rateType": 0,
        "rateType12": 0,
        "accRateType": 0,
        "destinationMoveFromExSerialNo": 1,
        "insTime": "2020-06-04T04:33:06.417Z",
        "status": 0,
        "confirmations": [
          {
            "userName": "Иванов И.И.",
            "confirm": 0,
            "orgType": 1,
            "reason": "введен по ошибке",
            "date": "2020-06-04T04:33:06.417Z",
            "actionType": 1
          }
        ]
      }
    ],
    "movesFromEx": [
      {
        "docDate": "2025-10-23T12:35:04.046Z",
        "sourceIdn": "207179114204743049190200005",
        "fundDestType": 1,
        "conditionType": 1,
        "letterOfCreditNo": "459210918111304",
        "guaranteeNo": "GC1783916002896",
        "policyNo": "14/00/00-1905-02",
        "currCode": "860",
        "amount": 1000,
        "currRate": 1.0419,
        "goodsAmount1": 1000,
        "goodsAmount2": 1000,
        "serviceAmount1": 1000,
        "serviceAmount2": 1000,
        "reasonType": 4,
        "destinationAgrmNo": "1",
        "courtName": "Верховный суд Республики Узбекистан",
        "courtDscnNo": "2017/11-00",
        "courtDscnDate": "2025-10-23T12:35:04.046Z",
        "otherReason": "Основание письмо АГМК №07-1126 от 04,12,2018 г",
        "serialNo": 1,
        "userName": "Иванов И.И.",
        "sourceFundDate": "2025-10-23T12:35:04.046Z",
        "rateType": 0,
        "saleType": 1,
        "sourceMoveToExSerialNo": 1,
        "insTime": "2020-06-04T04:33:06.417Z",
        "status": 0,
        "confirmations": [
          {
            "userName": "Иванов И.И.",
            "confirm": 0,
            "orgType": 1,
            "reason": "введен по ошибке",
            "date": "2020-06-04T04:33:06.417Z",
            "actionType": 1
          }
        ]
      }
    ],
    "paymentRefs": [
      {
        "actionType": 0,
        "docNo": "00873/13.10.16/00002",
        "docDate": "2025-10-23T12:35:04.046Z",
        "reasonType": 1,
        "fundSerialNo": 1,
        "letterOfCreditNo": "459210918111304",
        "guaranteeNo": "GC1783916002896",
        "policyNo": "14/00/00-1905-02",
        "amount": 1000,
        "currCode": "860",
        "docValDate": "2025-10-23T12:35:04.046Z",
        "bankAccountant": "Иванов И.И.",
        "bankManager": "Иванов И.И.",
        "mainBranchUserName": "Иванов И.И.",
        "userName": "Иванов И.И.",
        "moveFromExSerialNo": 1,
        "tnCode": "0101210000",
        "unusedAmount": 1000,
        "insTime": "2020-06-04T04:33:06.417Z",
        "status": 0,
        "paymentRefSumChanges": [
          {
            "actionType": 0,
            "newAmount": 1000,
            "reason": "Согласно дополнению аккредитива №01 от 10.10.2017г",
            "bankAccountant": "Иванов И.И.",
            "bankManager": "Иванов И.И.",
            "serialNo": 1,
            "userName": "Иванов И.И.",
            "mainBranchUserName": "Иванов И.И.",
            "insTime": "2020-06-04T04:33:06.417Z",
            "status": 0,
            "confirmations": [
              {
                "userName": "Иванов И.И.",
                "confirm": 0,
                "orgType": 1,
                "reason": "введен по ошибке",
                "date": "2020-06-04T04:33:06.417Z",
                "actionType": 1
              }
            ]
          }
        ],
        "confirmations": [
          {
            "userName": "Иванов И.И.",
            "confirm": 0,
            "orgType": 1,
            "reason": "введен по ошибке",
            "date": "2020-06-04T04:33:06.417Z",
            "actionType": 1
          }
        ]
      }
    ],
    "taxes": [
      {
        "actionType": 0,
        "docDate": "2025-10-23T12:35:04.046Z",
        "countryCode": "840",
        "currCode": "840",
        "amount": 1000,
        "currRate1": 1.02,
        "currRate2": 1.02,
        "amount1": 1000,
        "amount2": 1000,
        "docName": "310",
        "serialNo": 1,
        "userName": "Иванов И.И.",
        "rateType1": 0,
        "rateType2": 0,
        "insTime": "2020-06-04T04:33:06.417Z",
        "status": 0,
        "confirmations": [
          {
            "userName": "Иванов И.И.",
            "confirm": 0,
            "orgType": 1,
            "reason": "введен по ошибке",
            "date": "2020-06-04T04:33:06.417Z",
            "actionType": 1
          }
        ]
      }
    ],
    "compensations": [
      {
        "actionType": 0,
        "policyNo": "14/00/00-1905-02",
        "currCode": "860",
        "amount": 1000,
        "docDate": "2025-10-23T12:35:04.046Z",
        "currRate1": 1.0419,
        "currRate2": 1.0419,
        "amount1": 1000,
        "amount2": 1000,
        "serialNo": 1,
        "userName": "Иванов И.И.",
        "rateType1": 0,
        "rateType2": 0,
        "insuranceCompany": "GROSS INSURANCE OOO",
        "insTime": "2020-06-04T04:33:06.417Z",
        "status": 0,
        "confirmations": [
          {
            "userName": "Иванов И.И.",
            "confirm": 0,
            "orgType": 1,
            "reason": "введен по ошибке",
            "date": "2020-06-04T04:33:06.417Z",
            "actionType": 1
          }
        ]
      }
    ],
    "sales": [
      {
        "actionType": 0,
        "saleType": 3,
        "requirementNo": "31/12",
        "requirementDate": "2025-10-23T12:35:04.046Z",
        "requirementCurrCode": "860",
        "requirementAmount": 1000,
        "taxAgencyCode": "0301",
        "saleSourceCode": 1,
        "fundSerialNo": 1,
        "benefitsInfoCode": 5,
        "benefitsType": 2,
        "partialExemptionReason": "string",
        "saleReasons": [
          {
            "code": 1,
            "sum": 1000
          }
        ],
        "saleReasonType": 1,
        "normativeDocName": "О мерах по обеспечению ускоренного развития туристской отрасли Рес Узбекистан",
        "normativeDocNo": "УП-4861",
        "normativeDocDate": "2025-10-23T12:35:04.046Z",
        "otherReason": "возмещение расходов",
        "freeAmount": 1000,
        "commissionAmount": 1000,
        "amount": 1000,
        "docDate": "2025-10-23T12:35:04.046Z",
        "serialNo": 1,
        "userName": "Иванов И.И.",
        "moveFromExSerialNo": 1,
        "compensationSerialNo": 1,
        "remark": "00610 50% обязательная продажа",
        "insTime": "2020-06-04T04:33:06.417Z",
        "status": 0,
        "confirmations": [
          {
            "userName": "Иванов И.И.",
            "confirm": 0,
            "orgType": 1,
            "reason": "введен по ошибке",
            "date": "2020-06-04T04:33:06.417Z",
            "actionType": 1
          }
        ]
      }
    ],
    "leasePayments": [
      {
        "actionType": 0,
        "docDate": "2025-10-23T12:35:04.046Z",
        "currCode": "860",
        "amount": 1000,
        "serialNo": 1,
        "userName": "Иванов И.И.",
        "insTime": "2020-06-04T04:33:06.417Z",
        "status": 0,
        "confirmations": [
          {
            "userName": "Иванов И.И.",
            "confirm": 0,
            "orgType": 1,
            "reason": "введен по ошибке",
            "date": "2020-06-04T04:33:06.417Z",
            "actionType": 1
          }
        ]
      }
    ],
    "payments": [
      {
        "actionType": 0,
        "docDate": "2025-10-23T12:35:04.046Z",
        "paymentSourceType": 1,
        "centralBankDscnNo": "352-2",
        "centralBankDscnDate": "2025-10-23T12:35:04.046Z",
        "bankForName": "АО \"Народный Банк Казахстана\"",
        "fundSourceType": 1,
        "fundType": 3,
        "loanType": 1,
        "otherLoan": "Другие типы заемщика",
        "conditionType": 2,
        "letterOfCreditNo": "459210918111304",
        "guaranteeNo": "GC1783916002896",
        "currCode": "860",
        "amount": 1000,
        "currRate": 1.0419,
        "goodsAmount1": 1000,
        "goodsAmount2": 1000,
        "serviceAmount1": 1000,
        "serviceAmount2": 1000,
        "serialNo": 1,
        "docNo": "6210YSP000000014",
        "userName": "Иванов И.И.",
        "rateType": 0,
        "bankAccountant": "Иванов И.И.",
        "countryCode": "840",
        "offshoreCode": "112",
        "insTime": "2020-06-04T04:33:06.417Z",
        "status": 0,
        "confirmations": [
          {
            "userName": "Иванов И.И.",
            "confirm": 0,
            "orgType": 1,
            "reason": "введен по ошибке",
            "date": "2020-06-04T04:33:06.417Z",
            "actionType": 1
          }
        ]
      }
    ],
    "refundsImp": [
      {
        "actionType": 0,
        "docDate": "2025-10-23T12:35:04.046Z",
        "paymentSourceType": 1,
        "paymentSerialNo": 1,
        "currCode": "860",
        "amount": 1000,
        "currRate": 1.09,
        "goodsAmount1": 1000,
        "goodsAmount2": 1000,
        "serviceAmount1": 1000,
        "serviceAmount2": 1000,
        "serialNo": 1,
        "moveFromImSerialNo": 1,
        "accCurrRate": 1.09,
        "userName": "Иванов И.И.",
        "rateType": 0,
        "bankAccountant": "Иванов И.И.",
        "accRateType": 0,
        "accAmount": 1000,
        "docNo": "30000000000000",
        "cashFlag": 0,
        "insTime": "2020-06-04T04:33:06.417Z",
        "status": 0,
        "confirmations": [
          {
            "userName": "Иванов И.И.",
            "confirm": 0,
            "orgType": 1,
            "reason": "введен по ошибке",
            "date": "2020-06-04T04:33:06.417Z",
            "actionType": 1
          }
        ]
      }
    ],
    "movesToIm": [
      {
        "actionType": 0,
        "paymentSerialNo": 1,
        "sourceAmount": 1000,
        "currRate": 1.02,
        "sourceGoodsAmount1": 1000,
        "sourceGoodsAmount2": 1000,
        "sourceServiceAmount1": 1000,
        "sourceServiceAmount2": 1000,
        "destinationIdn": "207179114204743049190200005",
        "fundSourceType": 1,
        "fundType": 1,
        "loanType": 2,
        "conditionType": 0,
        "letterOfCreditNo": "459210918111304",
        "guaranteeNo": "GC1783916002896",
        "destinationCurrCode": "860",
        "currRate12": 1.02,
        "destinationAmount": 1000,
        "accCurrRate": 1.02,
        "destinationGoodsAmount1": 1000,
        "destinationGoodsAmount2": 1000,
        "destinationServiceAmount1": 1000,
        "destinationServiceAmount2": 1000,
        "reasonType": 5,
        "sourceAgrmNo": "1",
        "destinationAgrmNo": "1",
        "courtName": "Верховный суд Республики Узбекистан",
        "courtDscnNo": "2017/11-00",
        "courtDscnDate": "2025-10-23T12:35:04.046Z",
        "otherReason": "Основание письмо АГМК №07-1126 от 04,12,2018 г",
        "serialNo": 1,
        "docDate": "2025-10-23T12:35:04.046Z",
        "paymentSourceType": 1,
        "bankForName": "TD BANK N.A.",
        "otherLoan": "Другие типы заемщика",
        "centralBankDscnNo": "352-2",
        "centralBankDscnDate": "2025-10-23T12:35:04.046Z",
        "userName": "Иванов И.И.",
        "rateType": 0,
        "rateType12": 0,
        "accRateType": 0,
        "destinationMoveFromImSerialNo": 1,
        "insTime": "2020-06-04T04:33:06.417Z",
        "status": 0,
        "confirmations": [
          {
            "userName": "Иванов И.И.",
            "confirm": 0,
            "orgType": 1,
            "reason": "введен по ошибке",
            "date": "2020-06-04T04:33:06.417Z",
            "actionType": 1
          }
        ]
      }
    ],
    "movesFromIm": [
      {
        "docDate": "2025-10-23T12:35:04.046Z",
        "sourceIdn": "207179114204743049190200005",
        "paymentSourceType": 1,
        "centralBankDscnNo": "352-2",
        "centralBankDscnDate": "2025-10-23T12:35:04.046Z",
        "bankForName": "TD BANK N.A.",
        "fundSourceType": 1,
        "fundType": 1,
        "loanType": 2,
        "otherLoan": "Другие типы заемщика",
        "conditionType": 0,
        "letterOfCreditNo": "459210918111304",
        "guaranteeNo": "GC1783916002896",
        "currCode": "860",
        "currRate": 1.0419,
        "amount": 1000,
        "goodsAmount1": 1000,
        "goodsAmount2": 1000,
        "serviceAmount1": 1000,
        "serviceAmount2": 1000,
        "reasonType": 0,
        "agrmNo": "1",
        "courtName": "Верховный суд Республики Узбекистан",
        "courtDscnNo": "2017/11-00",
        "courtDscnDate": "2025-10-23T12:35:04.046Z",
        "otherReason": "Основание письмо АГМК №07-1126 от 04,12,2018 г",
        "serialNo": 1,
        "userName": "Иванов И.И.",
        "paymentDate": "2025-10-23T12:35:04.046Z",
        "rateType": 0,
        "sourceMoveToImSerialNo": 1,
        "insTime": "2020-06-04T04:33:06.417Z",
        "status": 0,
        "confirmations": [
          {
            "userName": "Иванов И.И.",
            "confirm": 0,
            "orgType": 1,
            "reason": "введен по ошибке",
            "date": "2020-06-04T04:33:06.417Z",
            "actionType": 1
          }
        ]
      }
    ],
    "exchangePayments": [
      {
        "docDate": "2025-10-23T12:35:04.046Z",
        "amountSoum": 1000,
        "currRate1": 1.0419,
        "currRate2": 1.0419,
        "goodsAmount1": 1000,
        "goodsAmount2": 1000,
        "docNo": "1680",
        "userName": "Иванов И.И.",
        "serviceAmount1": 1000,
        "serviceAmount2": 1000,
        "serialNo": 1,
        "insTime": "2020-06-04T04:33:06.417Z",
        "status": 0
      }
    ]
  }
}

```

</details>
</details>

<!--END ПОЛУЧИТЬ ИСТОРИЮ КОНТРАКТА -->

<details open>
<summary><b>ПОЛУЧИТЬ КОНТРАКТ ПО ДЕТАЛЯМ</b> (нажмите, чтобы открыть или скрыть)</summary>

<a id="localhost-7100-api-v1-core-contr-get"></a>


#### localhost-7100/api/v1/contract/get

<b>Method name:POST</b>

<details open>
<summary><b>ЗАПРОС</b></summary>

```json
{
  "bankInn": "*********",
  "bankId": "*****",
  "idn": "***********************"
}

```

</details>
<details open>
<summary><b>ОТВЕТ</b></summary>

```json
{
  "status": 0,
  "errorMsg": "OK",
  "timeStamp": "2025-10-23T12:40:40.904Z",
  "gtkId": 15021918,
  "contract": {
    "idn": "207179114204743049190200005",
    "rootIdn": "201052966302916241219800003",
    "cntrType": "02",
    "oldIdn": "207179114204743049190200005",
    "bankId": "00898",
    "bankInn": "207179114",
    "mvesNo": "26600702",
    "mvesDate": "2025-10-23T12:40:40.904Z",
    "govDcsnNo": "ПП-2313",
    "govDcsnDate": "2025-10-23T12:40:40.904Z",
    "okpo": "02906897",
    "okonx": "20.30.0",
    "contractorUzParentCompany": "Вышестоящая организация отсутсвует",
    "contractorUzName": "GAMMA COLOR SERVICE OOO",
    "rgnCode": "27",
    "industrialZoneCode": 3,
    "contractorUzAddress": "27",
    "contractorUzPhone": "998901234567",
    "cntrSubject": 1,
    "currCode1": "860",
    "currCode2": "860",
    "amount1": 1000,
    "amount2": 1000,
    "accCurrCode1": "860",
    "accCurrCode2": "860",
    "contractorForName": "ТОО \"Sercan Investment Group\"",
    "contractorForCountryCode": "398",
    "contractorForAddress": "Республика Казахстан, Восточно-Казахстанская обл., Усть-Каменогорск, Самарское шоссе, 17",
    "bankForName": "АО \"Народный Банк Казахстана\"",
    "bankForCountryCode": "398",
    "bankForAddress": "Республика Казахстан",
    "bankForAttributes": "Р/С:KZ516010151000230954 БИКHSBKKZKX Cor.ac.:№8900372605 SWIFT BIC:IRVTUS3NXXX",
    "centralBankLetterNo": "222",
    "centralBankLetterDate": "2025-10-23T12:40:40.904Z",
    "creditRate": 1,
    "leasingFlag": 1,
    "productsName": "фланцевая заглушка для магистральных трубопровод",
    "docNo": "EX-41/2016",
    "docDate": "2025-10-23T12:40:40.904Z",
    "execType": 2,
    "execDate": "2025-10-23T12:40:40.904Z",
    "reestrRegNo": "0810016",
    "softwareName": "программное обеспечение",
    "cntrStatus": 0,
    "lockDate": "2025-10-23T12:40:40.904Z",
    "status": 0,
    "cstDate": "2025-10-23T12:40:40.904Z",
    "contractorUzInn": "202328794",
    "receivableTotal1": 1000,
    "receivableOverdue1": 1000,
    "receivableTotal2": 1000,
    "receivableOverdue2": 1000,
    "payableTotal1": 1000,
    "payableOverdue1": 1000,
    "payableTotal2": 1000,
    "payableOverdue2": 1000,
    "insTime": "2020-06-04T04:33:06.417Z",
    "specifications": [
      {
        "docDate": "2025-10-23T12:40:40.904Z",
        "amount": 1000,
        "agrmNo": "1",
        "currCode": "860",
        "cstDate": "2025-10-23T12:40:40.904Z",
        "docType": 0,
        "docNo": "1",
        "imExFlag": 1,
        "serialNo": 1,
        "insTime": "2020-06-04T04:33:06.417Z",
        "oldNo": "1",
        "status": 0,
        "confirmations": [
          {
            "userName": "Иванов И.И.",
            "confirm": 0,
            "orgType": 1,
            "reason": "введен по ошибке",
            "date": "2020-06-04T04:33:06.417Z",
            "actionType": 1
          }
        ],
        "goods": [
          {
            "tnCode": "0101210000",
            "itemsName": "Картофель продовольственный",
            "serialNo": 1,
            "unitCode": "796",
            "quantity": 50,
            "currCode": "860",
            "cost": 1000,
            "amount": 1000,
            "insTime": "2020-06-04T04:33:06.417Z",
            "status": 0
          }
        ],
        "fare": {
          "currCode": "860",
          "amount": 1000,
          "insTime": "2020-06-04T04:33:06.417Z",
          "status": 0
        },
        "services": [
          {
            "serialNo": 1,
            "executorName": "ЧП \"SAIF-OPTIMA\"",
            "executorCountryCode": "860",
            "executionCountryCode": "860",
            "serviceName": "Переработка зерна в муку",
            "serviceType": "12",
            "currCode": "860",
            "amount": 1000,
            "selfMadeFlag": 1,
            "insTime": "2020-06-04T04:33:06.417Z",
            "status": 0
          }
        ]
      }
    ],
    "performedServices": [
      {
        "docDate": "2025-10-23T12:40:40.904Z",
        "docNo": "479657",
        "docType": 1,
        "currCode1": "860",
        "amount1": 1000,
        "amount2": 1000,
        "currCode2": "860",
        "insTime": "2020-06-04T04:33:06.417Z",
        "imExFlag": 1,
        "status": 0,
        "confirmations": [
          {
            "userName": "Иванов И.И.",
            "confirm": 0,
            "orgType": 1,
            "reason": "введен по ошибке",
            "date": "2020-06-04T04:33:06.417Z",
            "actionType": 1
          }
        ],
        "services": [
          {
            "serialNo": 1,
            "executorName": "ЧП \"SAIF-OPTIMA\"",
            "executorCountryCode": "860",
            "executionCountryCode": "860",
            "serviceName": "Переработка зерна в муку",
            "serviceType": "12",
            "currCode": "860",
            "amount": 1000,
            "selfMadeFlag": 1,
            "insTime": "2020-06-04T04:33:06.417Z",
            "status": 0
          }
        ],
        "goods": [
          {
            "tnCode": "0101210000",
            "itemsName": "Картофель продовольственный",
            "serialNo": 1,
            "unitCode": "796",
            "quantity": 50,
            "currCode": "860",
            "cost": 1000,
            "amount": 1000,
            "insTime": "2020-06-04T04:33:06.417Z",
            "status": 0
          }
        ]
      }
    ],
    "consignors": [
      {
        "serialNo": 1,
        "name": "ООО \"Техномолпром\"",
        "countryCode": "840",
        "inn": "206013203",
        "insTime": "2020-06-04T04:33:06.417Z",
        "status": 0,
        "confirmations": [
          {
            "userName": "Иванов И.И.",
            "confirm": 0,
            "orgType": 1,
            "reason": "введен по ошибке",
            "date": "2020-06-04T04:33:06.417Z",
            "actionType": 1
          }
        ]
      }
    ],
    "consignees": [
      {
        "serialNo": 1,
        "name": "ООО \"Техномолпром\"",
        "countryCode": "840",
        "inn": "206013203",
        "insTime": "2020-06-04T04:33:06.417Z",
        "status": 0,
        "confirmations": [
          {
            "userName": "Иванов И.И.",
            "confirm": 0,
            "orgType": 1,
            "reason": "введен по ошибке",
            "date": "2020-06-04T04:33:06.417Z",
            "actionType": 1
          }
        ]
      }
    ],
    "manufacturers": [
      {
        "serialNo": 1,
        "name": "ООО \"Техномолпром\"",
        "countryCode": "840",
        "inn": "206013203",
        "insTime": "2020-06-04T04:33:06.417Z",
        "status": 0,
        "confirmations": [
          {
            "userName": "Иванов И.И.",
            "confirm": 0,
            "orgType": 1,
            "reason": "введен по ошибке",
            "date": "2020-06-04T04:33:06.417Z",
            "actionType": 1
          }
        ]
      }
    ],
    "deliveryTerms": [
      {
        "destination": "Янгиюль, Узбекистан",
        "incotermsCode": "02",
        "agrmNo": "1",
        "serialNo": 1,
        "insTime": "2020-06-04T04:33:06.417Z",
        "status": 0,
        "confirmations": [
          {
            "userName": "Иванов И.И.",
            "confirm": 0,
            "orgType": 1,
            "reason": "введен по ошибке",
            "date": "2020-06-04T04:33:06.417Z",
            "actionType": 1
          }
        ]
      }
    ],
    "financeInfos": [
      {
        "companyForName": "\"THE ALPINE COMMERCE GROUP LLC\"",
        "companyForCountryCode": "860",
        "companyForAddress": "11 Peace Valley Rd, Towaco, New Jersey,07082 USA",
        "bankForName": "TD BANK N.A.",
        "bankForCountryCode": "840",
        "bankForAddress": "430 North Beverwyck Road Parsippany, NJ 07054",
        "bankForAttributes": "4308134115SWIFT NUMBER:NRTHUS33XXX",
        "agrmNo": "1",
        "serialNo": 1,
        "insTime": "2020-06-04T04:33:06.417Z",
        "status": 0,
        "confirmations": [
          {
            "userName": "Иванов И.И.",
            "confirm": 0,
            "orgType": 1,
            "reason": "введен по ошибке",
            "date": "2020-06-04T04:33:06.417Z",
            "actionType": 1
          }
        ]
      }
    ],
    "agreements": [
      {
        "docNo": "1",
        "docDate": "2025-10-23T12:40:40.904Z",
        "mvesDate": "2025-10-23T12:40:40.904Z",
        "agrmSubject": "Изменение спецификации и условий контракта",
        "currCode1": "860",
        "currCode2": "860",
        "accCurrCode1": "860",
        "accCurrCode2": "860",
        "contractorForName": "ТОО \"Sercan Investment Group\"",
        "contractorForCountryCode": "398",
        "contractorForAddress": "Республика Казахстан, Восточно-Казахстанская обл., Усть-Каменогорск, Самарское шоссе, 17",
        "bankForName": "АО \"Народный Банк Казахстана\"",
        "bankForCountryCode": "398",
        "bankForAddress": "Республика Казахстан",
        "bankForAttributes": "Р/С:KZ516010151000230954 БИКHSBKKZKX Cor.ac.:№8900372605 SWIFT BIC:IRVTUS3NXXX",
        "contractorUzName": "ООО \"Adler Group Distibution\"",
        "rgnCode": "01",
        "contractorUzAddress": "РУз, Ташкентская область , букинский район, ул. Узбекистон, дом 3",
        "amount1": 1000,
        "amount2": 1000,
        "cntrSubject": 1,
        "productsName": "фланцевая заглушка для магистральных трубопровод",
        "execType": 2,
        "execDate": "2025-10-23T12:40:40.904Z",
        "creditRate": 1,
        "cstDate": "2025-10-23T12:40:40.904Z",
        "serialNo": 1,
        "insTime": "2020-06-04T04:33:06.417Z",
        "industrialZoneCode": 3,
        "status": 0,
        "confirmations": [
          {
            "userName": "Иванов И.И.",
            "confirm": 0,
            "orgType": 1,
            "reason": "введен по ошибке",
            "date": "2020-06-04T04:33:06.417Z",
            "actionType": 1
          }
        ]
      }
    ],
    "endContract": {
      "docDate": "2025-10-23T12:40:40.904Z",
      "remark": "в связи с отсутствием задолженности",
      "insTime": "2020-06-04T04:33:06.417Z",
      "status": 0
    },
    "exportTerms": [
      {
        "exportType": 1,
        "exportDeadline": "2025-10-23T12:40:40.904Z",
        "exportTerm": 120,
        "paymentTerm": 120,
        "paymentDeadline": "2025-10-23T12:40:40.904Z",
        "leasingDeadline": 3,
        "leasingRate": 1,
        "deadlineType": 1,
        "agrmNo": "1",
        "insTime": "2020-06-04T04:33:06.417Z",
        "status": 0,
        "confirmations": [
          {
            "userName": "Иванов И.И.",
            "confirm": 0,
            "orgType": 1,
            "reason": "введен по ошибке",
            "date": "2020-06-04T04:33:06.417Z",
            "actionType": 1
          }
        ]
      }
    ],
    "tollingTerms": [
      {
        "idCol": "string",
        "goodsTitle": "string",
        "currCode": "860",
        "currRate1": 1.09,
        "currRate2": 1.09,
        "serviceAmount1": 1000,
        "serviceAmount2": 1000,
        "docDate": "2025-10-23T12:40:40.904Z",
        "rateType1": 0,
        "rateType2": 0,
        "goodsAmount": 1000,
        "serialNo": 1,
        "insTime": "2020-06-04T04:33:06.417Z",
        "status": 0,
        "confirmations": [
          {
            "userName": "Иванов И.И.",
            "confirm": 0,
            "orgType": 1,
            "reason": "введен по ошибке",
            "date": "2020-06-04T04:33:06.417Z",
            "actionType": 1
          }
        ]
      }
    ],
    "importTerms": [
      {
        "paymentMethod": "04",
        "paymentTerm": 5,
        "paymentDeadline": "2025-10-23T12:40:40.904Z",
        "importTerm": 30,
        "importDeadline": "2025-10-23T12:40:40.904Z",
        "importTermAfterAccr": 90,
        "agrmNo": "1",
        "insTime": "2020-06-04T04:33:06.417Z",
        "status": 0,
        "confirmations": [
          {
            "userName": "Иванов И.И.",
            "confirm": 0,
            "orgType": 1,
            "reason": "введен по ошибке",
            "date": "2020-06-04T04:33:06.417Z",
            "actionType": 1
          }
        ]
      }
    ],
    "barterTerms": [
      {
        "paymentMethod": "06",
        "exportDeadline": "2025-10-23T12:40:40.904Z",
        "exportTerm": 10,
        "importTerm": 10,
        "paymentTerm": 10,
        "agrmNo": "1",
        "insTime": "2020-06-04T04:33:06.417Z",
        "status": 0,
        "confirmations": [
          {
            "userName": "Иванов И.И.",
            "confirm": 0,
            "orgType": 1,
            "reason": "введен по ошибке",
            "date": "2020-06-04T04:33:06.417Z",
            "actionType": 1
          }
        ]
      }
    ],
    "transfers": [
      {
        "transferType": 1,
        "agrmNo": "1",
        "newIdn": "207179114204743049190200005",
        "dstBankId": "01061",
        "dstBankInn": "201589828",
        "dstExchInn": "201589828",
        "remark": "Доп.Согл.№ 2 от 20.04.2017г.",
        "serialNo": 1,
        "srcBankUserName": "Иванов И.И.",
        "dstBankUserName": "Иванов И.И.",
        "srcBankConfirmationFlag": 1,
        "dstBankConfirmationFlag": 1,
        "dstExchName": "string",
        "insTime": "2020-06-04T04:33:06.417Z",
        "oldIdn": "207179114204743049190200005",
        "status": 0
      }
    ],
    "delegates": [
      {
        "reasonType": 1,
        "courtName": "Верховный суд Республики Узбекистан",
        "courtDscnNo": "2017/11-00",
        "courtDscnDate": "2025-10-23T12:40:40.904Z",
        "govDscnNo": "ПП-4388",
        "govDscnDate": "2025-10-23T12:40:40.904Z",
        "agrmNo": "1",
        "dstInn": "200837914",
        "dstBankId": "01061",
        "dstOkpo": "26825672",
        "dstOkonx": "20.30.0",
        "dstName": "Агрофирма \"DOLINA AGRO TRANZIT\"",
        "dstRgnCode": "30",
        "dstIndustrialZoneCode": 3,
        "dstAddress": "Таш. обл. Юкори. Чирчик. Пос. ИИК Ота Машино тракт",
        "dstPhone": "712072772",
        "remark": "Доп.Согл.№ 2 от 20.04.2017г.",
        "serialNo": 1,
        "srcBankConfirmationFlag": 1,
        "dstBankConfirmationFlag": 1,
        "srcBankUserName": "Иванов И.И.",
        "dstBankUserName": "Иванов И.И.",
        "insTime": "2020-06-04T04:33:06.417Z",
        "oldIdn": "207179114204743049190200005",
        "parentCompanyCode": "Хокимият г.Ташкента",
        "newIdn": "207179114204743049190200005",
        "status": 0,
        "dstBankInn": "201589828"
      }
    ],
    "tradingPlatforms": [
      {
        "serialNo": 1,
        "docNo": 1,
        "name": "Google",
        "url": "www.google.com",
        "countryCode": "840",
        "ownFlag": 1,
        "insTime": "2020-06-04T04:33:06.417Z",
        "status": 0,
        "confirmations": [
          {
            "userName": "Иванов И.И.",
            "confirm": 0,
            "orgType": 1,
            "reason": "введен по ошибке",
            "date": "2020-06-04T04:33:06.417Z",
            "actionType": 1
          }
        ]
      }
    ],
    "declarations": [
      {
        "g7a": "03015",
        "g7c": "0008999",
        "g7b": "2025-10-23T12:40:40.904Z",
        "g17": "840",
        "g24a": "21",
        "gdvipdate": "2025-10-23T12:40:40.904Z",
        "gc3date": "2025-10-23T12:40:40.904Z",
        "g2name": "ООО \"Водий Пахтаси\"",
        "g23": 9446.65,
        "g8name": "ООО Производственно-коммерческая компания ВИВА",
        "g9name": "ООО \"Водий Пахтаси\"",
        "g1": "ЭК10",
        "g22a": "840",
        "g22b": 1000,
        "g15": "840",
        "goods": [
          {
            "g32": 1,
            "g33": "3204110000",
            "g42": 1000,
            "g37c": "000",
            "g31name": "1)Вата хлопчатобумажная чесаная \"Люкс\", O'zDSt 1029:2003, в кипах -  вес: 20306 кг, ; 2)99 кипов;",
            "g31amount": 30,
            "g41": "166",
            "g38": 4400,
            "g37b": "00"
          }
        ]
      }
    ],
    "guarantees": [
      {
        "docNo": "GC1783916002896",
        "docStartDate": "2025-10-23T12:40:40.904Z",
        "currCode": "860",
        "amount": 1000,
        "docValDate": "2025-10-23T12:40:40.904Z",
        "docValDays": 120,
        "userName": "Иванов И.И.",
        "insTime": "2020-06-04T04:33:06.417Z",
        "status": 0,
        "guaranteeTimeChanges": [
          {
            "docValDate": "2025-10-23T12:40:40.904Z",
            "docValDays": 120,
            "newDocValDate": "2025-10-23T12:40:40.904Z",
            "newDocValDays": 150,
            "reasonType": 4,
            "serialNo": 1,
            "userName": "Иванов И.И.",
            "insTime": "2020-06-04T04:33:06.417Z",
            "status": 0
          }
        ],
        "guaranteeSumChanges": [
          {
            "amount": 1000,
            "newAmount": 1000,
            "reasonType": 4,
            "serialNo": 1,
            "userName": "Иванов И.И.",
            "insTime": "2020-06-04T04:33:06.417Z",
            "status": 0
          }
        ]
      }
    ],
    "lettersOfCredit": [
      {
        "docNo": "459210918111304",
        "docStartDate": "2025-10-23T12:40:40.904Z",
        "currCode": "860",
        "amount": 1000,
        "docValDays": 120,
        "userName": "Иванов И.И.",
        "insTime": "2020-06-04T04:33:06.417Z",
        "status": 0,
        "letterOfCreditSumChanges": [
          {
            "amount": 1000,
            "newAmount": 1000,
            "reasonType": 4,
            "serialNo": 1,
            "userName": "Иванов И.И.",
            "insTime": "2020-06-04T04:33:06.417Z",
            "status": 0
          }
        ],
        "letterOfCreditTimeChanges": [
          {
            "docValDays": 120,
            "newDocValDays": 150,
            "reasonType": 4,
            "serialNo": 1,
            "userName": "Иванов И.И.",
            "insTime": "2020-06-04T04:33:06.417Z",
            "status": 0
          }
        ]
      }
    ],
    "policies": [
      {
        "docNo": "14/00/00-1905-02",
        "docDate": "2025-10-23T12:40:40.904Z",
        "currCode": "860",
        "amount": 1000,
        "docValDate": "2025-10-23T12:40:40.904Z",
        "docValDays": 120,
        "userName": "Иванов И.И.",
        "insTime": "2020-06-04T04:33:06.417Z",
        "status": 0,
        "policySumChanges": [
          {
            "amount": 1000,
            "newAmount": 1000,
            "reasonType": 4,
            "serialNo": 1,
            "userName": "Иванов И.И.",
            "insTime": "2020-06-04T04:33:06.417Z",
            "status": 0
          }
        ],
        "policyTimeChanges": [
          {
            "docValDate": "2025-10-23T12:40:40.904Z",
            "docValDays": 120,
            "newDocValDate": "2025-10-23T12:40:40.904Z",
            "newDocValDays": 150,
            "reasonType": 4,
            "serialNo": 1,
            "userName": "Иванов И.И.",
            "insTime": "2020-06-04T04:33:06.417Z",
            "status": 0
          }
        ]
      }
    ],
    "receivableDrops": [
      {
        "currCode": "860",
        "amount": 1000,
        "reasonType": 1,
        "serialNo": 1,
        "docDate": "2025-10-23T12:40:40.904Z",
        "userName": "Иванов И.И.",
        "insTime": "2020-06-04T04:33:06.417Z",
        "status": 0
      }
    ],
    "payableDrops": [
      {
        "currCode": "860",
        "amount": 1000,
        "reasonType": 4,
        "serialNo": 1,
        "docDate": "2025-10-23T12:40:40.904Z",
        "userName": "Иванов И.И.",
        "insTime": "2020-06-04T04:33:06.417Z",
        "status": 0
      }
    ],
    "penalties": [
      {
        "penaltyType": 1,
        "docDate": "2025-10-23T12:40:40.904Z",
        "currCode": "860",
        "amount": 1000,
        "serialNo": 1,
        "userName": "Иванов И.И.",
        "insTime": "2020-06-04T04:33:06.417Z",
        "status": 0
      }
    ],
    "commissions": [
      {
        "paymentSerialNo": 1,
        "amount": 1000,
        "serialNo": 1,
        "userName": "Иванов И.И.",
        "fundSerialNo": 1,
        "insTime": "2020-06-04T04:33:06.417Z",
        "status": 0
      }
    ],
    "taxAgencyPenalties": [
      {
        "docDate": "2025-10-23T12:40:40.904Z",
        "amountSoum": 1000,
        "serialNo": 1,
        "userName": "Иванов И.И.",
        "insTime": "2020-06-04T04:33:06.417Z",
        "status": 0
      }
    ],
    "receivableInfos": [
      {
        "tempImpCurrCode": "860",
        "tempImpAmount": 1000,
        "customsMode": 1,
        "remark": "string",
        "serialNo": 1,
        "userName": "Иванов И.И.",
        "insTime": "2020-06-04T04:33:06.417Z",
        "status": 0
      }
    ],
    "funds": [
      {
        "serialNo": 1,
        "docNo": "30000000000000",
        "docDate": "2025-10-23T12:40:40.904Z",
        "conditionType": 1,
        "currCode": "860",
        "amount": 1000,
        "currRate": 1.0419,
        "goodsAmount1": 1000,
        "goodsAmount2": 1000,
        "serviceAmount1": 1000,
        "serviceAmount2": 1000,
        "userName": "Иванов И.И.",
        "rateType": 0,
        "insTime": "2020-06-04T04:33:06.417Z",
        "status": 0
      }
    ],
    "refundsExp": [
      {
        "docDate": "2025-10-23T12:40:40.904Z",
        "fundSerialNo": 1,
        "amount": 1000,
        "serialNo": 1,
        "moveFromExSerialNo": 1,
        "userName": "Иванов И.И.",
        "docNo": "30000000000000",
        "insTime": "2020-06-04T04:33:06.417Z",
        "status": 0
      }
    ],
    "movesToEx": [
      {
        "fundSerialNo": 1,
        "sourceAmount": 1000,
        "destinationIdn": "207179114204743049190200005",
        "docDate": "2025-10-23T12:40:40.904Z",
        "serialNo": 1,
        "userName": "Иванов И.И.",
        "insTime": "2020-06-04T04:33:06.417Z",
        "status": 0
      }
    ],
    "movesFromEx": [
      {
        "docDate": "2025-10-23T12:40:40.904Z",
        "sourceIdn": "207179114204743049190200005",
        "currCode": "860",
        "amount": 1000,
        "serialNo": 1,
        "userName": "Иванов И.И.",
        "insTime": "2020-06-04T04:33:06.417Z",
        "status": 0,
        "currRate": 1.0419,
        "goodsAmount1": 1000,
        "goodsAmount2": 1000,
        "serviceAmount1": 1000,
        "serviceAmount2": 1000,
        "rateType": 0
      }
    ],
    "paymentRefs": [
      {
        "docNo": "00873/13.10.16/00002",
        "docDate": "2025-10-23T12:40:40.904Z",
        "reasonType": 1,
        "fundSerialNo": 1,
        "letterOfCreditNo": "459210918111304",
        "guaranteeNo": "GC1783916002896",
        "policyNo": "14/00/00-1905-02",
        "amount": 1000,
        "currCode": "860",
        "unusedAmount": 1000,
        "docValDate": "2025-10-23T12:40:40.904Z",
        "userName": "Иванов И.И.",
        "moveFromExSerialNo": 1,
        "insTime": "2020-06-04T04:33:06.417Z",
        "status": 0,
        "paymentRefSumChanges": [
          {
            "newAmount": 1000,
            "reason": "Согласно дополнению аккредитива №01 от 10.10.2017г",
            "serialNo": 1,
            "userName": "Иванов И.И.",
            "mainBranchUserName": "Иванов И.И.",
            "insTime": "2020-06-04T04:33:06.417Z",
            "status": 0
          }
        ]
      }
    ],
    "taxes": [
      {
        "docDate": "2025-10-23T12:40:40.904Z",
        "countryCode": "840",
        "currCode": "840",
        "amount": 1000,
        "serialNo": 1,
        "userName": "Иванов И.И.",
        "insTime": "2020-06-04T04:33:06.417Z",
        "status": 0
      }
    ],
    "compensations": [
      {
        "policyNo": "14/00/00-1905-02",
        "currCode": "860",
        "amount": 1000,
        "docDate": "2025-10-23T12:40:40.904Z",
        "serialNo": 1,
        "userName": "Иванов И.И.",
        "insTime": "2020-06-04T04:33:06.417Z",
        "status": 0
      }
    ],
    "sales": [
      {
        "saleType": 3,
        "fundSerialNo": 1,
        "benefitsInfoCode": 5,
        "amount": 1000,
        "docDate": "2025-10-23T12:40:40.904Z",
        "serialNo": 1,
        "userName": "Иванов И.И.",
        "moveFromExSerialNo": 1,
        "compensationSerialNo": 1,
        "insTime": "2020-06-04T04:33:06.417Z",
        "status": 0
      }
    ],
    "leasePayments": [
      {
        "docDate": "2025-10-23T12:40:40.904Z",
        "currCode": "860",
        "amount": 1000,
        "serialNo": 1,
        "userName": "Иванов И.И.",
        "insTime": "2020-06-04T04:33:06.417Z",
        "status": 0
      }
    ],
    "payments": [
      {
        "docDate": "2025-10-23T12:40:40.904Z",
        "fundSourceType": 1,
        "currCode": "860",
        "amount": 1000,
        "serialNo": 1,
        "docNo": "6210YSP000000014",
        "userName": "Иванов И.И.",
        "insTime": "2020-06-04T04:33:06.417Z",
        "status": 0,
        "currRate": 1.0419,
        "goodsAmount1": 1000,
        "goodsAmount2": 1000,
        "serviceAmount1": 1000,
        "serviceAmount2": 1000,
        "rateType": 0
      }
    ],
    "refundsImp": [
      {
        "docDate": "2025-10-23T12:40:40.904Z",
        "currCode": "860",
        "amount": 1000,
        "serialNo": 1,
        "userName": "Иванов И.И.",
        "docNo": "30000000000000",
        "insTime": "2020-06-04T04:33:06.417Z",
        "status": 0
      }
    ],
    "movesToIm": [
      {
        "paymentSerialNo": 1,
        "sourceAmount": 1000,
        "destinationIdn": "207179114204743049190200005",
        "serialNo": 1,
        "docDate": "2025-10-23T12:40:40.904Z",
        "userName": "Иванов И.И.",
        "insTime": "2020-06-04T04:33:06.417Z",
        "status": 0
      }
    ],
    "movesFromIm": [
      {
        "docDate": "2025-10-23T12:40:40.904Z",
        "sourceIdn": "207179114204743049190200005",
        "currCode": "860",
        "amount": 1000,
        "serialNo": 1,
        "userName": "Иванов И.И.",
        "insTime": "2020-06-04T04:33:06.417Z",
        "status": 0,
        "currRate": 1.0419,
        "goodsAmount1": 1000,
        "goodsAmount2": 1000,
        "serviceAmount1": 1000,
        "serviceAmount2": 1000,
        "rateType": 0
      }
    ],
    "exchangePayments": [
      {
        "docDate": "2025-10-23T12:40:40.904Z",
        "amountSoum": 1000,
        "docNo": "391",
        "userName": "Иванов И.И.",
        "serialNo": 1,
        "insTime": "2020-06-04T04:33:06.417Z",
        "status": 0
      }
    ]
  }
}

```

</details>
</details>

<!--END ПОЛУЧИТЬ КОНТРАКТ ПО ДЕТАЛЯМ -->



<details open>
<summary><b>ПОЛУЧИТЬ БАНКОВСКИЕ ДОКУМЕНТЫ</b> (нажмите, чтобы открыть или скрыть)</summary>

<a id="localhost-7100-api-v1-core-contr-get-bank-docs"></a>


#### localhost-7100/api/v1/contract/get-bank-docs

<b>Method name:POST</b>

<details open>
<summary><b>ЗАПРОС</b></summary>

```json
{
  "bankInn": "*********",
  "bankId": "*****",
  "idn": "***********************"
}

```

</details>
<details open>
<summary><b>ОТВЕТ</b></summary>

```json
{
  "status": 0,
  "errorMsg": "OK",
  "timeStamp": "2025-10-23T12:45:17.495Z",
  "gtkId": 15021918,
  "contract": {
    "guarantees": [
      {
        "docNo": "**********",
        "docStartDate": "2025-10-23T12:45:17.495Z",
        "currCode": "***",
        "amount": 1000,
        "docValDate": "2025-10-23T12:45:17.495Z",
        "docValDays": 120,
        "userName": "Иванов И.И.",
        "insTime": "2020-06-04T04:33:06.417Z",
        "status": 0,
        "guaranteeTimeChanges": [
          {
            "docValDate": "2025-10-23T12:45:17.495Z",
            "docValDays": 120,
            "newDocValDate": "2025-10-23T12:45:17.495Z",
            "newDocValDays": 150,
            "reasonType": 4,
            "serialNo": 1,
            "userName": "Иванов И.И.",
            "insTime": "2020-06-04T04:33:06.417Z",
            "status": 0
          }
        ],
        "guaranteeSumChanges": [
          {
            "amount": 1000,
            "newAmount": 1000,
            "reasonType": 4,
            "serialNo": 1,
            "userName": "Иванов И.И.",
            "insTime": "2020-06-04T04:33:06.417Z",
            "status": 0
          }
        ]
      }
    ],
    "lettersOfCredit": [
      {
        "docNo": "459210918111304",
        "docStartDate": "2025-10-23T12:45:17.495Z",
        "currCode": "***",
        "amount": 1000,
        "docValDays": 120,
        "userName": "Иванов И.И.",
        "insTime": "2020-06-04T04:33:06.417Z",
        "status": 0,
        "letterOfCreditSumChanges": [
          {
            "amount": 1000,
            "newAmount": 1000,
            "reasonType": 4,
            "serialNo": 1,
            "userName": "Иванов И.И.",
            "insTime": "2020-06-04T04:33:06.417Z",
            "status": 0
          }
        ],
        "letterOfCreditTimeChanges": [
          {
            "docValDays": 120,
            "newDocValDays": 150,
            "reasonType": 4,
            "serialNo": 1,
            "userName": "Иванов И.И.",
            "insTime": "2020-06-04T04:33:06.417Z",
            "status": 0
          }
        ]
      }
    ],
    "policies": [
      {
        "docNo": "14/00/00-1905-02",
        "docDate": "2025-10-23T12:45:17.495Z",
        "currCode": "***",
        "amount": 1000,
        "docValDate": "2025-10-23T12:45:17.495Z",
        "docValDays": 120,
        "userName": "Иванов И.И.",
        "insTime": "2020-06-04T04:33:06.417Z",
        "status": 0,
        "policySumChanges": [
          {
            "amount": 1000,
            "newAmount": 1000,
            "reasonType": 4,
            "serialNo": 1,
            "userName": "Иванов И.И.",
            "insTime": "2020-06-04T04:33:06.417Z",
            "status": 0
          }
        ],
        "policyTimeChanges": [
          {
            "docValDate": "2025-10-23T12:45:17.495Z",
            "docValDays": 120,
            "newDocValDate": "2025-10-23T12:45:17.495Z",
            "newDocValDays": 150,
            "reasonType": 4,
            "serialNo": 1,
            "userName": "Иванов И.И.",
            "insTime": "2020-06-04T04:33:06.417Z",
            "status": 0
          }
        ]
      }
    ],
    "receivableDrops": [
      {
        "currCode": "***",
        "amount": 1000,
        "reasonType": 1,
        "serialNo": 1,
        "docDate": "2025-10-23T12:45:17.495Z",
        "userName": "Иванов И.И.",
        "insTime": "2020-06-04T04:33:06.417Z",
        "status": 0
      }
    ],
    "payableDrops": [
      {
        "currCode": "860",
        "amount": 1000,
        "reasonType": 4,
        "serialNo": 1,
        "docDate": "2025-10-23T12:45:17.495Z",
        "userName": "Иванов И.И.",
        "insTime": "2020-06-04T04:33:06.417Z",
        "status": 0
      }
    ],
    "penalties": [
      {
        "penaltyType": 1,
        "docDate": "2025-10-23T12:45:17.495Z",
        "currCode": "860",
        "amount": 1000,
        "serialNo": 1,
        "userName": "Иванов И.И.",
        "insTime": "2020-06-04T04:33:06.417Z",
        "status": 0
      }
    ],
    "commissions": [
      {
        "paymentSerialNo": 1,
        "amount": 1000,
        "serialNo": 1,
        "userName": "Иванов И.И.",
        "fundSerialNo": 1,
        "insTime": "2020-06-04T04:33:06.417Z",
        "status": 0
      }
    ],
    "taxAgencyPenalties": [
      {
        "docDate": "2025-10-23T12:45:17.495Z",
        "amountSoum": 1000,
        "serialNo": 1,
        "userName": "Иванов И.И.",
        "insTime": "2020-06-04T04:33:06.417Z",
        "status": 0
      }
    ],
    "receivableInfos": [
      {
        "tempImpCurrCode": "***",
        "tempImpAmount": 1000,
        "customsMode": 1,
        "remark": "string",
        "serialNo": 1,
        "userName": "Иванов И.И.",
        "insTime": "2020-06-04T04:33:06.417Z",
        "status": 0
      }
    ],
    "funds": [
      {
        "serialNo": 1,
        "docNo": "************",
        "docDate": "2025-10-23T12:45:17.495Z",
        "conditionType": 1,
        "currCode": "***",
        "amount": 1000,
        "currRate": 1.0419,
        "goodsAmount1": 1000,
        "goodsAmount2": 1000,
        "serviceAmount1": 1000,
        "serviceAmount2": 1000,
        "userName": "Иванов И.И.",
        "rateType": 0,
        "insTime": "2020-06-04T04:33:06.417Z",
        "status": 0
      }
    ],
    "refundsExp": [
      {
        "docDate": "2025-10-23T12:45:17.495Z",
        "fundSerialNo": 1,
        "amount": 1000,
        "serialNo": 1,
        "moveFromExSerialNo": 1,
        "userName": "Иванов И.И.",
        "docNo": "***********",
        "insTime": "2020-06-04T04:33:06.417Z",
        "status": 0
      }
    ],
    "movesToEx": [
      {
        "fundSerialNo": 1,
        "sourceAmount": 1000,
        "destinationIdn": "************",
        "docDate": "2025-10-23T12:45:17.495Z",
        "serialNo": 1,
        "userName": "Иванов И.И.",
        "insTime": "2020-06-04T04:33:06.417Z",
        "status": 0
      }
    ],
    "movesFromEx": [
      {
        "docDate": "2025-10-23T12:45:17.495Z",
        "sourceIdn": "***********",
        "currCode": "***",
        "amount": 1000,
        "serialNo": 0,
        "userName": "Иванов И.И.",
        "insTime": "2020-06-04T04:33:06.417Z",
        "status": 0,
        "currRate": 1.0419,
        "goodsAmount1": 1000,
        "goodsAmount2": 1000,
        "serviceAmount1": 1000,
        "serviceAmount2": 1000,
        "rateType": 0
      }
    ],
    "paymentRefs": [
      {
        "docNo": "00873/13.10.16/00002",
        "docDate": "2025-10-23T12:45:17.495Z",
        "reasonType": 1,
        "fundSerialNo": 1,
        "letterOfCreditNo": "************",
        "guaranteeNo": "**********",
        "policyNo": "14/00/00-1905-02",
        "amount": 1000,
        "currCode": "***",
        "unusedAmount": 1000,
        "docValDate": "2025-10-23T12:45:17.495Z",
        "userName": "Иванов И.И.",
        "moveFromExSerialNo": 1,
        "insTime": "2020-06-04T04:33:06.417Z",
        "status": 0,
        "paymentRefSumChanges": [
          {
            "newAmount": 1000,
            "reason": "Согласно дополнению аккредитива №01 от 10.10.2017г",
            "serialNo": 0,
            "userName": "Иванов И.И.",
            "mainBranchUserName": "Иванов И.И.",
            "insTime": "2020-06-04T04:33:06.417Z",
            "status": 0
          }
        ]
      }
    ],
    "taxes": [
      {
        "docDate": "2025-10-23T12:45:17.495Z",
        "countryCode": "***",
        "currCode": "***",
        "amount": 1000,
        "serialNo": 1,
        "userName": "Иванов И.И.",
        "insTime": "2020-06-04T04:33:06.417Z",
        "status": 0
      }
    ],
    "compensations": [
      {
        "policyNo": "14/00/00-1905-02",
        "currCode": "***",
        "amount": 1000,
        "docDate": "2025-10-23T12:45:17.495Z",
        "serialNo": 1,
        "userName": "Иванов И.И.",
        "insTime": "2020-06-04T04:33:06.417Z",
        "status": 0
      }
    ],
    "sales": [
      {
        "saleType": 3,
        "fundSerialNo": 1,
        "benefitsInfoCode": 5,
        "amount": 1000,
        "docDate": "2025-10-23T12:45:17.495Z",
        "serialNo": 1,
        "userName": "Иванов И.И.",
        "moveFromExSerialNo": 1,
        "compensationSerialNo": 1,
        "insTime": "2020-06-04T04:33:06.417Z",
        "status": 0
      }
    ],
    "leasePayments": [
      {
        "docDate": "2025-10-23T12:45:17.495Z",
        "currCode": "***",
        "amount": 1000,
        "serialNo": 1,
        "userName": "Иванов И.И.",
        "insTime": "2020-06-04T04:33:06.417Z",
        "status": 0
      }
    ],
    "payments": [
      {
        "docDate": "2025-10-23T12:45:17.495Z",
        "fundSourceType": 0,
        "currCode": "860",
        "amount": 1000,
        "serialNo": 0,
        "docNo": "************",
        "userName": "Иванов И.И.",
        "insTime": "2020-06-04T04:33:06.417Z",
        "status": 0,
        "currRate": 1.0419,
        "goodsAmount1": 1000,
        "goodsAmount2": 1000,
        "serviceAmount1": 1000,
        "serviceAmount2": 1000,
        "rateType": 0
      }
    ],
    "refundsImp": [
      {
        "docDate": "2025-10-23T12:45:17.495Z",
        "currCode": "***",
        "amount": 1000,
        "serialNo": 0,
        "userName": "Иванов И.И.",
        "docNo": "*********",
        "insTime": "2020-06-04T04:33:06.417Z",
        "status": 0
      }
    ],
    "movesToIm": [
      {
        "paymentSerialNo": 1,
        "sourceAmount": 1000,
        "destinationIdn": "******",
        "serialNo": 1,
        "docDate": "2025-10-23T12:45:17.495Z",
        "userName": "Иванов И.И.",
        "insTime": "2020-06-04T04:33:06.417Z",
        "status": 0
      }
    ],
    "movesFromIm": [
      {
        "docDate": "2025-10-23T12:45:17.495Z",
        "sourceIdn": "******",
        "currCode": "860",
        "amount": 1000,
        "serialNo": 0,
        "userName": "Иванов И.И.",
        "insTime": "2020-06-04T04:33:06.417Z",
        "status": 0,
        "currRate": 1.0419,
        "goodsAmount1": 1000,
        "goodsAmount2": 1000,
        "serviceAmount1": 1000,
        "serviceAmount2": 1000,
        "rateType": 0
      }
    ],
    "exchangePayments": [
      {
        "docDate": "2025-10-23T12:45:17.495Z",
        "amountSoum": 0,
        "docNo": "****",
        "userName": "Иванов И.И.",
        "serialNo": 1,
        "insTime": "2020-06-04T04:33:06.417Z",
        "status": 0
      }
    ]
  }
}

```

</details>
</details>
<!--END GET BANK DOCS -->


<details open>
<summary><b>ПОЛУЧИТЬ СПИСОК КОНТРАКТОВ (с обновлением и поиском)</b> (нажмите, чтобы открыть или скрыть)</summary>

<a id="localhost-7100-api-v1-core-contr-list"></a>


#### localhost-7100/api/v1/contract/list

<b>Method name:POST</b>

<details open>
<summary><b>ЗАПРОС</b></summary>

```json
{
  "bankInn": "*****",
  "bankId": "*****",
  "contractorUzInn": "*******",
  "isUpdate": "false",
  "idn": "",
  "docNo": "",
  "contractorUzName": "",
  "size": 100,
  "page": 0
}

```

</details>
<details open>
<summary><b>ОТВЕТ</b></summary>

```json
{
  "timestamp": null,
  "success": true,
  "result": {
    "code": 20200,
    "message": "OK",
    "audit": "d5da231b-1911-4e69-8cfe-74e456a1f164",
    "data": {
      "status": 0,
      "errorMsg": "OK",
      "timeStamp": null,
      "gtkId": null,
      "contracts": [
        {
          "idn": "******************",
          "bankInn": "*********",
          "bankId": "*****",
          "cntrStatus": 1,
          "contractorUzName": "GOODFROST",
          "docNo": "29/04",
          "docDate": "2019-03-29",
          "contractorForName": "ОООquot;ЕВРОТРАНСГРУППquot;",
          "contractorForCountryCode": "643",
          "update": true
        }
      ],
      "size": 100,
      "page": 0,
      "total": 1
    }
  }
}

```

</details>
</details>
<!--END ПОЛУЧИТЬ СПИСОК КОНТРАКТОВ -->


<details open>
<summary><b>ПОЛУЧИТЬ СПИСОК ИНН (Юридические Клиенты)</b> (нажмите, чтобы открыть или скрыть)</summary>

<a id="localhost-7100-api-v1-core-contr-get-inn-list"></a>


#### localhost-7100/api/v1/contract/get-inn-list

<b>Method name:POST</b>

<details open>
<summary><b>ЗАПРОС</b></summary>

```json
{
  "bankId": "*****",
  "active": "true"
}

```

</details>
<details open>
<summary><b>ОТВЕТ</b></summary>

```json
{
  "timestamp": "2025-10-23T16:29:40.888840994",
  "success": true,
  "result": {
    "code": 20200,
    "message": "OK",
    "audit": "2ce54f1f-65a6-4d19-b10b-474778159682",
    "data": {
      "total": 816,
      "data": [
        {
          "clientcbId": 0,
          "clientId": 0,
          "companyName": "МЧЖ \"BYORK\"",
          "companyInn": "*******",
          "statusClient": 2,
          "branchClient": "*****",
          "requestBankId": "*****",
          "requestActive": "true"
        }
      ]
    }
  }
}
```

</details>
</details>
<!--END ПОЛУЧИТЬ СПИСОК ИНН (Juridic Client) -->


</details>
<!--КОНЕЦ Contract Service-->





<details open>
<summary><b>API Справочника</b></summary>

<a id="mibcreditor-api"></a>

### Сервис Справочника


| No |                         API name                         |                                                          URL                                                           |
|:--:|:--------------------------------------------------------:|:----------------------------------------------------------------------------------------------------------------------:|
| 1  |         ПОЛУЧИТЬ СПРАВОЧНУЮ ИНФОРМАЦИЮ О СТРАНЕ          |                [localhost:7100/api/v1/dictionary/country](#localhost-7100/api/v1/contract/dic/country)                 |
| 2  |   ПОЛУЧИТЬ СПРАВОЧНУЮ ИНФОРМАЦИЮ О ПРЕДМЕТАХ ДОГОВОРА    |       [localhost:7100/api/v1/dictionary/contract-subject](#localhost-7100/api/v1/contract/dic/contract-subject)        |
| 3  |      ПОЛУЧИТЬ СПРАВОЧНУЮ ИНФОРМАЦИЮ О СТРАНЕ ПО ID       |           [localhost:7100/api/v1/dictionary/country/{{id}}](#localhost-7100/api/v1/contract/dic/get-country-by-id)           |
| 4  | ПОЛУЧИТЬ СПРАВОЧНУЮ ИНФОРМАЦИЮ О ПРЕДМЕТЕ ДОГОВОРА ПО ID |  [localhost:7100/api/v1/dictionary/contract-subject/{{id}}](#localhost-7100/api/v1/contract/dic/get-contract-subjects-by-id)  |
| 5  |         ПОЛУЧИТЬ СПРАВОЧНУЮ ИНФОРМАЦИЮ О РЕГИОНЕ         |                  [localhost:7100/api/v1/dictionary/region](#localhost-7100/api/v1/dictionary/region)                   |
| 6  |    ПОЛУЧИТЬ СПРАВОЧНУЮ ИНФОРМАЦИЮ О ПРОМЫШЛЕННОЙ ЗОНЕ    |         [localhost:7100/api/v1/dictionary/industrial-zone](#localhost-5028-api-v1-dictionary-industrial-zone)          |
| 7  |          ПОЛУЧИТЬ СПРАВОЧНУЮ ИНФОРМАЦИЮ О БАНКЕ          |               [localhost:7100/api/v1/dictionary/bank-info](#localhost-5028-api-v1-dictionary-bank-info)                |
| 8  | ПОЛУЧИТЬ СПРАВОЧНУЮ ИНФОРМАЦИЮ О ПРОМЫШЛЕННОЙ ЗОНЕ ПО ID |   [localhost:7100/api/v1/dictionary/industrial-zone/{{id}}](#localhost-5028-api-v1-dictionary-industrial-zone-by-id)   |
| 9  |      ПОЛУЧИТЬ СПРАВОЧНУЮ ИНФОРМАЦИЮ О РЕГИОНЕ ПО ID      |            [localhost:7100/api/v1/dictionary/region/{{id}}](#localhost-5028-api-v1-dictionary-region-by-id)            |
| 10 |      ПОЛУЧИТЬ СПРАВОЧНУЮ ИНФОРМАЦИЮ О БАНКЕ ПО ID        |          [localhost:7100/api/v1/dictionary/bank-info/{{id}}<br/>](#localhost-5028-api-v1-dictionary-o-by-id)           |

<details open>
<summary><b>ПОЛУЧИТЬ СПРАВОЧНУЮ ИНФОРМАЦИЮ О СТРАНЕ(</b>(click to expand or hide)</summary>

<a id="localhos-7100-api-v1-core-contr-dic-country"></a>

#### localhost-7100/api/v1/contract/dic/country

<b>Method name:POST</b>

<details open>
<summary><b>ЗАПРОС</b></summary>

```json
{
  "bankInn": "*********",
  "bankId": "*****"
}

```

</details>
<details open>
<summary><b>ОТВЕТ</b></summary>

```json
{
  "timestamp": null,
  "success": true,
  "result": {
    "code": 20200,
    "message": "OK",
    "audit": "85072ae1-bcc9-4532-bb85-7acb50bf2325",
    "data": {
      "status": 0,
      "errorMsg": "OK",
      "timeStamp": "2025-10-24T04:44:32.185+00:00",
      "gtkId": 1761281071671,
      "records": [
        {
          "idNumber": 1,
          "id": "000",
          "name": "НЕИЗВЕСТНА",
          "isDeleted": false
        }
      ]
    }
  }
}

```

</details>

</details>
<!--END ПОЛУЧИТЬ СПРАВОЧНУЮ ИНФОРМАЦИЮ О СТРАНЕ -->


<details open>
<summary><b>ПОЛУЧИТЬ СПРАВОЧНУЮ ИНФОРМАЦИЮ О ПРЕДМЕТАХ ДОГОВОРА</b>(click to expand or hide)</summary>

<a id="localhos-7100-api-v1-core-contr-dic-contract-subject"></a>

#### localhost-7100/api/v1/dictionary/contract-subject

<b>Method name:POST</b>

<details open>
<summary><b>ЗАПРОС</b></summary>

```json
{
  "bankInn": "*********",
  "bankId": "*****"
}

```

</details>
<details open>
<summary><b>ОТВЕТ</b></summary>

```json
{
  "timestamp": null,
  "success": true,
  "result": {
    "code": 20200,
    "message": "OK",
    "audit": "4f45c22c-c1a5-46d2-b698-676365d1810d",
    "data": {
      "status": 0,
      "errorMsg": "OK",
      "timeStamp": "2025-10-24T04:54:40.125+00:00",
      "gtkId": "************",
      "records": [
        {
          "idNumber": 1,
          "id": 1,
          "name": "товар",
          "isDeleted": false
        }
      ]
    }
  }
}
```

</details>

</details>
<!--END ПОЛУЧИТЬ СПРАВОЧНУЮ ИНФОРМАЦИЮ О ПРЕДМЕТАХ ДОГОВОРА -->



<details open>
<summary><b>ПОЛУЧИТЬ СТРАНУ ПО ID (справочник)</b> (нажмите, чтобы открыть или скрыть)</summary>

<a id="localhost-7100-api-v1-core-contr-dic-get-country-by-id"></a>

#### localhost-7100/api/v1/dictionary/get-country-by-id


<b>Method name:GET</b>

<details open>
<summary><b>ЗАПРОС</b></summary>

```request param
  id: "***"

```

</details>
<details open>
<summary><b>ОТВЕТ</b></summary>

```json
{
  "timestamp": null,
  "success": true,
  "result": {
    "code": 20200,
    "message": "OK",
    "audit": "5eafb2a2-3399-44db-b793-260567efcba0",
    "data": {
      "status": 0,
      "errorMsg": "OK",
      "timeStamp": null,
      "gtkId": null,
      "records": [
        {
          "idNumber": 2,
          "id": "***",
          "name": "АФГАНИСТАН",
          "isDeleted": false
        }
      ]
    }
  }
}
```

</details>

</details>
<!--END ПОЛУЧИТЬ СТРАНУ ПО ID -->



<details open>
<summary><b>ПОЛУЧИТЬ СУБЪЕКТЫ КОНТРАКТА ПО ID (справочник)</b> (нажмите, чтобы открыть или скрыть)</summary>

<a id="localhost-7100-api-v1-core-contr-dic-get-contract-subjects-by-id"></a>

#### localhost-7100/api/v1/dictionary/get-contract-subjects-by-id


<b>Method name:GET</b>

<details open>
<summary><b>ЗАПРОС</b></summary>

```request param
  id: "***"

```

</details>
<details open>
<summary><b>ОТВЕТ</b></summary>

```json
{
  "timestamp": null,
  "success": true,
  "result": {
    "code": 20200,
    "message": "OK",
    "audit": "4f45c22c-c1a5-46d2-b698-676365d1810d",
    "data": {
      "status": 0,
      "errorMsg": "OK",
      "timeStamp": "2025-10-24T04:54:40.125+00:00",
      "gtkId": 1761281679499,
      "records": [
        {
          "idNumber": 1,
          "id": 1,
          "name": "товар",
          "isDeleted": false
        }
      ]
    }
  }
}
```

</details>

</details>
<!--END ПОЛУЧИТЬ СУБЪЕКТЫ КОНТРАКТА ПО ID  -->


<details open>
<summary><b>ПОЛУЧИТЬ РЕГИОНЫ (справочник)</b> (нажмите, чтобы открыть или скрыть)</summary>

<a id="localhost-7100-api-v1-core-contr-dic-region"></a>

#### localhost-7100/api/v1/dictionary/region


<b>Method name:POST</b>

<details open>
<summary><b>ЗАПРОС</b></summary>

```json
 {
  "bankInn": "*********",
  "bankId": "*****"
}

```

</details>
<details open>
<summary><b>ОТВЕТ</b></summary>

```json
{
  "timestamp": null,
  "success": true,
  "result": {
    "code": 20200,
    "message": "OK",
    "audit": "f5419043-570d-4ff4-8166-a1b34d65022a",
    "data": {
      "status": 0,
      "errorMsg": "OK",
      "timeStamp": null,
      "gtkId": null,
      "records": [
        {
          "idNumber": 1,
          "id": "**",
          "name": "РЕСПУБЛИКА УЗБЕКИСТАН",
          "isDeleted": false
        }
      ]
    }
  }
}
```

</details>

</details>
<!--END ПОЛУЧИТЬ РЕГИОНЫ-->




<details open>
<summary><b>ПОЛУЧИТЬ ПРОМЫШЛЕННУЮ ЗОНУ (справочник)</b> (нажмите, чтобы открыть или скрыть)</summary>

<a id="localhost-7100-api-v1-core-contr-dic-industrial-zone"></a>

#### localhost-7100/api/v1/contract/dic/industrial-zone


<b>Method name:POST</b>

<details open>
<summary><b>ЗАПРОС</b></summary>

```json
{
  "bankInn": "*********",
  "bankId": "*****"
}
```

</details>
<details open>
<summary><b>ОТВЕТ</b></summary>

```json
{
  "timestamp": null,
  "success": true,
  "result": {
    "code": 20200,
    "message": "OK",
    "audit": "962f6b8e-17a1-422f-b2a2-b14a0984580e",
    "data": {
      "status": 0,
      "errorMsg": "OK",
      "timeStamp": null,
      "gtkId": null,
      "records": [
        {
          "idNumber": 1,
          "id": 0,
          "name": "не зарегистрирован в СИЗ",
          "isDeleted": false
        }
      ]
    }
  }
}
```

</details>

</details>
<!--END ПОЛУЧИТЬ ПРОМЫШЛЕННУЮ ЗОНУ  -->


<details open>
<summary><b>ПОЛУЧИТЬ ИНФОРМАЦИЮ О БАНКЕ (справочник)</b> (нажмите, чтобы открыть или скрыть)</summary>

<a id="localhost-7100-api-v1-core-contr-dic-bank-info"></a>

#### localhost-7100/api/v1/dictionary/bank-info


<b>Method name:POST</b>

<details open>
<summary><b>ЗАПРОС</b></summary>

```json
{
  "bankInn": "*********",
  "bankId": "*****"
}
```

</details>
<details open>
<summary><b>ОТВЕТ</b></summary>

```json
{
  "timestamp": null,
  "success": true,
  "result": {
    "code": 20200,
    "message": "OK",
    "audit": "633d1b9f-c590-47db-94bc-5173ec8e9ed8",
    "data": {
      "status": 0,
      "errorMsg": "OK",
      "timeStamp": null,
      "gtkId": null,
      "banks": [
        {
          "id": 1,
          "bankInn": "******",
          "bankId": "******",
          "bankClass": "****",
          "regionId": "**",
          "bankType": 0,
          "bankName": "ТОШКЕНТ Ш., МАРКАЗИЙ БАНКНИНГ ТЕЗКОР ТУЛОВЛАР КЛИРИНГ МАРКАЗИ",
          "bankAddress": "100001, Тошкент ш., Миробод т., Ислом Каримов к., 6",
          "active": "A",
          "newBankInn": "",
          "newBankId": "******",
          "parentNewBankInn": null,
          "parentNewBankId": null
        }
      ]
    }
  }
}
```

</details>

</details>
<!--END ПОЛУЧИТЬ ИНФОРМАЦИЮ О БАНКЕ -->




<details open>
<summary><b>ПОЛУЧИТЬ ПРОМЫШЛЕННУЮ ЗОНУ ПО ID (справочник)</b> (нажмите, чтобы открыть или скрыть)</summary>

<a id="localhost-7100-api-v1-core-contr-dic-industrial-zone-by-id"></a>

#### localhost-7100/api/v1/dictionary/industrial-zone-by-id


<b>Method name:GET</b>

<details open>
<summary><b>ЗАПРОС</b></summary>

```request param
  id: "**"

```

</details>
<details open>
<summary><b>ОТВЕТ</b></summary>

```json
{
  "timestamp": null,
  "success": true,
  "result": {
    "code": 20200,
    "message": "OK",
    "audit": "93bdcf16-1f72-4723-97f4-d0692f94a842",
    "data": {
      "status": 0,
      "errorMsg": "OK",
      "timeStamp": null,
      "gtkId": null,
      "records": [
        {
          "idNumber": 4,
          "id": 2,
          "name": "СИЗ \"Ангрен\"",
          "isDeleted": false
        }
      ]
    }
  }
}
```

</details>

</details>
<!--END ПОЛУЧИТЬ ПРОМЫШЛЕННУЮ ЗОНУ ПО ID  -->



<details open>
<summary><b>ПОЛУЧИТЬ РЕГИОН ПО ID (справочник)</b> (нажмите, чтобы открыть или скрыть)</summary>

<a id="localhost-7100-api-v1-core-contr-dic-region-by-id"></a>

#### localhost-7100/api/v1/dictionary/region-by-id


<b>Method name:GET</b>

<details open>
<summary><b>ЗАПРОС</b></summary>

```request param
  id: "**"

```

</details>
<details open>
<summary><b>ОТВЕТ</b></summary>

```json
{
  "timestamp": null,
  "success": true,
  "result": {
    "code": 20200,
    "message": "OK",
    "audit": "f5419043-570d-4ff4-8166-a1b34d65022a",
    "data": {
      "status": 0,
      "errorMsg": "OK",
      "timeStamp": null,
      "gtkId": null,
      "records": [
        {
          "idNumber": 1,
          "id": "01",
          "name": "РЕСПУБЛИКА УЗБЕКИСТАН",
          "isDeleted": false
        }
      ]
    }
  }
}
```

</details>

</details>
<!--END ПОЛУЧИТЬ РЕГИОН ПО ID  -->



<details open>
<summary><b>ПОЛУЧИТЬ ИНФОРМАЦИЮ О БАНКЕ ПО ID (справочник)</b> (нажмите, чтобы открыть или скрыть)</summary>

<a id="localhost-7100-api-v1-core-contr-dic-bank-info-by-id"></a>


#### localhost-7100/api/v1/dictionary/bank-info-by-id

<b>Method name:GET</b>

<details open>
<summary><b>ЗАПРОС</b></summary>

```request param
  id: "**"

```

</details>
<details open>
<summary><b>ОТВЕТ</b></summary>

```json
{
  "timestamp": null,
  "success": true,
  "result": {
    "code": 20200,
    "message": "OK",
    "audit": "83644781-d9a1-407e-9670-9e6240f8db2d",
    "data": {
      "status": 0,
      "errorMsg": "OK",
      "timeStamp": null,
      "gtkId": null,
      "banks": [
        {
          "id": 3,
          "bankInn": "******",
          "bankId": "******",
          "bankClass": "******",
          "regionId": "00",
          "bankType": 0,
          "bankName": "НАМАНГАН Ш., МАРКАЗИЙ БАНКНИНГ НАМАНГАН ВИЛОЯТ ХИСОБ-КИТОБ КАССА МАРКАЗИ",
          "bankAddress": "160136, Наманган ш., Боборахим Машраб к., 2",
          "active": "A",
          "newBankInn": "******",
          "newBankId": "******",
          "parentNewBankInn": null,
          "parentNewBankId": null
        }
      ]
    }
  }
}
```

</details>

</details>
<!--END ПОЛУЧИТЬ ИНФОРМАЦИЮ О БАНКЕ ПО ID  -->
</details>



<details open>
<summary><b>API Swift</b></summary>

<a id="mibcreditor-api"></a>

### Сервис отчетов

| №  |            Название API             |                                                    URL                                                    |
|:--:|:----------------------------------:|:--------------------------------------------------------------------------------------------------------:|
| 1  |    ПОЛУЧИТЬ ВСЕ ИНН ЮРИДИЧЕСКИХ КЛИЕНТОВ    |                [localhost:7100/api/v1/swift/get-all](#localhost:7100/api/v1/swift/get-all)                |
| 2  | ПОЛУЧИТЬ ИНН ЮРИДИЧЕСКИХ КЛИЕНТОВ С ПАГИНАЦИЕЙ |       [localhost:7100/api/v1/swift/get-page](#localhost:7100/api/v1/swift/get-page)        |
| 3  |   СКАЧАТЬ SWIFT В EXCEL    |    [localhost:7100/api/v1/swift/download/excel](#localhost:7100/api/v1/contract/dic/get-country-by-id)    |

<details open>
<summary><b>ПОЛУЧИТЬ ВСЕ ИНН ЮРИДИЧЕСКИХ КЛИЕНТОВ</b> (нажмите, чтобы открыть или скрыть)</summary>

<a id="localhost-7100-api-v1-core-contr-dic-country"></a>

#### localhost:7100/api/v1/swift/get-all


<b>Method name:POST</b>

<details open>
<summary><b>ЗАПРОС</b></summary>

```json
{
  "transType": 1,
  "startDate": "08.08.2025",
  "endDate": "08.011.2025"
}
```

</details>
<details open>
<summary><b>ОТВЕТ</b></summary>

```json
{
  "status": "success",
  "totalElements": 600,
  "error": "",
  "data": [
    {
      "DATE_OPER": "08.08.2025",
      "INN_CLIENT": "**********",
      "NAME_CLIENT": "OK \"BEYOND EXPECTATIONS\"",
      "IDN": "************************",
      "OKED": "*****",
      "OKONX": "",
      "KOD_REGION": "**",
      "KOD_STRANA": "***",
      "KOD_VAL": "***",
      "SUMMA_7": 0,
      "SUMMA_8": 0,
      "SUMMA_81": 0,
      "SUMMA_82": 0,
      "SUMMA_83": 0,
      "SUMMA_84": 0,
      "SUMMA_9": 0,
      "SUMMA_10": 0,
      "SUMMA_11": 0,
      "SUMMA_12": 0,
      "SUMMA_13": 0,
      "SUMMA_14": 0,
      "SUMMA_15": 0,
      "SUMMA_16": 0,
      "SUMMA_17": 0,
      "SUMMA_18": 0,
      "SUMMA_19": 0,
      "SUMMA_20": 0,
      "VID_OPER": "Оказанные туристические услуги нерезидентам в Узбекистане - прочие",
      "SUMMA_CHECK_1": 0,
      "SUMMA_CHECK_2": 0,
      "GENERAL_ID": "************",
      "ACC_CL": "************",
      "ACC_CO": "************",
      "TRANS_KOD1": "****",
      "TRANS_KOD2": ""
    }
  ]
}
```

</details>
</details>
<!--END ПОЛУЧИТЬ ВСЕ ИНН ЮРИДИЧЕСКИХ КЛИЕНТОВ -->


<details open>
<summary><b>ПОЛУЧИТЬ ИНН ЮРИДИЧЕСКИХ КЛИЕНТОВ С ПАГИНАЦИЕЙ</b> (нажмите, чтобы открыть или скрыть)</summary>

<a id="localhost-7100-api-v1-swift-get-page"></a>


#### localhost:7100/api/v1/swift/get-page

<b>Method name:POST</b>

<details open>
<summary><b>ЗАПРОС</b></summary>

```json
{
  "transType": 1,
  "startDate": "08.08.2025",
  "endDate": "08.011.2025",
  "pageNumber": 1,
  "pageSize": 10
}
```

</details>
<details open>
<summary><b>ОТВЕТ</b></summary>

```json
{
  "status": "success",
  "totalElements": 600,
  "error": "",
  "data": [
    {
      "DATE_OPER": "08.08.2025",
      "INN_CLIENT": "************",
      "NAME_CLIENT": "OK \"BEYOND EXPECTATIONS\"",
      "IDN": "************",
      "OKED": "************",
      "OKONX": "",
      "KOD_REGION": "**",
      "KOD_STRANA": "**",
      "KOD_VAL": "***",
      "SUMMA_7": 0,
      "SUMMA_8": 0,
      "SUMMA_81": 0,
      "SUMMA_82": 0,
      "SUMMA_83": 0,
      "SUMMA_84": 0,
      "SUMMA_9": 0,
      "SUMMA_10": 0,
      "SUMMA_11": 0,
      "SUMMA_12": 0,
      "SUMMA_13": 0,
      "SUMMA_14": 0,
      "SUMMA_15": 0,
      "SUMMA_16": 0,
      "SUMMA_17": 0,
      "SUMMA_18": 0,
      "SUMMA_19": 0,
      "SUMMA_20": 0,
      "VID_OPER": "Оказанные туристические услуги нерезидентам в Узбекистане - прочие",
      "SUMMA_CHECK_1": 0,
      "SUMMA_CHECK_2": 0,
      "GENERAL_ID": 109747740201,
      "ACC_CL": "************",
      "ACC_CO": "************",
      "TRANS_KOD1": "******",
      "TRANS_KOD2": ""
    }
  ]
}
```
</details>

<!--END ПОЛУЧИТЬ ИНН ЮРИДИЧЕСКИХ КЛИЕНТОВ С ПАГИНАЦИЕЙ-->
</details>

<details open>
<summary><b>СКАЧАТЬ SWIFT В EXCEL</b> (нажмите, чтобы открыть или скрыть)</summary>

<a id="localhost-7100-api-v1-swift-download-excel"></a>

#### localhost:7100/api/v1/swift/download/excel


<b>Method name:POST</b>

<details open>
<summary><b>ЗАПРОС</b></summary>

```json
{
  "transType": 1,
  "startDate": "08.08.2025",
  "endDate": "08.011.2025"
}
```

</details>
<details open>
<summary><b>ОТВЕТ</b></summary>

```json
"Base64"
```

</details>

</details>
<!--END СКАЧАТЬ SWIFT В EXCEL -->

</details>
</details>


