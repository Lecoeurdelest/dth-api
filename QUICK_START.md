# Quick Start Guide

## Prerequisites Check

- [ ] Java 17+ installed (`java -version`)
- [ ] MariaDB installed and running
- [ ] Database `app_db` created (or will be auto-created)

## Setup Steps

### 1. Configure Database

Edit `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mariadb://localhost:3306/app_db
spring.datasource.username=root
spring.datasource.password=your_password
```

### 2. Run Application

```bash
./gradlew bootRun
```

Or on Windows:
```bash
gradlew.bat bootRun
```

### 3. Verify Installation

- Application runs on: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui.html
- Health check: http://localhost:8080/actuator/health (if actuator is added)

## First API Call

### Register a User

```bash
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user@example.com",
    "username": "testuser",
    "password": "password123",
    "firstName": "Test",
    "lastName": "User"
  }'
```

### Login

```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "usernameOrEmail": "testuser",
    "password": "password123"
  }'
```

### Use Access Token

```bash
# Replace YOUR_ACCESS_TOKEN with token from login response
curl -X GET http://localhost:8080/profile \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN"
```

## Common Tasks

### Build Project

```bash
./gradlew build
```

### Run Tests

```bash
./gradlew test
```

### Clean Build

```bash
./gradlew clean build
```

### View Dependencies

```bash
./gradlew dependencies
```

## Troubleshooting

### Database Connection Error

- Check MariaDB is running
- Verify credentials in `application.properties`
- Ensure database exists or `createDatabaseIfNotExist=true` is set

### Port Already in Use

Change port in `application.properties`:
```properties
server.port=8081
```

### Flyway Migration Errors

- Check migration files in `src/main/resources/db/migration/`
- Verify database schema matches migrations
- Check Flyway schema history table: `flyway_schema_history`

### JWT Token Issues

- Verify JWT secret in `application.properties`
- Check token expiration times
- Ensure token is sent in Authorization header: `Bearer <token>`

## Next Steps

1. Explore Swagger UI: http://localhost:8080/swagger-ui.html
2. Review module structure in README.md
3. Check API endpoints documentation
4. Review module boundaries and communication patterns


