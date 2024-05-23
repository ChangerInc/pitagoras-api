# Use a lightweight Java base image
FROM openjdk:21-slim

# Set working directory
WORKDIR /home/ubuntu/pitagoras-api/

# Copy your application JAR file
COPY target/*.jar app.jar

# Expose port where your application listens (replace 8080 with your actual port)
EXPOSE 8080

EXPOSE 443

EXPOSE 80

# Command to run your Spring Boot application
CMD ["java", "-jar", "app.jar"]
