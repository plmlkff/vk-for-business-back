all: up

.PHONY: up, down, db

up: down
	./gradlew clean
	./gradlew bootJar
	docker-compose up --build -d

down:
	docker-compose down

db: down
	docker-compose up -d postgres