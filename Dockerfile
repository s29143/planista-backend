# ========= BUILD STAGE =========
FROM gradle:8.14.3-jdk21-alpine AS build
WORKDIR /app

COPY build.gradle.kts settings.gradle.kts gradle.properties ./
COPY gradle ./gradle
RUN gradle --no-daemon dependencies > /dev/null 2>&1 || true

COPY src ./src
RUN gradle --no-daemon clean bootJar

# ========= RUNTIME STAGE =========
FROM eclipse-temurin:21-jre-alpine
ENV TZ=Europe/Warsaw
WORKDIR /app

RUN addgroup -S app && adduser -S app -G app
USER appA

COPY --from=build /app/build/libs/*.jar /app/app.jar

ENV JAVA_OPTS="" \
    SPRING_PROFILES_ACTIVE=prod \
    SERVER_PORT=8080

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=5s --retries=5 \
  CMD wget -qO- http://127.0.0.1:${SERVER_PORT}/actuator/health | grep -q '"status":"UP"' || exit 1

ENTRYPOINT ["sh","-c","exec java $JAVA_OPTS -jar /app/app.jar"]
