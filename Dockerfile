FROM gradle:jdk21 AS build

WORKDIR /app

COPY build.gradle.kts settings.gradle.kts /app/
COPY gradle /app/gradle
COPY src /app/src

RUN gradle clean build -x test --no-daemon

FROM bellsoft/liberica-runtime-container:jdk-21-slim-musl

WORKDIR /app

COPY --from=build /app/build/libs/*.jar /app/app.jar

CMD ["sh", "-c", "java -jar /app/app.jar"]