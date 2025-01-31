FROM openjdk:17-jdk-alpine

WORKDIR /app

COPY target/MS_Dossier-1.0-SNAPSHOT-exec.jar app.jar

EXPOSE 8183

CMD ["java", "-jar", "app.jar"]