FROM adoptopenjdk/openjdk11:jre-11.0.6_10-alpine
ENV APP_NAME=filezipper-service
COPY target/filezipper-0.0.1-SNAPSHOT.jar filezipper-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/filezipper-0.0.1-SNAPSHOT.jar"]
