#
# Build stage
#
FROM maven:3.6.0-jdk-11-slim AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -DskipTests=true

#
# Package stage
#
FROM openjdk:11-jre-slim
EXPOSE 8080
COPY --from=build /home/app/target/wordzle-0.0.1-SNAPSHOT.jar /usr/local/lib/wordzle.jar
ENTRYPOINT ["java","-jar","/usr/local/lib/wordzle.jar"]