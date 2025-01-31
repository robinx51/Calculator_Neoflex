FROM openjdk:17-jdk-alpine

WORKDIR /app

COPY target/MS_Gateway-1.0-SNAPSHOT-exec.jar app.jar

EXPOSE 8184

CMD ["java", "-jar", "app.jar"]