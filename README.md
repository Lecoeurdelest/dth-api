# Modular Monolith Backend

A Java Spring Boot backend application built with a **Modular Monolith** architecture, using Gradle, MariaDB, and JWT-based authentication.

## 🏗️ Architecture Overview

This project follows a **Modular Monolith** pattern, which provides:

- ✅ Strong module boundaries
- ✅ Clear separation of concerns
- ✅ Single deployable unit (no microservices)
- ✅ Scalable structure that can evolve
- ✅ No circular dependencies

### Architecture Principles

1. **Module Isolation**: Each module owns its domain entities, repositories, and business logic
2. **Communication via Application Layer**: Modules communicate only through application service interfaces
3. **No Cross-Module Entity Access**: Modules cannot directly access entities from other modules
4. **No Cross-Module Repository Access**: Repositories are module-private
5. **Database Isolation**: Each module has its own tables (prefixed with module name), no foreign keys across modules

## 📁 Project Structure

```
src/main/java/com/example/app
├── shared/              # Shared infrastructure (Shared Kernel)
│   ├── config/         # Global configuration
│   ├── security/       # JWT, Spring Security
│   ├── exception/      # Global exception handling
│   ├── response/       # API response wrapper
│   └── util/           # Utility classes
│
├── auth/               # Authentication module
├── home/               # Home page module
├── services/           # Services module
├── news/               # News module
├── contact/            # Contact module
├── profile/            # Profile module
├── orders/             # Orders module
├── tasks/              # Tasks dashboard module
├── loyalty/            # Loyalty points module
│
└── Application.java    # Main application class
```

**📖 For detailed module organization guidelines, see [MODULE_ORGANIZATION.md](./MODULE_ORGANIZATION.md)**

**📖 For project file organization, see [PROJECT_ORGANIZATION.md](./PROJECT_ORGANIZATION.md)**

## 📦 Module Structure (Mandatory)

Each module follows this exact structure:

```
{module}/
├── api/              # REST controllers (public boundary)
├── application/      # Use cases / services (public API)
├── domain/           # Entities & domain logic (private)
├── repository/       # JPA repositories (private)
├── dto/              # Request / Response DTOs
├── mapper/           # MapStruct mappers
└── config/           # Module-specific config
```

### Module Boundaries Rules

❌ **DO NOT:**
- Import entities from other modules
- Import repositories from other modules
- Access domain objects directly across modules

✅ **DO:**
- Use DTOs for cross-module communication
- Call other modules via their application services
- Keep entities and repositories private to the module

## 🚀 Quick Start with Docker (Recommended)

The easiest way to get started is using Docker:

```bash
# One-click start (builds and runs everything)
./scripts/start.sh

# Or using Make
make start
```

This will:
- ✅ Start MariaDB database
- ✅ Build and start the Spring Boot application
- ✅ Run Flyway migrations automatically
- ✅ Make services available at http://localhost:8080

**Access:**
- API: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui.html
- Database: localhost:3306 (user: `root`, password: `root`)

For detailed Docker setup, see [DOCKER_SETUP.md](./DOCKER_SETUP.md) or [DOCKER_QUICK_START.md](./DOCKER_QUICK_START.md)

## 📋 Available Commands

### Using Make

```bash
make help          # Show all available commands
make build         # Build Docker images
make up            # Start containers
make down          # Stop containers
make logs          # Show logs
make restart       # Restart containers
make clean         # Clean everything
make dev           # Development mode
make db-logs       # MariaDB logs only
make app-logs      # Application logs only
make shell         # Open shell in app container
make db-shell      # Open MariaDB shell
make rebuild       # Rebuild and restart
```

### Using Scripts

```bash
./scripts/start.sh      # One-click start
./scripts/start-dev.sh   # Development mode with hot-reload
```

### Using Docker Compose Directly

```bash
# Start services
docker-compose -f docker/docker-compose.yml up -d

# View logs
docker-compose -f docker/docker-compose.yml logs -f

# Stop services
docker-compose -f docker/docker-compose.yml down
```

## 🔐 Module Responsibilities

### Auth Module (`/auth/**`)
- User registration and login
- JWT token generation (access + refresh)
- Password hashing (BCrypt)
- Social login placeholders (Google, Facebook)
- **Public endpoints**

### Services Module (`/services/**`)
- Service listing (paginated)
- Service detail
- Service categories
- **Public endpoints**

### Orders Module (`/orders/**`)
- Order history (paginated)
- Order detail
- Order status filtering
- Order reviews
- Invoice preview (stub)
- **Protected endpoints** (requires authentication)

### Profile Module (`/profile/**`)
- User profile management
- Update profile
- Avatar upload (stub)
- **Protected endpoints** (requires authentication)

### Loyalty Module (`/loyalty/**`)
- Points balance
- Tier calculation
- Points history (paginated)
- **Protected endpoints** (requires authentication)

## 🗄️ Database Design

### Database Rules

1. **Module-Owned Tables**: Each module owns its tables (prefixed with module name)
   - `auth_users`
   - `home_data`, `home_testimonials`
   - `services_services`
   - `orders_orders`, `orders_reviews`
   - etc.

2. **No Foreign Keys Across Modules**: Communication via IDs only
   - Example: `orders_orders.user_id` references `auth_users.id` by ID only (no FK)

3. **Flyway Migrations**: All schema changes via Flyway migrations in `src/main/resources/db/migration/`

## 🔒 Security

### Authentication
- **JWT-based authentication** with access tokens and refresh tokens
- Access token expiration: 1 hour (configurable)
- Refresh token expiration: 24 hours (configurable)
- Password hashing: BCrypt

### Security Configuration
- Public endpoints: `/auth/**`, `/home/**`, `/services/**`, `/news/**`, `/contact/**`
- Protected endpoints: `/profile/**`, `/orders/**`, `/tasks/**`, `/loyalty/**`
- CORS enabled for all origins (configure for production)

## 🛠️ Technology Stack

- **Java 17+**
- **Spring Boot 3.2.0**
- **Gradle (Kotlin DSL)**
- **Spring Web** (REST APIs)
- **Spring Data JPA** (Hibernate)
- **MariaDB**
- **Spring Security**
- **JWT** (io.jsonwebtoken:jjwt)
- **Lombok**
- **MapStruct**
- **Flyway**
- **Jakarta Validation**
- **OpenAPI (Swagger)**

## 📚 Documentation

- [ARCHITECTURE.md](./ARCHITECTURE.md) - Architecture documentation
- [MODULE_ORGANIZATION.md](./MODULE_ORGANIZATION.md) - Module organization guide
- [PROJECT_STRUCTURE.md](./PROJECT_STRUCTURE.md) - Project structure overview
- [PROJECT_ORGANIZATION.md](./PROJECT_ORGANIZATION.md) - File organization guide
- [DOCKER_SETUP.md](./DOCKER_SETUP.md) - Docker setup guide
- [DOCKER_QUICK_START.md](./DOCKER_QUICK_START.md) - Docker quick reference
- [QUICK_START.md](./QUICK_START.md) - Quick start guide
- [TROUBLESHOOTING.md](./TROUBLESHOOTING.md) - Troubleshooting guide

## 📝 License

[Add your license here]
