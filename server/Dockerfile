FROM openjdk:17-jdk-alpine

RUN apk add --no-cache bash maven

WORKDIR /app

COPY mvnw .
COPY .mvn .mvn
RUN chmod +x mvnw

COPY . .

EXPOSE 8081

CMD ["./mvnw", "spring-boot:run"]

