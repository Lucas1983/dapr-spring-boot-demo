FROM common-base:latest
WORKDIR /app
COPY ./inventory-service/build/libs/*.jar /app/app.jar
EXPOSE 8081
ENV JAVA_OPTS=""
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar /app/app.jar"]