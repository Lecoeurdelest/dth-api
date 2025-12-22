# Project Structure Overview

This document provides a visual overview of the project structure following Modular Monolith standards.

## 📂 Root Structure

```
dth-api/
├── src/
│   ├── main/
│   │   ├── java/com/example/app/    # Source code
│   │   └── resources/                # Configuration & migrations
│   │       ├── application*.properties
│   │       └── db/migration/         # Flyway migrations
│   └── test/                         # Tests
├── build.gradle.kts                  # Build configuration
├── settings.gradle.kts
├── Dockerfile                        # Production Docker image
├── Dockerfile.dev                    # Development Docker image
├── docker-compose.yml                # Docker Compose configuration
├── MODULE_ORGANIZATION.md           # Module organization guide
├── ARCHITECTURE.md                   # Architecture documentation
└── README.md                         # Project README
```

## 🏗️ Module Structure (Standard)

Every module follows this structure:

```
{module}/
├── api/                              # REST Controllers (Public API)
│   └── {Module}Controller.java
│
├── application/                      # Business Logic Layer
│   ├── {Module}ApplicationService.java    # Public Interface
│   └── {Module}ServiceImpl.java           # Implementation
│
├── domain/                           # Domain Entities (PRIVATE)
│   └── {Entity}.java
│
├── repository/                       # JPA Repositories (PRIVATE)
│   └── {Entity}Repository.java
│
├── dto/                              # Data Transfer Objects
│   ├── {Request}Request.java
│   └── {Response}Dto.java
│
├── mapper/                           # MapStruct Mappers
│   └── {Module}Mapper.java
│
├── config/                           # Module Configuration (optional)
│   └── {Module}Config.java
│
└── package-info.java                 # Module documentation
```

## 📦 Current Modules

### Auth Module (`com.example.app.auth`)
- **Purpose:** Authentication & Authorization
- **Public Interface:** `AuthApplicationService`
- **Tables:** `auth_users`, `auth_refresh_tokens`
- **Endpoints:** `/auth/**`

### Services Module (`com.example.app.services`)
- **Purpose:** Service Catalog
- **Public Interface:** `ServiceApplicationService`
- **Tables:** `services_services`
- **Endpoints:** `/services/**`

### Orders Module (`com.example.app.orders`)
- **Purpose:** Order Management
- **Public Interface:** `OrderApplicationService`
- **Tables:** `orders_orders`, `orders_reviews`, etc.
- **Endpoints:** `/orders/**`

### Loyalty Module (`com.example.app.loyalty`)
- **Purpose:** Loyalty Points System
- **Public Interface:** `LoyaltyApplicationService`
- **Tables:** `loyalty_points`, `loyalty_points_transactions`
- **Endpoints:** `/loyalty/**`

### Profile Module (`com.example.app.profile`)
- **Purpose:** User Profile Management
- **Tables:** `profile_user_profiles`
- **Endpoints:** `/profile/**`

### Home Module (`com.example.app.home`)
- **Purpose:** Homepage Data
- **Tables:** `home_data`, `home_testimonials`
- **Endpoints:** `/home/**`

### News Module (`com.example.app.news`)
- **Purpose:** News/Articles
- **Tables:** `news_articles`
- **Endpoints:** `/news/**`

### Contact Module (`com.example.app.contact`)
- **Purpose:** Contact Form
- **Tables:** `contact_messages`
- **Endpoints:** `/contact/**`

### Tasks Module (`com.example.app.tasks`)
- **Purpose:** Dashboard Aggregation
- **Tables:** None (aggregates from other modules)
- **Endpoints:** `/tasks/**`

## 🔗 Shared Kernel (`com.example.app.shared`)

The shared kernel provides cross-cutting concerns:

```
shared/
├── config/              # Global Spring configuration
│   └── OpenApiConfig.java
├── security/            # Security infrastructure
│   ├── SecurityConfig.java
│   ├── JwtTokenProvider.java
│   └── JwtAuthenticationFilter.java
├── exception/           # Exception handling
│   ├── GlobalExceptionHandler.java
│   ├── ResourceNotFoundException.java
│   └── ...
├── response/            # API response wrapper
│   └── ApiResponse.java
└── util/               # Utilities
    └── PageUtil.java
```

## 🔄 Module Communication Flow

```
┌─────────────┐
│  Controller │  (api/)
└──────┬──────┘
       │
       ▼
┌─────────────────────┐
│ Application Service │  (application/)
│    (Interface)      │  ← Public API
└──────┬──────────────┘
       │
       ▼
┌─────────────────────┐
│ Service Impl        │  (application/)
│    (Implementation) │
└──────┬──────────────┘
       │
       ├──► Repository (repository/)
       ├──► Domain (domain/)
       └──► Other Module's Application Service (via interface)
```

## 📋 Key Files

### Configuration Files
- `application.properties` - Main configuration
- `application-docker.properties` - Docker-specific config
- `build.gradle.kts` - Build dependencies

### Documentation
- `MODULE_ORGANIZATION.md` - Module organization guide
- `ARCHITECTURE.md` - Architecture documentation
- `DOCKER_SETUP.md` - Docker setup guide

### Docker Files
- `Dockerfile` - Production image
- `Dockerfile.dev` - Development image
- `docker-compose.yml` - Service orchestration
- `start.sh` - One-click startup script

## 🎯 Module Boundary Enforcement

### Compile-Time Enforcement
- Package structure enforces boundaries
- `package-info.java` documents rules
- Interfaces define public contracts

### Runtime Enforcement
- Spring dependency injection uses interfaces
- No direct entity/repository access across modules

### Code Review Checklist
- [ ] No entity imports from other modules
- [ ] No repository imports from other modules
- [ ] Cross-module communication uses interfaces only
- [ ] DTOs used for data transfer
- [ ] Module follows standard structure

## 📚 Related Documentation

- [MODULE_ORGANIZATION.md](./MODULE_ORGANIZATION.md) - Detailed module organization guide
- [ARCHITECTURE.md](./ARCHITECTURE.md) - Architecture principles
- [README.md](./README.md) - Project overview and setup

