# Use an official Maven image with Java 21 to build the project
FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app

# Copy pom.xml and download dependencies (caching layer)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the entire project and build the JAR
COPY . .
RUN mvn clean package -DskipTests

# Use a lightweight JDK 21 image for running the app
FROM eclipse-temurin:21-jdk

WORKDIR /app

# Copy the built JAR from the previous stage
COPY --from=build /app/target/*.jar app.jar

# Expose port 8080 (Spring Boot default)
EXPOSE 8080

# Ensure the correct entrypoint to prevent JShell from running
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
