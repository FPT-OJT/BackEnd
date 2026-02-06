
FROM maven:3.9.12-eclipse-temurin-25-noble AS build

WORKDIR /app

COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .

RUN ./mvnw -B dependency:go-offline

COPY src src

RUN ./mvnw -B -q clean package -DskipTests

FROM eclipse-temurin:25-jre-ubi10-minimal

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENV JAVA_OPTS=""

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
