# Dockerfile
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/rag-chat-storage-0.0.1-SNAPSHOT.jar app.jar
# Step 4: Disable metrics (this avoids cgroup crash in Docker Desktop for macOS)
ENV MANAGEMENT_METRICS_ENABLE_PROCESS=false
ENV MANAGEMENT_METRICS_ENABLE_JVM=false
ENV MANAGEMENT_METRICS_ENABLE_SYSTEM=false
ENV MANAGEMENT_METRICS_ENABLE_TOMCAT=false

ENTRYPOINT ["java", "-jar", "app.jar"]
