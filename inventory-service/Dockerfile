FROM common-base:latest AS builder
WORKDIR /workspace

COPY inventory-service/ /workspace/inventory-service/

WORKDIR /workspace/inventory-service
RUN chmod +x ./gradlew && ./gradlew build -x test

FROM common-base:latest
WORKDIR /app

COPY --from=builder /workspace/inventory-service/build/libs/*.jar /app/app.jar

EXPOSE 8081

ENV JAVA_OPTS=""
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar /app/app.jar"]
