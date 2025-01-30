FROM openjdk:17-jdk-alpine

WORKDIR /app

COPY target/MS_statement-1.0-SNAPSHOT-exec.jar app.jar

EXPOSE 8182

CMD ["java", "-jar", "app.jar"]