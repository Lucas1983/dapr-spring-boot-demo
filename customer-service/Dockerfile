FROM common-base:latest
WORKDIR /app
COPY ./customer-service/build/libs/*.jar /app/app.jar
EXPOSE 8084
ENV JAVA_OPTS=""
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar /app/app.jar"]
