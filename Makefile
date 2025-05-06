all: up

.PHONY: up, down, db

up: down
	./gradlew clean
	./gradlew bootJar
	docker-compose up --build -d

down:
	docker-compose down

db:
	docker-compose up -d postgres

minio:
	docker-compose up -d minio

kafka:
	docker compose up -d kafka kafka-ui zookeeper kafka-topics-generator

down-kafka:
	docker compose down kafka kafka-ui zookeeper kafka-topics-generator