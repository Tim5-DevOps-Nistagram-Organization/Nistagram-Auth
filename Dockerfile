FROM maven:3.8.1-jdk-11 AS nistagramAuthMicroserviceTest
ARG STAGE=test
WORKDIR /usr/src/server
COPY . .

FROM maven:3.8.1-jdk-11  AS nistagramAuthMicroserviceBuild
ARG STAGE=dev
WORKDIR /usr/src/server
COPY . .
RUN mvn package -Pdev -DskipTests

FROM openjdk:11.0-jdk as nistagramAuthMicroserviceRuntime
COPY --from=nistagramAuthMicroserviceBuild /usr/src/server/target/*.jar nistagram-auth.jar
CMD java -jar nistagram-auth.jar
