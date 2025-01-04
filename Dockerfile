FROM eclipse-temurin:17-jdk-alpine

# Set the working directory in the container
WORKDIR /app

# Copy the packaged jar file into the container
COPY target/Online-Food-Ordering-0.0.1-SNAPSHOT.jar /app/Online-Food-Ordering.jar

# Expose the port the application runs on
EXPOSE 8080

# Run the jar file
ENTRYPOINT ["java", "-jar", "Online-Food-Ordering.jar"]
