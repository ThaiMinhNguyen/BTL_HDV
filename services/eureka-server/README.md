# Eureka Server

Eureka Server là dịch vụ Service Discovery trong hệ thống microservices, giúp các service đăng ký và tìm kiếm lẫn nhau. Service này sử dụng Netflix Eureka, một thành phần của Spring Cloud.

## Chức năng chính

- Đăng ký service: Các microservice đăng ký thông tin với Eureka Server khi khởi động
- Service discovery: Giúp các service tìm kiếm và giao tiếp với nhau mà không cần biết chính xác địa chỉ
- Health monitoring: Giám sát trạng thái hoạt động của các service
- Load balancing: Hỗ trợ cân bằng tải giữa các instance của cùng một service
- Self-preservation: Tự bảo vệ khi nhiều service bị ngắt kết nối đột ngột

## Cấu hình

Eureka Server được cấu hình trong `application.properties` hoặc `application.yml`:

```properties
# Tên ứng dụng
spring.application.name=eureka-server

# Cổng mặc định
server.port=8761

# Không đăng ký bản thân làm client
eureka.client.register-with-eureka=false
eureka.client.fetch-registry=false

# Cấu hình máy chủ
eureka.server.enable-self-preservation=true
eureka.server.renewal-percent-threshold=0.85
eureka.server.renewal-interval-in-ms=30000

# URL để các client đăng ký
eureka.client.service-url.defaultZone=http://localhost:8761/eureka/
```

## Cách sử dụng

### Khởi động Eureka Server

```bash
cd services/eureka-server
mvn spring-boot:run
```

### Truy cập Dashboard

Truy cập `http://localhost:8761` để xem dashboard hiển thị:
- Các service đã đăng ký
- Trạng thái hoạt động
- Địa chỉ IP và cổng
- Thông tin health check

### Đăng ký Service với Eureka

Để một service đăng ký với Eureka, thêm các dependency và cấu hình sau:

1. Thêm dependency trong `pom.xml`:
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
```

2. Thêm annotation `@EnableEurekaClient` trong application class:
```java
@SpringBootApplication
@EnableEurekaClient
public class ServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceApplication.class, args);
    }
}
```

3. Cấu hình trong `application.properties`:
```properties
eureka.client.service-url.defaultZone=http://localhost:8761/eureka/
eureka.instance.prefer-ip-address=true
```

## API

Eureka Server cung cấp các REST API để quản lý service registry:
- Danh sách tất cả các service: `GET /eureka/apps`
- Thông tin về một service cụ thể: `GET /eureka/apps/{serviceId}`
- Dashboard: `GET /`

## Cổng

Eureka Server chạy trên cổng 8761 theo mặc định, có thể thay đổi qua biến môi trường `EUREKA_PORT` hoặc trong file cấu hình.
