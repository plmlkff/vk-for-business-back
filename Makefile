all: up

.PHONY: up, down, db

up:
	./gradlew bootJar
	docker-compose up -d

down:
	docker-compose down

db:
	docker-compose up -d postgres