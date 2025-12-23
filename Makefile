.PHONY: help build up down logs restart clean dev

help: ## Show this help message
	@echo "DTH API Docker Commands:"
	@echo ""
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | awk 'BEGIN {FS = ":.*?## "}; {printf "  \033[36m%-15s\033[0m %s\n", $$1, $$2}'

build: ## Build Docker images
	docker-compose -f docker/docker-compose.yml build --no-cache

up: ## Start all containers
	docker-compose -f docker/docker-compose.yml up -d
	@echo "Waiting for services to be ready..."
	@sleep 5
	@docker-compose -f docker/docker-compose.yml ps

down: ## Stop all containers
	docker-compose -f docker/docker-compose.yml down

logs: ## Show logs from all containers
	docker-compose -f docker/docker-compose.yml logs -f

restart: ## Restart all containers
	docker-compose -f docker/docker-compose.yml restart

clean: ## Stop containers and remove volumes
	docker-compose -f docker/docker-compose.yml down -v
	docker system prune -f

dev: ## Start in development mode (local app + Docker DB only, no Docker build)
	@echo "🛑 Stopping all containers..."
	@docker-compose -f docker/docker-compose.yml down
	@echo "🐬 Starting MariaDB only..."
	@docker-compose -f docker/docker-compose.yml up -d mariadb
	@echo "⏳ Waiting for MariaDB to be ready..."
	@sleep 5
	@echo "🚀 Starting Spring Boot application locally..."
	@echo "   Profile: dev"
	@echo "   Database: localhost:3306"
	@echo "   Note: Running locally, no Docker build needed!"
	@echo ""
	@JAVA17_HOME=""; \
	if [ -f "/opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home/bin/java" ]; then \
		JAVA17_HOME="/opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home"; \
	elif [ -f "/usr/local/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home/bin/java" ]; then \
		JAVA17_HOME="/usr/local/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home"; \
	fi; \
	if [ -n "$$JAVA17_HOME" ]; then \
		echo "✅ Using Java 17: $$JAVA17_HOME"; \
		export JAVA_HOME="$$JAVA17_HOME"; \
		if [ -f "./gradlew" ]; then \
			SPRING_PROFILES_ACTIVE=dev JAVA_HOME="$$JAVA17_HOME" ./gradlew bootRun; \
		elif command -v gradle > /dev/null 2>&1; then \
			echo "⚠️  gradlew not found, using system gradle..."; \
			SPRING_PROFILES_ACTIVE=dev JAVA_HOME="$$JAVA17_HOME" gradle bootRun; \
		else \
			echo "❌ Error: Neither gradlew nor gradle command found!"; \
			exit 1; \
		fi; \
	else \
		echo "⚠️  Java 17 not found, using current Java (may cause issues with Java 25)"; \
		echo "   Install Java 17: brew install openjdk@17"; \
		if [ -f "./gradlew" ]; then \
			SPRING_PROFILES_ACTIVE=dev ./gradlew bootRun; \
		elif command -v gradle > /dev/null 2>&1; then \
			echo "⚠️  gradlew not found, using system gradle..."; \
			SPRING_PROFILES_ACTIVE=dev gradle bootRun; \
		else \
			echo "❌ Error: Neither gradlew nor gradle command found!"; \
			exit 1; \
		fi; \
	fi

start: ## One-click start (build + run)
	@bash scripts/start.sh

db-logs: ## Show MariaDB logs
	docker-compose -f docker/docker-compose.yml logs -f mariadb

app-logs: ## Show application logs
	docker-compose -f docker/docker-compose.yml logs -f app

shell: ## Open shell in app container
	docker-compose -f docker/docker-compose.yml exec app sh

db-shell: ## Open MariaDB shell
	docker-compose -f docker/docker-compose.yml exec mariadb mysql -uroot -proot app_db

rebuild: ## Rebuild and restart
	docker-compose -f docker/docker-compose.yml down
	docker-compose -f docker/docker-compose.yml build --no-cache
	docker-compose -f docker/docker-compose.yml up -d

