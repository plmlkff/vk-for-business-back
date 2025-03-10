all: up

.PHONY: up, down

up:
	./gradlew bootJar
	docker-compose up -d

down:
	docker-compose down

db:
	docker-compose up -d postgres