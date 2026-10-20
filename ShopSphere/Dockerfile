# Stage 1: Build the application using Maven & Eclipse Temurin Java 17
FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app

# Copy pom.xml and download project dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy project source code and build executable jar
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Runtime image using lightweight JRE 17
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copy packaged jar from builder stage
COPY --from=builder /app/target/shopnest-ecommerce-1.0.0.jar app.jar

# Expose Spring Boot port
EXPOSE 8080

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
