FROM openjdk:17-jdk-alpine

# Install necessary tools
RUN apk add --no-cache bash maven

# Set working directory
WORKDIR /app

# Copy wrapper files and give execute permission first (avoid invalidating cache)
COPY mvnw .
COPY .mvn .mvn
RUN chmod +x mvnw

# Copy remaining source files
COPY . .

# Optional: Build with Maven (faster in builder step than runtime)
RUN ./mvnw clean package -DskipTests

# Expose the default Spring Boot port
EXPOSE 8081

# Run the packaged JAR instead of re-invoking Maven at runtime
CMD ["java", "-jar", "target/*.jar"]
