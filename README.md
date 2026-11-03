# SmartBAS — Bank Automation System

**SmartBAS** is Davrbank's internal bank automation system: a set of Spring Boot microservices for internal
reporting and back-office operations, built on top of the core banking system (NCI/ABS, Oracle). It handles
**50M+ records** and generates high-performance reports with Excel/CSV export, and it integrates the bank with
card processing (Humo, Uzcard), government systems and partners.

> **Context.** Built by the Davrbank engineering team. I work there as a Backend Engineer (Oct 2025 – present) and
> worked mainly on the reporting and integration services marked ⭐. Published as a portfolio sample with all
> confidential data removed — see [Security & sanitization](#security--sanitization).

## Services

### Reporting & back-office
| Service | Purpose |
|---|---|
| `report-turnover` ⭐ | Account turnover reports over very large ledgers, streamed Excel/CSV export (90 of 94 commits are mine) |
| `user-info-base` ⭐ | Customer information base that aggregates core-banking and document-hub data |
| `loan-outside` ⭐ | Customers' loans in other banks (credit bureau data) with Excel export |
| `repayment-schedule` ⭐ | Loan repayment schedules |
| `user-manual` ⭐ | Versioned user-manual and documentation service (Oracle/PostgreSQL, MinIO) |
| `client-accounts`, `credit-accounts` | Client and credit account statements with file export |
| `treasury` | Treasury payments, templates and wiring |
| `journal-transfer` | Journal (ledger) transfers |
| `abs-resource-hub` ⭐, `audit-account`, `audit-account-hub` | Access hub to core-banking (ABS) resources and account auditing |
| `collateral-auto`, `collateral-mortgage` (+ `*-hub`, `collateral-hub`) | Collateral registries for auto loans and mortgages, with reports |
| `files-cloud` | File storage service on MinIO |

### Integrations
| Service | Purpose |
|---|---|
| `nci-billing` | Integration with the NCI core banking system: client info, loan payments, simulator |
| `partner-validation` ⭐ | Merchant/partner validation with a reactive client, Flyway and Testcontainers tests |
| `remittance-outside` ⭐, `remittance`, `leskar-server` | International money remittance integration |
| `humo-gateway`, `humo-integration`, `humo-middleware` | Humo card processing: SOAP gateway, limits, card restrictions, verification, history |
| `uzcard-integration`, `account-to-card` | Uzcard card operations and account-to-card transfers |
| `external-payment` | External payments: hold, pay and status |
| `esud` | Electronic court (e-SUD) integration with digital signatures |
| `yeisvo` | YEISVO foreign-trade system integration: contracts, reports and bank dictionaries |

### Internal tools
| Service | Purpose |
|---|---|
| `edu-platform` | Employee training platform: courses, assignments and task control (3 services, Keycloak) |
| `recruiter-bot` ⭐ | Telegram recruiting bot with candidate tests |

## Tech stack
**Java 17/21 · Spring Boot 3.x (some legacy 2.x services) · Spring Cloud (Eureka, OpenFeign, Vault) · WebFlux ·
Oracle · PostgreSQL · ClickHouse · Kafka · Redis · MinIO · Liquibase/Flyway · Apache POI · SOAP · Jasypt ·
Testcontainers · Docker · Maven**

## What I worked on
- Report generation and **performance optimization** for very large datasets: streaming Excel generation (Apache POI SXSSF),
  `StreamingResponseBody` downloads and CSV export, which keep memory flat on multi-million-row reports.
- Integration with the **NCI / ABS core banking** system.
- New services end-to-end: turnover reports, loan-outside, repayment schedules, user manual, partner
  validation and remittance.

## Running a service
```bash
cd report-turnover
./mvnw spring-boot:run
```
Every secret or endpoint is a `${ENV_VAR}` placeholder (see `.env.example`). Docker builds expect your own
Maven `settings.xml`, which was removed from this copy.

## Security & sanitization
Credentials, keys, environment profiles, CI pipelines, Maven settings, internal hosts/IPs, customer data
(national IDs, card numbers, phones) and colleagues' personal data were removed or masked. Git history is not
included.
