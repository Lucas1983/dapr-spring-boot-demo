FROM common-base:latest AS builder
WORKDIR /workspace

COPY shipping-service/ /workspace/shipping-service/

WORKDIR /workspace/shipping-service
RUN chmod +x ./gradlew && ./gradlew build -x test

FROM common-base:latest
WORKDIR /app

COPY --from=builder /workspace/shipping-service/build/libs/*.jar /app/app.jar

EXPOSE 8083

ENV JAVA_OPTS=""
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar /app/app.jar"]
