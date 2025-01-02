# Use the official openjdk image as the base image
FROM openjdk:17-jdk-alpine

# Set the working directory inside the container
WORKDIR /AppDocker

# Copy the JAR file into the container (update the file name accordingly)
COPY target/beeback-0.0.1.jar beeback.jar

# Expose the port the app will run on (e.g., port 8080)
EXPOSE 8080

# Command to run the Java application
CMD ["java", "-jar", "beeback.jar"]