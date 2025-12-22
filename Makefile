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

dev: ## Start in development mode with hot-reload
	@bash scripts/start-dev.sh

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

