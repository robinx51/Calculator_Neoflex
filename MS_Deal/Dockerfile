FROM openjdk:17-jdk-alpine

WORKDIR /app

COPY target/MS_Deal-1.0-SNAPSHOT-exec.jar app.jar

EXPOSE 8181

CMD ["java", "-jar", "app.jar"]