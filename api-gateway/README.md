# API Gateway

API Gateway là điểm truy cập trung tâm của hệ thống, chịu trách nhiệm định tuyến các yêu cầu đến các microservices phù hợp. Service này sử dụng Spring Cloud Gateway và tích hợp với Eureka Server.

## Chức năng

- Định tuyến yêu cầu dựa trên đường dẫn URI
- Cân bằng tải giữa các instance của cùng một service
- Bảo mật và xác thực (JWT)
- CORS (Cross-Origin Resource Sharing)



## Cấu hình định tuyến

API Gateway được cấu hình để định tuyến các yêu cầu đến các microservices sau:

- `/api/orders/**` -> Order Service (cổng 8081)
- `/api/shoes/**` -> Shoe Service (cổng 8082)
- `/api/users/**` -> User Service (cổng 8083)
- `/api/cart/**` -> Cart Service (cổng 8084)
- `/api/notifications/**` -> Notification Service (cổng 8085)

## Cấu hình trong application.yml

```yaml
spring:
  application:
    name: api-gateway
  cloud:
    gateway:
      discovery:
        locator:
          enabled: true
          lower-case-service-id: true
      routes:
        - id: order-service
          uri: lb://order-service
          predicates:
            - Path=/api/orders/**
        - id: shoe-service
          uri: lb://shoe-service
          predicates:
            - Path=/api/shoes/**
        - id: user-service
          uri: lb://user-service
          predicates:
            - Path=/api/users/**
        - id: cart-service
          uri: lb://cart-service
          predicates:
            - Path=/api/cart/**
        - id: notification-service
          uri: lb://notification-service
          predicates:
            - Path=/api/notifications/**

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
  instance:
    prefer-ip-address: true

server:
  port: 8080
```

## Cách sử dụng

### Khởi động API Gateway

```bash
cd api-gateway
mvn spring-boot:run
```

### Truy cập Microservices

Tất cả các yêu cầu có thể được gửi qua API Gateway, ví dụ:
- Lấy thông tin người dùng: `GET http://localhost:8080/api/users/username`
- Lấy danh sách giày: `GET http://localhost:8080/api/shoes`
- Tạo đơn hàng: `POST http://localhost:8080/api/orders`

API Gateway tự động phát hiện các service đã đăng ký với Eureka Server và định tuyến yêu cầu đến service tương ứng.

## JWT Authentication

API Gateway có thể được cấu hình để xác thực JWT, đảm bảo rằng chỉ có người dùng đã xác thực mới có thể truy cập các endpoint được bảo vệ:

```java
@Component
public class JwtAuthenticationFilter implements GatewayFilter {
    // Implementation of JWT verification
}
```

## CORS Configuration

Cấu hình CORS cho phép yêu cầu từ frontend:

```java
@Configuration
public class CorsConfig {
    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.addAllowedOrigin("*");
        corsConfig.addAllowedMethod("*");
        corsConfig.addAllowedHeader("*");
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);
        
        return new CorsWebFilter(source);
    }
}
```

## Cổng

API Gateway chạy trên cổng 8080 theo mặc định, có thể thay đổi qua biến môi trường `API_GATEWAY_PORT` hoặc trong file cấu hình.

## Mở rộng

API Gateway có thể được mở rộng với các tính năng bổ sung như:
- API Documentation với Swagger/OpenAPI
- Global error handling
- Response transformation
- Request validation
- SSL termination 