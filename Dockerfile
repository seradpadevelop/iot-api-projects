FROM openjdk:11.0.1-jdk-stretch as builder
ARG SONAR_HOST
ARG SONAR_TOKEN
WORKDIR builder
COPY . .
RUN ./gradlew
RUN ./build_app.sh

FROM openjdk:11.0.1-jre-slim-sid
WORKDIR /app
COPY --from=builder /builder/build/libs/iot-api-projects-0.1.0.jar iot-api-projects-0.1.0.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom -Dspring.profiles.active=ibmcloud-dev","-jar","iot-api-projects-0.1.0.jar"]
