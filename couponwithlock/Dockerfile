# 첫 번째 스테이지: 빌드
FROM openjdk:17-jdk-slim as build

WORKDIR /app
COPY . .
RUN ./gradlew build -x test

# 두 번째 스테이지: 실행 환경
FROM openjdk:17-jdk-slim

WORKDIR /app
COPY --from=build /app/build/libs/*.jar coupon-app.jar

ENTRYPOINT ["java", "-jar", "coupon-app.jar"]
