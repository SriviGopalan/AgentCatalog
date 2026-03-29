# ── Stage 1: Build React frontend ─────────────────────────────
FROM node:18-alpine AS frontend-build
WORKDIR /app/frontend
COPY frontend/package*.json ./
RUN npm ci --silent
COPY frontend/ ./
RUN npm run build

# ── Stage 2: Build Spring Boot JAR ────────────────────────────
FROM maven:3.9-eclipse-temurin-21 AS backend-build
WORKDIR /app
COPY pom.xml ./
# Pre-download dependencies (cached layer)
RUN mvn dependency:go-offline -q
COPY src ./src
# Inject React build into Spring Boot static resources
COPY --from=frontend-build /app/frontend/dist ./src/main/resources/static
RUN mvn package -DskipTests -q

# ── Stage 3: Minimal runtime image ────────────────────────────
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=backend-build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]