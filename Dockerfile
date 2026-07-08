FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY gradlew settings.gradle build.gradle ./
COPY gradle ./gradle
RUN chmod +x ./gradlew

COPY src ./src

RUN ./gradlew clean bootJar --no-daemon

EXPOSE 3000

CMD ["java", "-jar", "build/libs/daniel-community-0.0.1-SNAPSHOT.jar"]
