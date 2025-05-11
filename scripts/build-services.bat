@echo off
set ROOT_DIR=%CD%
echo Building all services...

echo Building Eureka Server...
cd %ROOT_DIR%\services\eureka-server
call mvn clean package -DskipTests
cd %ROOT_DIR%

echo Building API Gateway...
cd %ROOT_DIR%\api-gateway
call mvn clean package -DskipTests
cd %ROOT_DIR%

echo Building Order Service...
cd %ROOT_DIR%\services\order-service
call mvn clean package -DskipTests
cd %ROOT_DIR%

echo Building User Service...
cd %ROOT_DIR%\services\user-service
call mvn clean package -DskipTests
cd %ROOT_DIR%

echo Building Shoe Service...
cd %ROOT_DIR%\services\shoe-service
call mvn clean package -DskipTests
cd %ROOT_DIR%

echo Building Cart Service...
cd %ROOT_DIR%\services\cart-service
call mvn clean package -DskipTests
cd %ROOT_DIR%

echo Building Notification Service...
cd %ROOT_DIR%\services\notification-service
call mvn clean package -DskipTests
cd %ROOT_DIR%

echo All services built successfully!