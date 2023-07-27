# Define the base image
FROM openjdk:11-jdk-slim as build

# Set the working directory in the builder image
WORKDIR /app

# Copy over gradle related files
COPY gradlew .
COPY gradle gradle
COPY build.gradle.kts settings.gradle.kts gradle.properties ./

# Copy the actual code
COPY src ./src

# Build the project using Gradle
RUN ./gradlew clean build --no-daemon

# Use a minimal base image to run the built application
FROM openjdk:11-jdk-slim as runtime

WORKDIR /app

# Copy the built jar file from the builder image
COPY --from=build /app/build/libs/*.jar ./app.jar

# Start the application
CMD ["java", "-jar", "/app/app.jar"]

