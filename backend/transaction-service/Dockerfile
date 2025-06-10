# Stage 1: Build the application using Maven Wrapper
FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /app

# Copy Maven wrapper and pom.xml first to cache dependencies
COPY .mvn .mvn
COPY mvnw pom.xml ./

# Tải dependencies trước để cache nếu không thay đổi pom.xml
RUN ./mvnw dependency:go-offline

# Copy phần còn lại của source code
COPY src ./src

# Build ứng dụng, bỏ qua test
RUN ./mvnw package -DskipTests

# Stage 2: Run the application
FROM amazoncorretto:21-alpine
WORKDIR /app

# Copy file JAR từ stage build
COPY --from=builder /app/target/transaction-service-0.0.1-SNAPSHOT.jar app.jar

# Mở port 8082
EXPOSE 8082

# Chạy ứng dụng
ENTRYPOINT ["java", "-jar", "app.jar"]
