FROM common-base:latest AS builder
WORKDIR /workspace

COPY order-service/ /workspace/order-service/

WORKDIR /workspace/order-service
RUN chmod +x ./gradlew && ./gradlew build -x test

FROM common-base:latest
WORKDIR /app

COPY --from=builder /workspace/order-service/build/libs/*.jar /app/app.jar

EXPOSE 8082

ENV JAVA_OPTS=""
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar /app/app.jar"]
