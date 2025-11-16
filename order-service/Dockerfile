# Minimal Dockerfile for order-service
FROM eclipse-temurin:21-jdk AS builder
WORKDIR /workspace

# Copy sources needed for build (common-data + target module)
# Expect build context at repository root
COPY common-data/ /workspace/common-data/
COPY order-service/ /workspace/order-service/

# Build shared library and publish to local maven, then build service (skip tests)
WORKDIR /workspace/common-data
RUN chmod +x ./gradlew && ./gradlew -x test publishToMavenLocal
WORKDIR /workspace/order-service
RUN chmod +x ./gradlew && ./gradlew -x test bootJar

FROM eclipse-temurin:21-jre
WORKDIR /app

# Copy the built jar
COPY --from=builder /workspace/order-service/build/libs/*.jar /app/app.jar

# Service port
EXPOSE 8082

ENV JAVA_OPTS=""
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar /app/app.jar"]
