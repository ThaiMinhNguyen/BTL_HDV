#!/bin/bash

# Khởi tạo hệ thống đặt hàng trực tuyến
echo "Initializing Online Ordering System..."

# Kiểm tra Docker
if ! command -v docker &> /dev/null; then
    echo "Docker không được cài đặt. Vui lòng cài đặt Docker trước."
    exit 1
fi

# Kiểm tra Docker Compose
if ! command -v docker-compose &> /dev/null; then
    echo "Docker Compose không được cài đặt. Vui lòng cài đặt Docker Compose trước."
    exit 1
fi

# Kiểm tra Maven
if ! command -v mvn &> /dev/null; then
    echo "Maven không được cài đặt. Vui lòng cài đặt Maven trước."
    exit 1
fi

# Tạo file .env từ .env.example nếu chưa tồn tại
if [ ! -f .env ]; then
    echo "Tạo file .env từ .env.example..."
    cp .env.example .env
    echo "Vui lòng chỉnh sửa file .env nếu cần thiết."
fi

# Build các service
echo "Building all services..."

echo "Building Eureka Server..."
cd services/eureka-server
mvn clean package -DskipTests
cd ../..

echo "Building API Gateway..."
cd api-gateway
mvn clean package -DskipTests
cd ..

echo "Building Order Service..."
cd services/order-service
mvn clean package -DskipTests
cd ../..

echo "Building User Service..."
cd services/user-service
mvn clean package -DskipTests
cd ../..

echo "Building Shoe Service..."
cd services/shoe-service
mvn clean package -DskipTests
cd ../..

echo "Building Cart Service..."
cd services/cart-service
mvn clean package -DskipTests
cd ../..

echo "Building Notification Service..."
cd services/notification-service
mvn clean package -DskipTests
cd ../..

# Xây dựng và khởi động các services
echo "Xây dựng và khởi động các services..."
docker-compose up -d --build

# Kiểm tra trạng thái
echo "Kiểm tra trạng thái các services..."
docker-compose ps

echo "Khởi tạo hoàn tất!"
echo "Truy cập ứng dụng tại: http://localhost"
echo "Eureka Server tại: http://localhost:8761"