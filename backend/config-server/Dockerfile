# Stage 1: Build the application
FROM maven:3.9.6-eclipse-temurin-21 AS builder

WORKDIR /app

# Copy file cấu hình và wrapper trước để cache nếu code không thay đổi
COPY .mvn .mvn
COPY mvnw pom.xml ./

# Tải dependencies trước (cache step này tốt)
RUN ./mvnw dependency:go-offline

# Copy phần còn lại
COPY src ./src

# Build ứng dụng, bỏ qua test
RUN ./mvnw clean package -DskipTests

# Stage 2: Run the application
FROM amazoncorretto:21-alpine

WORKDIR /app

# Copy file JAR đã build từ stage trước
COPY --from=builder /app/target/config-server-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8888

ENTRYPOINT ["java", "-jar", "app.jar"]
