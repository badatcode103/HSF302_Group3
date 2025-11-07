# Multi-stage build để tối ưu kích thước image
# Stage 1: Build
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copy pom.xml và tải dependencies trước (layer caching)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code và build
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Runtime
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Cài wget cho healthcheck và tạo user không phải root để bảo mật
RUN apk add --no-cache wget && \
    addgroup -S spring && \
    adduser -S spring -G spring

# Chuyển sang user spring
USER spring:spring

# Copy JAR từ stage build
COPY --from=build /app/target/*.jar app.jar

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/ 2>/dev/null || exit 1

# Run application
ENTRYPOINT ["java", "-jar", "app.jar"]

