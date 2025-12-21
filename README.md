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
├── shared/              # Shared infrastructure
│   ├── config/         # Global configuration
│   ├── security/       # JWT, Spring Security
│   ├── exception/      # Global exception handling
│   ├── response/       # API response wrapper
│   ├── mapper/         # Shared mappers (if any)
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

## 🔐 Module Responsibilities

### Auth Module (`/auth/**`)
- User registration and login
- JWT token generation (access + refresh)
- Password hashing (BCrypt)
- Social login placeholders (Google, Facebook)
- **Public endpoints**

### Home Module (`/home/**`)
- Homepage aggregated data
- Hero banners
- Testimonials
- Service categories preview
- **Public endpoints**

### Services Module (`/services/**`)
- Service listing (paginated)
- Service detail
- Service categories
- **Public endpoints**

### News Module (`/news/**`)
- News listing (paginated)
- News detail
- Featured articles
- **Public endpoints**

### Contact Module (`/contact/**`)
- Contact form submission
- Chat request placeholder
- **Public endpoints**

### Profile Module (`/profile/**`)
- User profile management
- Update profile
- Avatar upload (stub)
- **Protected endpoints** (requires authentication)

### Orders Module (`/orders/**`)
- Order history (paginated)
- Order detail
- Order status filtering
- Order reviews
- Invoice preview (stub)
- **Protected endpoints** (requires authentication)

### Tasks Module (`/tasks/**`)
- Dashboard aggregation
- Profile summary
- Orders summary
- Promotions summary
- Read-only aggregation
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

### Database Schema

See Flyway migration files:
- `V1__Create_auth_users_table.sql`
- `V2__Create_home_tables.sql`
- `V3__Create_services_services_table.sql`
- `V4__Create_news_articles_table.sql`
- `V5__Create_contact_messages_table.sql`
- `V6__Create_profile_profiles_table.sql`
- `V7__Create_orders_tables.sql`
- `V8__Create_loyalty_tables.sql`

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

### JWT Configuration

Configure in `application.properties`:
```properties
app.jwt.secret=your-secret-key-change-this-in-production
app.jwt.access-token-expiration-ms=3600000
app.jwt.refresh-token-expiration-ms=86400000
```

**⚠️ Important**: Change the JWT secret in production to a strong secret (minimum 256 bits).

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

## 📋 API Design

### Response Format

All API responses follow this unified format:

```json
{
  "success": true,
  "data": {},
  "error": null,
  "message": "Optional message"
}
```

### Error Response

```json
{
  "success": false,
  "data": null,
  "error": "ERROR_CODE",
  "message": "Error description"
}
```

### Pagination

List endpoints support pagination with query parameters:
- `page` (default: 0)
- `size` (default: 10)
- `sortBy` (default: "id")
- `sortDir` (default: "asc")

Response includes pagination metadata:
```json
{
  "success": true,
  "data": {
    "content": [],
    "page": 0,
    "size": 10,
    "totalElements": 100,
    "totalPages": 10,
    "last": false,
    "first": true
  }
}
```

## 🚀 Getting Started

### Prerequisites

- Java 17+
- Gradle 8.5+
- MariaDB 10.5+
- IDE (IntelliJ IDEA recommended)

### Setup

1. **Clone the repository**

2. **Configure Database**

   Update `src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:mariadb://localhost:3306/app_db
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   ```

3. **Create Database**
   ```sql
   CREATE DATABASE app_db;
   ```

4. **Run Application**
   ```bash
   ./gradlew bootRun
   ```

   Or using Gradle wrapper:
   ```bash
   gradlew bootRun
   ```

5. **Access Swagger UI**
   - Open: http://localhost:8080/swagger-ui.html
   - API Docs: http://localhost:8080/v3/api-docs

### Build

```bash
./gradlew build
```

### Run Tests

```bash
./gradlew test
```

## 📝 Adding a New Module

To add a new module, follow these steps:

1. **Create Module Structure**
   ```
   {newmodule}/
   ├── api/
   ├── application/
   ├── domain/
   ├── repository/
   ├── dto/
   ├── mapper/
   └── config/
   ```

2. **Create Domain Entity**
   - Place in `domain/` package
   - Use `@Entity` and `@Table(name = "{module}_{tablename}")`
   - No foreign keys to other modules

3. **Create Repository**
   - Extend `JpaRepository<Entity, Long>`
   - Keep it in `repository/` package

4. **Create DTOs**
   - Request DTOs for API input
   - Response DTOs for API output
   - Place in `dto/` package

5. **Create Application Service**
   - Business logic goes here
   - Public API for the module
   - Place in `application/` package

6. **Create REST Controller**
   - Public endpoints
   - Use `@RestController` and `@RequestMapping("/{module}")`
   - Place in `api/` package

7. **Create MapStruct Mapper**
   - Map between entities and DTOs
   - Place in `mapper/` package

8. **Create Flyway Migration**
   - Create table(s) with module prefix
   - No foreign keys to other modules
   - Place in `src/main/resources/db/migration/`

9. **Update Security Config**
   - Add endpoint paths to `SecurityConfig.java`
   - Specify public vs protected

10. **Add Module Config**
    - Create `{module}/config/{Module}Config.java`
    - Add any module-specific configuration

## 🔄 Module Communication Example

**Example: Orders module needs user info from Auth module**

❌ **Wrong Approach:**
```java
// In OrderService
@Autowired
private UserRepository userRepository; // ❌ DON'T DO THIS
```

✅ **Correct Approach:**
```java
// In OrderService
@Autowired
private AuthApplicationService authService; // ✅ Use application service

// Auth module exposes:
public interface AuthApplicationService {
    UserDto getUserById(Long userId);
}
```

## 🧪 Testing

### Unit Tests

Create tests in `src/test/java/com/example/app/{module}/`

### Integration Tests

Use `@SpringBootTest` for integration testing.

## 📚 API Documentation

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/v3/api-docs

All endpoints are documented with OpenAPI annotations.

## 🎯 Best Practices

1. **Keep modules independent**: No direct dependencies between modules
2. **Use DTOs**: Always use DTOs for API boundaries
3. **Validate input**: Use Jakarta Validation on request DTOs
4. **Handle exceptions**: Use global exception handler
5. **Log appropriately**: Use appropriate log levels
6. **Transaction management**: Use `@Transactional` on service methods
7. **Pagination**: Always paginate list endpoints
8. **Security**: Never expose entities directly, always use DTOs

## 🚧 Limitations & Future Enhancements

### Current Limitations

- Tasks module aggregation is a placeholder (needs integration with other modules)
- Social login (Google, Facebook) is placeholder
- Avatar upload is stub
- Invoice preview is stub

### Future Enhancements

- Event-driven communication between modules (optional)
- Caching layer (Redis)
- Message queue for async operations
- Metrics and monitoring
- Full integration tests

## 📄 License

[Your License Here]

## 👥 Contributors

[Your Team Here]

---

**Built with ❤️ using Modular Monolith architecture**


