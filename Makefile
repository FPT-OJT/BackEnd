up-b: ## Start containers (rebuild images)
	podman compose -f docker/docker-compose.yaml --env-file .env up --build -d

up: ## Start containers (background mode)
	podman compose -f docker/docker-compose.yaml --env-file .env up -d

down: ## Stop containers
	podman compose -f docker/docker-compose.yaml --env-file .env down

down-v: ## Stop containers and remove volumes (Fresh Start)
	podman compose -f docker/docker-compose.yaml --env-file .env down -v

up-prod: ## Start containers (background mode)
	podman compose -f docker/docker-compose.prod.yaml --env-file .env up -d

.PHONY: up-b up down down-v