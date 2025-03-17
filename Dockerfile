FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/db-migration-tool-1.0-SNAPSHOT.jar /app/db-migration-tool.jar

COPY src/main/resources/application.properties /app/application.properties

CMD ["java", "-jar", "/app/db-migration-tool.jar", "--config=/app/application.properties", "--migrations=src/main/resources/migrations"]