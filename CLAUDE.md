# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

emall is an e-commerce system built with a Dubbo-based microservices architecture. The project uses Java 11, Spring Boot 2.7.18, and Spring Cloud Hoxton.SR7.

## Architecture

### Module Structure

This is a multi-module Maven project with a layered architecture:

- **emall-dependencies**: Centralized dependency version management
- **emall-super-pom**: Parent POM with common build configuration
- **emall-common**: Shared utilities, DTOs, exceptions, enums, request/response wrappers
- **emall-api**: Dubbo service interface definitions for each business domain (cart, comment, order, pay, product, search, sms, user)
- **emall-service**: Service implementations
  - **emall-service-component**: Shared service components (Redis, ES, DB configs, etc.)
  - **emall-service-facade**: HTTP REST API gateway (port 80) that consumes Dubbo services and exposes them as REST endpoints. This is the main entry point for external clients.
  - **emall-service-{domain}**: Individual microservices implementing the Dubbo API interfaces (each has its own database)

### Technology Stack

- **RPC**: Dubbo 2.7.8 with Zookeeper registry
- **Database**: MySQL 8.0.32 with MyBatis Plus 3.5.12
- **Cache**: Redis (Jedis client)
- **Search**: Elasticsearch 7.17.5
- **Tracing**: Zipkin + Spring Cloud Sleuth
- **Auth**: JWT with Apache Shiro
- **Connection Pool**: Druid

### Service Communication

- **Internal**: Services communicate via Dubbo RPC (registered in Zookeeper)
- **External**: Clients interact with `emall-service-facade` (HTTP REST), which acts as an API gateway
- API interfaces are defined in `emall-api/*` modules
- Implementations are annotated with `@DubboService` in `emall-service-*/api/*ServiceImpl.java`

## Building and Running

### Build entire project
```bash
mvn clean install
```

### Build specific module
```bash
cd emall-service/emall-service-product
mvn clean package
```

### Run a microservice
Each service module (e.g., emall-service-product, emall-service-cart) is a Spring Boot application:
```bash
cd emall-service/emall-service-product
mvn spring-boot:run
```

Or run the Application class directly from IDE (e.g., `EmallProductApplication.java`).

### Run the facade (API gateway)
```bash
cd emall-service/emall-service-facade
mvn spring-boot:run
```
The facade runs on port 80 by default.

### Run tests
```bash
mvn test
```

## Infrastructure Dependencies

Services expect the following middleware to be running (configured in `application.yml` files):

- **MySQL**: `mysql.middleware.emall.docker:3306` (separate database per service)
- **Redis**: `redis.middleware.emall.docker:6379`
- **Zookeeper**: `zookeeper.middleware.emall.docker:2181`
- **Elasticsearch**: Configured in service-search
- **Zipkin**: `http://zipkin.middleware.emall.docker:9411`

These are typically run via Docker. See `emall-init/` for Docker initialization scripts and `Dockerfile`s in service modules.

## Configuration

- Each service has its own `application.yml` in `src/main/resources/`
- Database connection strings, Dubbo registry, Redis, and Zipkin are configured per service
- JWT secret and TTL are configured in facade's `application.yml`
- Table prefix convention: `t_` (configured in MyBatis Plus)

## Common Patterns

### Adding a new API method
1. Define the interface method in `emall-api/{domain}/api/Emall{Domain}Service.java`
2. Implement it in `emall-service-{domain}/api/Emall{Domain}ServiceImpl.java` with `@DubboService` annotation
3. Add a controller method in `emall-service-facade/controller/{domain}/*Controller.java` to expose it via HTTP

### Request/Response Pattern
All Dubbo service methods follow a standard pattern:
- Accept `EmallRequest<T>` as parameter
- Return `EmallResponse<T>` as result
- Throw `EmallException` on errors

### Data Access
- Use MyBatis Plus mappers (extending `BaseMapper`)
- Mappers are located in `{service}/mapper/` packages
- Entity models are in `emall-api/{domain}/model/` packages
