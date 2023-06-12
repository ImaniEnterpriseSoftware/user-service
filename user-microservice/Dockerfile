#
# Build stage
#
FROM maven:3.8.6-openjdk-17 AS build
WORKDIR /home/app
COPY src ./src
COPY pom.xml ./
RUN mvn clean package -DskipTests
RUN mkdir -p target && mv target/*.jar target/app.jar

#
# Package stage
#
FROM openjdk:17
COPY --from=build /home/app/target/app.jar /app.jar
EXPOSE 8083
ENTRYPOINT ["java", "-jar", "/app.jar"]
