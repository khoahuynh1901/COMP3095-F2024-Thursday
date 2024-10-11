# BUILD STAGE
FROM gradle:8-jdk-21-and-22-alpine AS builder

# Copy source code and set appropriate ownership
COPY --chown=gradle:gradle . /home/gradle/src

# Set working directory to the source directory
WORKDIR /home/gradle/src

# Build the project, excluding tests if necessary
RUN gradle build -x test

# PRODUCTION STAGE
FROM openjdk:22-jdk

# Create the /app directory in the container
RUN mkdir /app

# Copy the built JAR file from the builder stage
COPY --from=builder /home/gradle/src/build/libs/*.jar /app/product-service.jar

# Set environment variables for MongoDB (if required)
ENV MONGO_DB_USERNAME=admin \
    MONGO_DB_PWD=password

# Expose the port the application will run on
EXPOSE 8084

# Define the entry point to run the Spring Boot app
ENTRYPOINT ["java", "-jar", "/app/product-service.jar"]
