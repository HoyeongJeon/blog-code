# Build stage
FROM gradle:8.4.0-jdk17 AS build
WORKDIR /app

# Gradle 캐시 최적화
COPY build.gradle settings.gradle ./
RUN gradle --no-daemon --build-cache dependencies

COPY src ./src
RUN gradle wrapper || true

# 빌드 실행
RUN ./gradlew --no-daemon --build-cache clean build -x test

# Run stage
FROM openjdk:17-jdk-slim
WORKDIR /app

# JAR 복사
COPY --from=build /app/build/libs/*.jar app.jar

# Health Check (선택)
HEALTHCHECK --interval=30s --timeout=3s \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
