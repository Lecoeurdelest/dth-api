# syntax=docker/dockerfile:1.4
# Multi-stage build for Spring Boot application
FROM gradle:8.5-jdk17 AS build

WORKDIR /app

# Copy Gradle files first for better caching
COPY build.gradle.kts settings.gradle.kts gradle.properties ./
COPY gradle ./gradle

# Use BuildKit cache mount for Gradle dependencies (much faster!)
# This persists Gradle cache between builds
RUN --mount=type=cache,target=/root/.gradle/caches \
    --mount=type=cache,target=/root/.gradle/wrapper \
    gradle dependencies --no-daemon || true

# Copy source code
COPY src ./src

# Build without clean (incremental build) - much faster!
# Use cache mounts for Gradle cache
RUN --mount=type=cache,target=/root/.gradle/caches \
    --mount=type=cache,target=/root/.gradle/wrapper \
    gradle build --no-daemon -x test --parallel

# Verify JAR was created
RUN ls -la /app/build/libs/ || (echo "ERROR: No JAR files found!" && exit 1)

# Find and copy the executable JAR (exclude sources and javadoc JARs)
# Spring Boot creates: app-0.0.1-SNAPSHOT.jar (executable) and app-0.0.1-SNAPSHOT-sources.jar, etc.
RUN set -e && \
    echo "Checking build/libs directory:" && \
    ls -la /app/build/libs/ && \
    JAR_FILE=$(find /app/build/libs -name "*.jar" -not -name "*-sources.jar" -not -name "*-javadoc.jar" | head -1) && \
    if [ -z "$JAR_FILE" ] || [ ! -f "$JAR_FILE" ]; then \
        echo "ERROR: No executable JAR found in build/libs/"; \
        echo "Files in build/libs/:"; \
        ls -la /app/build/libs/ || echo "Directory does not exist"; \
        exit 1; \
    fi && \
    echo "Found executable JAR: $JAR_FILE" && \
    cp "$JAR_FILE" /app/app.jar && \
    ls -lh /app/app.jar && \
    echo "JAR file copied successfully"

# Runtime stage
FROM eclipse-temurin:17-jre

WORKDIR /app

# Install curl for health check
RUN apt-get update && \
    apt-get install -y curl && \
    rm -rf /var/lib/apt/lists/*

# Copy the built JAR from build stage
COPY --from=build /app/app.jar app.jar

# Expose port
EXPOSE 8080

# Health check (using root endpoint if actuator is not available)
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD curl -f http://localhost:8080/ || curl -f http://localhost:8080/actuator/health || exit 1

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]

