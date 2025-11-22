FROM common-base:latest AS builder
WORKDIR /workspace

COPY customer-service/ /workspace/customer-service/

WORKDIR /workspace/customer-service
RUN chmod +x ./gradlew && ./gradlew build -x test

FROM common-base:latest
WORKDIR /app

COPY --from=builder /workspace/customer-service/build/libs/*.jar /app/app.jar

EXPOSE 8084

ENV JAVA_OPTS=""
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar /app/app.jar"]
