# Define the build stage
FROM openjdk:17-slim AS build
WORKDIR /workspace/app

# Copy the Gradle executable files
COPY gradlew .
COPY gradle gradle
# Provide execution rights to the Gradle wrapper
RUN chmod +x ./gradlew

# Copy the Gradle build file(s)
COPY build.gradle .
COPY settings.gradle .

# Cache dependencies to improve subsequent build speeds
RUN ./gradlew clean build --no-daemon > /dev/null 2>&1 || true

# Copy the source code
COPY src src

# Build the project and skip tests to speed up build
RUN ./gradlew build -x test --no-daemon

# Define the final image
FROM openjdk:17-slim
WORKDIR /app

# Copy the built artifact from the build stage
COPY --from=build /workspace/app/build/libs/*.jar app.jar

# Expose the application port
EXPOSE 8080

# Define the container's entry point
ENTRYPOINT ["java", "-jar", "app.jar"]
