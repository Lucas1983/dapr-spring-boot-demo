FROM common-base:latest
WORKDIR /app
COPY ./order-service/build/libs/*.jar /app/app.jar
EXPOSE 8082
ENV JAVA_OPTS=""
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar /app/app.jar"]