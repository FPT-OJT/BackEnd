dev-up-b: ## Start containers (rebuild images)
	podman compose -f docker/dev/docker-compose.yaml --env-file .env up --build -d

dev-up: ## Start containers (background mode)
	podman compose -f docker/dev/docker-compose.yaml --env-file .env up -d

dev-down: ## Stop containers
	podman compose -f docker/dev/docker-compose.yaml --env-file .env down

dev-down-v: ## Stop containers and remove volumes (Fresh Start)
	podman compose -f docker/dev/docker-compose.yaml --env-file .env down -v

reset: dev-down-v dev-up-b

prod-up-b: ## Start prod containers (rebuild images)
	podman compose -f docker/prod/docker-compose.yaml --env-file .env up --build -d

prod-up: ## Start prod containers (background mode)
	podman compose -f docker/prod/docker-compose.yaml --env-file .env up -d

prod-down: ## Stop prod containers
	podman compose -f docker/prod/docker-compose.yaml --env-file .env down

prod-down-v: ## Stop prod containers and remove volumes (Fresh Start)
	podman compose -f docker/prod/docker-compose.yaml --env-file .env down -v

prod-reset: prod-down-v prod-up-b

.PHONY: dev-up-b dev-up dev-down dev-down-v reset prod-up-b prod-up prod-down prod-down-v prod-reset
