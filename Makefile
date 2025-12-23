.PHONY: help build up down logs restart clean dev dev-watcher

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
	@echo "🚀 Starting development mode with auto-compile watcher..."
	@echo "   Profile: dev (with DevTools hot reload enabled)"
	@echo "   Database: localhost:3306"
	@echo "   Hot reload: Save file -> Auto compile -> DevTools auto-restart (~2-5s)"
	@echo ""
	@bash scripts/dev-with-watcher.sh

dev-watcher: dev ## Alias for 'make dev' (kept for backward compatibility)
	@echo ""
	@echo "💡 Note: 'make dev-watcher' is now the same as 'make dev'"
	@echo "   Auto-compile watcher is built-in to 'make dev'"

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

