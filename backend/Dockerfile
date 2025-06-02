FROM gradle:8.13-jdk21-alpine AS java_builder

WORKDIR /app

COPY build.gradle.kts settings.gradle.kts ./
COPY gradle gradle
RUN gradle build --no-daemon --dry-run || true

COPY . .

RUN gradle build --no-daemon

# Etapa: Runtime - final
FROM eclipse-temurin:21-jre-alpine-3.21

WORKDIR /app

COPY --from=java_builder /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=prod"]