# Use an official Maven image to build the application
FROM maven:3.8.5-openjdk-17 AS build

# Set the working directory in the container
WORKDIR /app

# Copy the Maven settings.xml if updated
# COPY settings.xml /root/.m2/settings.xml

# Copy the pom.xml and source code into the container
COPY pom.xml .
COPY src ./src

# Package the application
RUN mvn clean package

# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the packaged jar file from the build stage
COPY --from=build /app/target/IoTdata-0.0.1-SNAPSHOT.jar /app/IoTdata-0.0.1-SNAPSHOT.jar

# Make port 8080 available to the world outside this container
EXPOSE 8080

# Define environment variable
ENV NAME IoTdata

# Run the application
CMD ["java", "-jar", "IoTdata-0.0.1-SNAPSHOT.jar"]
