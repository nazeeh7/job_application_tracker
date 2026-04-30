# syntax=docker/dockerfile:1

# Build stage with Maven and Java 21
FROM eclipse-temurin:21-jdk-jammy AS build

# Set the working directory inside the container
WORKDIR /build

# Copy Maven wrapper files
COPY mvnw .
COPY .mvn .mvn

# Copy the Maven project file
COPY pom.xml .

# Download dependencies first to improve Docker layer caching
RUN ./mvnw dependency:go-offline -DskipTests

# Copy the application source code
COPY src src

# Build the Spring Boot application
RUN ./mvnw clean package -DskipTests

# Runtime stage with Java 21 JRE only
FROM eclipse-temurin:21-jre-jammy

# Set the working directory inside the container
WORKDIR /app

# Copy the built JAR file from the build stage
COPY --from=build /build/target/*.jar app.jar

# Expose the Spring Boot default port
EXPOSE 8080

# Start the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]