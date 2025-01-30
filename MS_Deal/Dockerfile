FROM openjdk:17-jdk-alpine

WORKDIR /app

# Копирование JAR-файла в контейнер
COPY MS_Deal/target/*.jar app.jar

# Команда для запуска JAR-файла
CMD ["java", "-jar", "app.jar"]