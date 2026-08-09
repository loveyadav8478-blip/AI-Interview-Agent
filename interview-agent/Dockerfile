# ---------- Build Stage ----------
FROM eclipse-temurin:21-jdk AS build

WORKDIR /app

# Copy Maven wrapper / project files first for better Docker layer caching
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

RUN chmod +x mvnw

# Download dependencies
RUN ./mvnw dependency:go-offline -B

# Copy source code
COPY src src

# Build application
RUN ./mvnw clean package -DskipTests


# ---------- Runtime Stage ----------
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copy the generated Spring Boot JAR
COPY --from=build /app/target/*.jar app.jar

# Render provides the PORT environment variable
EXPOSE 8080

# Start Spring Boot
ENTRYPOINT ["sh", "-c", "java -Dserver.port=${PORT:-8080} -jar app.jar"]