# Use Maven with Temurin JDK to build the application
FROM maven:3.8.7-eclipse-temurin-17-alpine AS build

# Set the working directory in the container
WORKDIR /app

# Copy the pom.xml and download the dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy the entire project
COPY . .

# Package the application, skipping tests and using the 'ci' profile
RUN mvn clean package -DskipTests -Dspring.profiles.active=ci

# Use the Temurin JDK runtime image to run the application
FROM eclipse-temurin:17-jdk-alpine

# Set the working directory in the container
WORKDIR /app

# Copy the jar file from the build stage
COPY --from=build /app/target/Online-Food-Ordering-0.0.1-SNAPSHOT.jar /app/Online-Food-Ordering.jar

# Expose the port the application runs on
EXPOSE 8080

# Run the jar file
CMD ["java", "-jar", "/app/Online-Food-Ordering.jar"]
