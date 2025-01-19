@echo off

REM Ожидание запуска Kafka
timeout /t 30

REM Создание топиков
docker exec kafka kafka-topics.sh --create --if-not-exists --bootstrap-server localhost:29092 --replication-factor 2 --partitions 1 --topic finish-registration
docker exec kafka kafka-topics.sh --create --if-not-exists --bootstrap-server localhost:29092 --replication-factor 2 --partitions 1 --topic create-documents
docker exec kafka kafka-topics.sh --create --if-not-exists --bootstrap-server localhost:29092 --replication-factor 2 --partitions 1 --topic send-documents
docker exec kafka kafka-topics.sh --create --if-not-exists --bootstrap-server localhost:29092 --replication-factor 2 --partitions 1 --topic send-ses
docker exec kafka kafka-topics.sh --create --if-not-exists --bootstrap-server localhost:29092 --replication-factor 2 --partitions 1 --topic credit-issued
docker exec kafka kafka-topics.sh --create --if-not-exists --bootstrap-server localhost:29092 --replication-factor 2 --partitions 1 --topic statement-denied

echo Топики успешно созданы.
pause