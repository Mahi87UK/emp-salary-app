FROM openjdk:13-jdk-alpine
ARG JAR_FILE=target/emp-salary-app-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} emp-salary-app.jar
ENTRYPOINT ["java","-jar","/emp-salary-app.jar"]