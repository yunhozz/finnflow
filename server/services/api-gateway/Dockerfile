FROM eclipse-temurin:24-jdk-alpine AS builder

WORKDIR /app

COPY gradlew settings.gradle.kts build.gradle.kts ./
COPY gradle ./gradle

COPY common/build.gradle.kts ./common/build.gradle.kts
COPY services/api-gateway/build.gradle.kts ./services/api-gateway/build.gradle.kts

RUN chmod +x gradlew
RUN ./gradlew :services:api-gateway:dependencies --no-daemon

COPY common/src ./common/src
COPY services/api-gateway/src ./services/api-gateway/src

RUN ./gradlew :services:api-gateway:clean :services:api-gateway:bootJar --no-daemon

FROM eclipse-temurin:24-jre-alpine

WORKDIR /app

COPY --from=builder /app/services/api-gateway/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]