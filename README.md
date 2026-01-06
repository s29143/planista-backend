# Planista – Backend

Backend application for the *Planista* system – a web-based platform for managing orders, processes, and related business entities in a production/service environment.

The backend is implemented as a REST API using Spring Boot and follows a modular architecture.

---

## Table of Contents
- [Technology Stack](#technology-stack)
- [Security](#security)
- [Requirements](#requirements)
- [Running the Application](#running-the-application)
- [Docker](#docker)
- [Testing](#testing)
- [Frontend](#frontend)

---

## Technology Stack

### Core
- **Java 25** (Gradle Toolchain)
- **Spring Boot 3.x.x**
- Spring Web (REST API)
- Spring Security
- Spring Validation

### Persistence
- Spring Data JPA
- PostgreSQL
- Hibernate

### Security
- JWT (JSON Web Tokens)
- Role-based authorization (`@PreAuthorize`)
- Rate limiting (Bucket4j)

### Utilities & Tooling
- MapStruct (DTO ↔ Entity mapping)
- Lombok
- Gradle (Kotlin DSL)

### Testing
- JUnit 5
- Spring Boot Test
- Spring Security Test
- Testcontainers (PostgreSQL)

---

## Requirements

- Java 25 (or compatible toolchain via Gradle)
- PostgreSQL
- Docker (required for Testcontainers-based tests)
- Gradle (wrapper included)

---
## Running the Application

Development mode
> `./gradlew clean bootRun`


The application will start on:

`http://localhost:8080`

Build executable JAR
> `./gradlew clean bootJar`


The JAR file will be available in:

`build/libs/`


## Docker

The backend can be built and run as a Docker container.

### Dockerfile
A multi-stage Docker build is used to create a small and production-ready image.

### Build image
```bash
docker build -t planista-backend .

docker run -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/planista \
  -e SPRING_DATASOURCE_USERNAME=planista \
  -e SPRING_DATASOURCE_PASSWORD=planista \
  -e JWT_SECRET=change-me \
  planista-backend
```
The application will be available at:

`http://localhost:8080`


## Testing
Run all tests
> `./gradlew test`

Integration tests use Testcontainers, so Docker must be running.

## Frontend

The frontend application is maintained in a separate repository:

👉 Frontend repository:
[Planista Frontend](https://github.com/s29143/planista-frontend)