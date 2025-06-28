####################################
# 🔨 Stage 1: Build the Spring Boot JAR
####################################
FROM maven:3.9.6-eclipse-temurin-21 AS builder

LABEL maintainer="zeogrid"
WORKDIR /app

# Copy the pom.xml and src directory into the build container
COPY zeover/pom.xml .
COPY zeover/src ./src

# Optional: Pre-fetch dependencies to cache them
RUN mvn dependency:go-offline

# Build the application
RUN mvn clean package -DskipTests


####################################
# 🚀 Stage 2: Run the JAR
####################################
FROM openjdk:21-jdk-slim

WORKDIR /app

# Copy the JAR from the build stage
COPY --from=builder /app/target/*.jar app.jar

# Expose the application port
EXPOSE 8938

# Start the application
ENTRYPOINT ["java", "-jar", "app.jar"]
