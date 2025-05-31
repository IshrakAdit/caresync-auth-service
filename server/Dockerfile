FROM eclipse-temurin:21-jdk-alpine

RUN apk add --no-cache bash maven

WORKDIR /app

COPY . .

EXPOSE 8080

CMD ["./mvnw", "spring-boot:run"]
