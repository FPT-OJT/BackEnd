FROM eclipse-temurin:21-jdk AS builder
WORKDIR /app

COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN chmod +x mvnw

RUN ./mvnw dependency:resolve

COPY src ./src
RUN ./mvnw clean package -DskipTests -B

RUN java -Djarmode=layertools -jar target/*.jar extract

FROM eclipse-temurin:21-jre AS runtime
WORKDIR /app

RUN apt-get update && apt-get install -y --no-install-recommends openssl \
    && rm -rf /var/lib/apt/lists/*

RUN mkdir -p /app/keys \
    && openssl genrsa -out /app/keys/private_raw.pem 2048 \
    && openssl pkcs8 -topk8 -inform PEM -outform PEM -nocrypt \
       -in /app/keys/private_raw.pem -out /app/keys/private.pem \
    && rm /app/keys/private_raw.pem \
    && openssl rsa -in /app/keys/private.pem -pubout -out /app/keys/public.pem

COPY --from=builder /app/dependencies/ ./
COPY --from=builder /app/spring-boot-loader/ ./
COPY --from=builder /app/snapshot-dependencies/ ./
COPY --from=builder /app/application/ ./

ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]