# === STAGE 1: Build the application ===
FROM maven:3.8.5-openjdk-17-slim AS build
WORKDIR /app

# Copy everything into the image
COPY . .

# Build the project and skip tests to save time
RUN mvn clean package -DskipTests

# === STAGE 2: Run the application ===
FROM openjdk:17-jdk-alpine
WORKDIR /app

# Copy only the JAR file from the first stage
COPY --from=build /app/target/*.jar app.jar

# Expose the port your app runs on (match your application.properties: 8080)
EXPOSE 8080

# Run the JAR file
ENTRYPOINT ["java", "-jar", "app.jar"]
