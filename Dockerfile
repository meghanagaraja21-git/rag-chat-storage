# Dockerfile
FROM openjdk:17-jdk-slim
COPY target/rag-chat-storage-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT java -XX:-UseContainerSupport -Djdk.disableLastGCStatistics=true -Djdk.disableContainerSupport=true -jar app.jar