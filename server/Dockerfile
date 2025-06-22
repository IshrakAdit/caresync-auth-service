FROM openjdk:17-jdk-alpine

RUN apk add --no-cache bash maven

WORKDIR /app

COPY mvnw .
COPY .mvn .mvn
RUN chmod +x mvnw

COPY . .

EXPOSE 8081

# RUN ./mvnw clean package -DskipTests

CMD ["./mvnw", "spring-boot:run"]
