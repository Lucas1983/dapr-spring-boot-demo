FROM eclipse-temurin:21-jdk AS builder
WORKDIR /workspace

COPY common-data/ /workspace/common-data/

WORKDIR /workspace/common-data
RUN chmod +x ./gradlew && ./gradlew -x test publishToMavenLocal
