# Architecture Documentation

## Modular Monolith Pattern

This backend implements a **Modular Monolith** architecture, providing:

- ✅ Strong module boundaries
- ✅ Clear separation of concerns  
- ✅ Single deployable unit
- ✅ Scalable structure
- ✅ No circular dependencies

## Module Boundaries

### Strict Rules

1. **No Cross-Module Entity Access**
   - Modules cannot import entities from other modules
   - Entities are private to their module

2. **No Cross-Module Repository Access**
   - Repositories are module-private
   - No direct database queries across modules

3. **Communication via Application Layer**
   - Modules communicate through application services
   - Use DTOs for data transfer
   - Application services are the public API

4. **Database Isolation**
   - Each module owns its tables (prefixed with module name)
   - No foreign keys across modules
   - Reference via IDs only

## Module Communication Pattern

```
Module A                    Module B
--------                    --------
Controller                  Controller
    |                           |
    v                           v
Application Service    Application Service (Public API)
    |                           |
    v                           |
Domain / Repository            |
    |                           |
    |<--- DTO Communication --->|
```

### Example Flow

**Orders Module needs User info:**

```
OrdersController
    |
    v
OrderService (application)
    |
    v (calls via interface)
AuthApplicationService (public interface)
    |
    v
AuthService (application) 
    |
    v
UserRepository (private)
    |
    v
UserDto (returned)
```

## Module Structure Template

Every module follows this structure:

```
{module}/
├── api/                  # REST Controllers (public boundary)
│   └── {Module}Controller.java
│
├── application/          # Business Logic (public API)
│   └── {Module}Service.java
│
├── domain/              # Entities (private)
│   └── {Entity}.java
│
├── repository/          # JPA Repositories (private)
│   └── {Entity}Repository.java
│
├── dto/                 # Data Transfer Objects
│   ├── {Request}Request.java
│   └── {Response}Dto.java
│
├── mapper/              # MapStruct Mappers
│   └── {Module}Mapper.java
│
└── config/              # Module Configuration
    └── {Module}Config.java
```

## Database Design

### Table Naming Convention

- Format: `{module}_{table_name}`
- Examples:
  - `auth_users`
  - `orders_orders`
  - `orders_reviews`
  - `services_services`
  - `loyalty_points`

### Cross-Module References

- Use `BIGINT` for IDs
- No foreign key constraints
- Reference integrity maintained in application layer
- Example: `orders_orders.user_id` references `auth_users.id` (no FK)

## Security Architecture

### Authentication Flow

```
1. Client → POST /auth/login
2. AuthController → AuthService
3. AuthService validates credentials
4. JwtTokenProvider generates tokens
5. Returns access + refresh tokens
```

### Authorization Flow

```
1. Client → GET /profile (with Bearer token)
2. JwtAuthenticationFilter intercepts
3. Validates token via JwtTokenProvider
4. Loads UserDetails via UserDetailsService
5. Sets SecurityContext
6. ProfileController processes request
```

### Security Layers

- **Filter Layer**: JwtAuthenticationFilter
- **Security Config**: SecurityConfig (endpoint rules)
- **Method Security**: @PreAuthorize (if needed)

## API Design Principles

### Response Format

All endpoints return unified `ApiResponse<T>`:

```json
{
  "success": true,
  "data": { ... },
  "error": null,
  "message": "Optional message"
}
```

### Error Handling

- Global exception handler catches all exceptions
- Returns appropriate HTTP status codes
- Consistent error response format
- Logs errors appropriately

### Pagination

- All list endpoints support pagination
- Standard query parameters: `page`, `size`, `sortBy`, `sortDir`
- Returns pagination metadata

## Technology Stack Decisions

### Spring Boot 3.2.0
- Latest stable version
- Jakarta EE 9+ support
- Improved performance

### Gradle (Kotlin DSL)
- Modern build system
- Type-safe build scripts
- Better IDE support

### MariaDB
- MySQL-compatible
- Open source
- Production-ready

### JWT (jjwt 0.12.3)
- Stateless authentication
- Access + refresh token pattern
- Industry standard

### MapStruct
- Compile-time code generation
- Type-safe mapping
- Better performance than runtime mapping

### Flyway
- Version-controlled migrations
- Database schema as code
- Rollback support

## Module Responsibilities Matrix

| Module | Endpoints | Auth Required | Main Entities |
|--------|-----------|---------------|---------------|
| auth | /auth/** | No | User |
| home | /home/** | No | HomeData, Testimonial |
| services | /services/** | No | Service |
| news | /news/** | No | News |
| contact | /contact/** | No | ContactMessage |
| profile | /profile/** | Yes | UserProfile |
| orders | /orders/** | Yes | Order, OrderReview |
| tasks | /tasks/** | Yes | (Aggregation only) |
| loyalty | /loyalty/** | Yes | LoyaltyPoints, PointsTransaction |

## Scalability Considerations

### Current Architecture Benefits

1. **Easy to Extract Modules**
   - Clear boundaries make extraction straightforward
   - Can evolve to microservices if needed

2. **Independent Development**
   - Teams can work on modules independently
   - Reduced coupling

3. **Database Scaling**
   - Can split databases per module later
   - Current: single database, module-prefixed tables

### Future Evolution Path

1. **Event-Driven Communication** (Optional)
   - Add Spring Events between modules
   - Async communication

2. **Separate Databases** (If needed)
   - Each module gets its own database
   - Keep application layer communication

3. **Microservices** (If needed)
   - Extract modules to separate services
   - Use API Gateway
   - Keep same application layer interfaces

## Best Practices

1. **Always use DTOs** for API boundaries
2. **Validate input** with Jakarta Validation
3. **Handle exceptions** globally
4. **Log appropriately** (info, debug, error)
5. **Use transactions** on write operations
6. **Paginate** all list endpoints
7. **Never expose entities** directly
8. **Keep modules independent**

## Testing Strategy

### Unit Tests
- Test application services in isolation
- Mock repository dependencies
- Test business logic

### Integration Tests
- Test controllers with @WebMvcTest
- Test repositories with @DataJpaTest
- Test security with @SpringBootTest

### Module Communication Tests
- Test application service interactions
- Verify DTO transformations
- Test error handling

---

**This architecture provides a solid foundation for a scalable, maintainable backend system.**


