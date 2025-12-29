# Build stage
FROM maven:3.9.6-amazoncorretto-21 AS build
WORKDIR /build
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Run stage
FROM amazoncorretto:21-alpine
WORKDIR /app

# Create non-root user for security
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Set Spring profile (can be overridden by environment variable)
ENV SPRING_PROFILES_ACTIVE=prod

COPY --from=build /build/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]