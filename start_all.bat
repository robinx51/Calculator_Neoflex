@echo off

docker-compose build --no-cache

docker-compose up

timeout /t 15

call create_topics.bat

echo Все сервисы запущены и топики созданы.