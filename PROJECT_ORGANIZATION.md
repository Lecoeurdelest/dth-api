# Project Organization Guide

This document describes the organization of files and directories in the DTH API project.

## 📁 Root Directory Structure

```
dth-api/
├── .gitignore                    # Git ignore rules
├── .dockerignore                 # Docker ignore rules
├── build.gradle.kts              # Gradle build configuration
├── settings.gradle.kts            # Gradle settings
├── gradle.properties             # Gradle properties
├── gradle/                        # Gradle wrapper
│   └── wrapper/
│       └── gradle-wrapper.properties
│
├── src/                           # Source code
│   ├── main/
│   │   ├── java/                  # Java source files
│   │   └── resources/             # Resources (config, migrations)
│   └── test/                      # Test source files
│
├── build/                         # Build output (generated, ignored)
│
├── docker/                        # Docker-related files
│   ├── Dockerfile                 # Production Docker image
│   ├── Dockerfile.dev             # Development Docker image
│   ├── docker-compose.yml         # Main compose file
│   ├── docker-compose.dev.yml     # Development overrides
│
├── scripts/                       # Utility scripts
│   ├── start.sh                   # One-click start script
│   └── start-dev.sh               # Development start script
│
├── config/                        # Configuration templates
│   └── .env.example               # Environment variables template
│
├── docs/                          # Documentation
│   ├── FRON_END.md                # Frontend requirements
│   ├── SPRING_BOOT_IMPLEMENTATION_TASKS.md
│   ├── implementation_rule.md
│   └── technical_design_documentation_rule.md
│
├── README.md                      # Project overview
├── ARCHITECTURE.md                # Architecture documentation
├── MODULE_ORGANIZATION.md         # Module organization guide
├── PROJECT_STRUCTURE.md           # Project structure overview
├── PROJECT_ORGANIZATION.md        # This file
├── QUICK_START.md                 # Quick start guide
├── DOCKER_SETUP.md                # Docker setup guide
├── DOCKER_QUICK_START.md          # Docker quick reference
├── TROUBLESHOOTING.md             # Troubleshooting guide
└── Makefile                       # Make commands
```

## 📂 Directory Details

### Build Files

**Location:** Root directory

- `build.gradle.kts` - Main Gradle build script (Kotlin DSL)
- `settings.gradle.kts` - Gradle project settings
- `gradle.properties` - Gradle configuration properties
- `gradle/wrapper/` - Gradle wrapper files

**Purpose:** Define build configuration, dependencies, and build tasks.

### Source Code

**Location:** `src/`

```
src/
├── main/
│   ├── java/com/example/app/      # Java source code
│   │   ├── Application.java       # Main application class
│   │   ├── {module}/              # Feature modules
│   │   └── shared/                # Shared kernel
│   │
│   └── resources/                 # Resources
│       ├── application.properties          # Main config
│       ├── application-dev.properties      # Development config
│       ├── application-docker.properties   # Docker config
│       └── db/migration/                   # Flyway migrations
│
└── test/                          # Test source code
    ├── java/                      # Test Java files
    └── resources/                 # Test resources
```

### Configuration Files

**Location:** `src/main/resources/`

- `application.properties` - Main application configuration
- `application-dev.properties` - Development profile configuration
- `application-docker.properties` - Docker profile configuration

**Location:** `config/`

- `.env.example` - Environment variables template

**Note:** Sensitive configuration files (like `application-prod.properties`) should be in `.gitignore`.

### Database Migrations

**Location:** `src/main/resources/db/migration/`

- `V{version}__{description}.sql` - Flyway migration files
- Naming convention: `V{number}__{Description}.sql`
- Example: `V1__Create_auth_users_table.sql`

### Docker Files

**Location:** `docker/`

- `Dockerfile` - Production Docker image
- `Dockerfile.dev` - Development Docker image
- `docker-compose.yml` - Main Docker Compose configuration
- `docker-compose.dev.yml` - Development overrides
- `docker-compose.sync.yml` - Docker sync configuration
- `docker-sync.yml` - Docker sync settings

**Note:** `.dockerignore` is in root directory.

### Scripts

**Location:** `scripts/`

- `start.sh` - One-click Docker startup script
- `start-dev.sh` - Development mode startup script

**Usage:**
```bash
./scripts/start.sh
./scripts/start-dev.sh
```

### Documentation

**Location:** Root directory and `docs/`

**Root Level:**
- `README.md` - Project overview and quick start
- `ARCHITECTURE.md` - Architecture documentation
- `MODULE_ORGANIZATION.md` - Module organization guide
- `PROJECT_STRUCTURE.md` - Project structure overview
- `PROJECT_ORGANIZATION.md` - This file
- `QUICK_START.md` - Quick start guide
- `DOCKER_SETUP.md` - Docker setup guide
- `DOCKER_QUICK_START.md` - Docker quick reference
- `TROUBLESHOOTING.md` - Troubleshooting guide

**docs/ Directory:**
- `FRON_END.md` - Frontend requirements
- `SPRING_BOOT_IMPLEMENTATION_TASKS.md` - Implementation tasks
- `implementation_rule.md` - Implementation rules
- `technical_design_documentation_rule.md` - Technical design rules

## 🔒 Ignore Files

### .gitignore

**Location:** Root directory

**Purpose:** Tell Git which files to ignore

**Categories:**
- Build outputs (`build/`, `.gradle/`)
- Compiled files (`*.class`, `*.jar`)
- IDE files (`.idea/`, `.vscode/`)
- OS files (`.DS_Store`, `Thumbs.db`)
- Docker files (`.docker-sync/`)
- Logs (`*.log`, `logs/`)
- Temporary files (`tmp/`, `*.tmp`)
- Secrets (`application-*.properties` with secrets)
- Environment files (`.env`)

### .dockerignore

**Location:** Root directory

**Purpose:** Tell Docker which files to exclude from build context

**Categories:**
- Build outputs
- IDE files
- OS files
- Git files
- Documentation (except README.md)
- Test files
- Temporary files
- Scripts
- Config templates

## 📋 File Naming Conventions

### Java Files
- Classes: `PascalCase.java` (e.g., `UserService.java`)
- Interfaces: `PascalCase.java` (e.g., `UserRepository.java`)
- Tests: `{ClassName}Test.java` (e.g., `UserServiceTest.java`)

### Configuration Files
- Properties: `application-{profile}.properties`
- YAML: `application-{profile}.yml`
- Environment: `.env`, `.env.example`

### Migration Files
- Format: `V{version}__{Description}.sql`
- Example: `V1__Create_auth_users_table.sql`

### Docker Files
- `Dockerfile` - Production
- `Dockerfile.{env}` - Environment-specific (e.g., `Dockerfile.dev`)
- `docker-compose.yml` - Main compose file
- `docker-compose.{override}.yml` - Override files

### Documentation Files
- `README.md` - Main readme
- `{TOPIC}.md` - Topic-specific documentation
- `{TOPIC}_QUICK_START.md` - Quick start guides

### Scripts
- `{action}.sh` - Shell scripts (e.g., `start.sh`)
- Executable permissions required

## 🎯 Best Practices

### 1. Keep Root Directory Clean
- Only essential files in root
- Group related files in subdirectories
- Use descriptive names

### 2. Separate Concerns
- Build files: Root or `gradle/`
- Source code: `src/`
- Configuration: `src/main/resources/` or `config/`
- Documentation: Root or `docs/`
- Scripts: `scripts/`
- Docker: `docker/`

### 3. Version Control
- Commit: Source code, config templates, documentation
- Ignore: Build outputs, IDE files, secrets, logs

### 4. Configuration Management
- Use profiles for different environments
- Keep secrets out of version control
- Use environment variables for sensitive data
- Provide `.env.example` templates

### 5. Documentation
- Keep documentation up to date
- Use clear, descriptive names
- Organize by topic
- Include examples

## 📝 Adding New Files

### Adding a New Module
1. Create module structure in `src/main/java/com/example/app/{module}/`
2. Follow standard module structure
3. Add migration in `src/main/resources/db/migration/`
4. Update documentation

### Adding Configuration
1. Add to `src/main/resources/application-{profile}.properties`
2. Document in relevant documentation
3. Add to `.gitignore` if contains secrets

### Adding Documentation
1. Create `{TOPIC}.md` in root or `docs/`
2. Update `README.md` with reference
3. Keep documentation organized

### Adding Scripts
1. Create in `scripts/` directory
2. Make executable: `chmod +x scripts/{script}.sh`
3. Document usage in README

## 🔍 File Organization Checklist

When organizing files, verify:

- [ ] Build files are in root or `gradle/`
- [ ] Source code is in `src/`
- [ ] Configuration is in `src/main/resources/` or `config/`
- [ ] Docker files are in `docker/`
- [ ] Scripts are in `scripts/`
- [ ] Documentation is organized and up to date
- [ ] `.gitignore` covers all generated files
- [ ] `.dockerignore` excludes unnecessary files
- [ ] Scripts are executable and documented
- [ ] No secrets in version control
- [ ] File names follow conventions
- [ ] Related files are grouped together

## 📚 Related Documentation

- [MODULE_ORGANIZATION.md](./MODULE_ORGANIZATION.md) - Module structure
- [PROJECT_STRUCTURE.md](./PROJECT_STRUCTURE.md) - Project structure
- [ARCHITECTURE.md](./ARCHITECTURE.md) - Architecture details
- [README.md](./README.md) - Project overview
