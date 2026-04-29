FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY . .

RUN chmod +x mvnw   # 👈 IMPORTANT FIX

RUN ./mvnw clean install

CMD ["java", "-jar", "target/sim-management-0.0.1-SNAPSHOT.jar"]