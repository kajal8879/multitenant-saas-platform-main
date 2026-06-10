# Multi Tenant SaaS Platform

## Stack

- Java 21
- Spring Boot 3
- Spring Security
- Spring Data JPA
- PostgreSQL
- JWT
- Stripe Java SDK
- Swagger/OpenAPI
- Maven

## Run With Docker

```bash
docker compose up --build
```

App runs at:

```text
http://localhost:8080
```

Swagger docs:

```text
http://localhost:8080/swagger-ui.html
```

## Run Locally

Create a PostgreSQL database:

```sql
create database tenant_platform;
```

Then open cmd and change directory to folder which contains pom.xml file and run following command

```bash
./mvnw spring-boot:run
```

Dummy values are already in `application.properties` so the app can start during assessment.
While testing through swagger please register using / register endpoint, it will return a token . Authorize swagger using that token , then all endpoints will be accessible.

## Tests

```bash
./mvnw test
```
