FROM openjdk:17-jdk-alpine

RUN apk add --no-cache bash maven

WORKDIR /app

COPY . .

EXPOSE 8081

CMD ["./mvnw", "spring-boot:run"]

