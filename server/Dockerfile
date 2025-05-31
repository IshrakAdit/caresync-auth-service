# Dockerfile
FROM openjdk:21-jdk-alpine

# Install bash & maven wrapper support
RUN apk add --no-cache bash maven

# Set working directory
WORKDIR /app

# Copy everything into the container
COPY . .

# Expose app port
EXPOSE 8080

# Run the app using Maven wrapper
CMD ["./mvnw", "spring-boot:run"]
