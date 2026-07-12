# Stage 1: Build
FROM maven:3.8.5-openjdk-17-slim AS build
WORKDIR /app
COPY pom.xml .
# Download dependencies first for caching
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Runtime
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/placement-platform-0.0.1-SNAPSHOT.jar app.jar
# Ensure uploads dir exists
RUN mkdir -p uploads/resumes
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
