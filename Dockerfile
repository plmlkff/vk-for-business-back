FROM alpine/java:21-jdk
WORKDIR /app
COPY ./build/libs/blps-lab-1-0.0.1-SNAPSHOT.jar /app/app.jar
ENTRYPOINT ["java","-jar","/app/app.jar"]