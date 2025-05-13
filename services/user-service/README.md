# User Service

User Service quản lý thông tin người dùng, xác thực và phân quyền trong hệ thống đặt hàng trực tuyến.

## Tính năng

- Đăng ký và đăng nhập người dùng
- Quản lý tài khoản người dùng
- Xác thực và phân quyền
- Lưu trữ thông tin cá nhân của khách hàng
- Kiểm tra quyền truy cập vào các tính năng hệ thống
- Quản lý địa chỉ giao hàng

## Mô hình dữ liệu

### User
- `username`: Tên đăng nhập (Primary Key)
- `password`: Mật khẩu (được mã hóa)
- `role`: Vai trò (CUSTOMER, ADMIN)
- `is_active`: Trạng thái hoạt động
- `email`: Địa chỉ email
- `created_at`: Thời gian tạo
- `last_login`: Thời gian đăng nhập gần nhất

### UserInfo
- `username`: Tên đăng nhập (Foreign Key)
- `full_name`: Họ tên đầy đủ
- `address`: Địa chỉ
- `phone`: Số điện thoại
- `birth_date`: Ngày sinh
- `gender`: Giới tính

### ShippingAddress
- `id`: Mã địa chỉ giao hàng
- `username`: Tên đăng nhập (Foreign Key)
- `recipient_name`: Tên người nhận
- `phone`: Số điện thoại
- `province`: Tỉnh/Thành phố
- `district`: Quận/Huyện
- `ward`: Phường/Xã
- `detail`: Địa chỉ chi tiết
- `is_default`: Là địa chỉ mặc định

## API Endpoints

- `POST /api/users/register`: Đăng ký người dùng mới
- `POST /api/users/login`: Đăng nhập
- `GET /api/users/{username}`: Lấy thông tin người dùng
- `PUT /api/users/{username}`: Cập nhật thông tin người dùng
- `GET /api/users/{username}/permission`: Kiểm tra quyền của người dùng
- `POST /api/users/{username}/info`: Lưu thông tin cá nhân của người dùng
- `GET /api/users/{username}/addresses`: Lấy danh sách địa chỉ giao hàng
- `POST /api/users/{username}/addresses`: Thêm địa chỉ giao hàng mới
- `PUT /api/users/{username}/addresses/{id}`: Cập nhật địa chỉ giao hàng

## Xác thực và Bảo mật

- Sử dụng Spring Security cho xác thực
- Mật khẩu được mã hóa bằng BCrypt
- JWT (JSON Web Token) cho phiên đăng nhập

## Tương tác với các Service khác

- **Order Service**: 
  - Xác thực người dùng khi tạo đơn hàng
  - Cung cấp thông tin địa chỉ giao hàng
  
- **Cart Service**: 
  - Xác thực người dùng khi thao tác với giỏ hàng
  - Liên kết giỏ hàng với người dùng
  
- **Notification Service**: 
  - Cung cấp thông tin liên hệ của người dùng để gửi email

## Cổng

User Service chạy trên cổng 8083 theo mặc định, có thể thay đổi qua biến môi trường `USER_SERVICE_PORT`.

## Eureka Client

User Service đăng ký với Eureka Server để service discovery, cho phép các service khác dễ dàng tìm thấy và giao tiếp với nó thông qua tên service thay vì địa chỉ IP và cổng cố định.

## Cấu hình

Các cấu hình chính trong `application.properties`:
```properties
# Service Configuration
spring.application.name=user-service
server.port=8083

# Eureka Client Configuration
eureka.client.service-url.defaultZone=http://localhost:8761/eureka/

# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/user_db
spring.datasource.username=root
spring.datasource.password=password
spring.jpa.hibernate.ddl-auto=update

# JWT Configuration
jwt.secret=your_jwt_secret_key
jwt.expiration=86400000
```

## Ví dụ Dữ liệu

### Ví dụ User
```json
{
  "username": "customer1",
  "password": "$2a$10$hDgfUQTJbGX5FsZV3kH2w.tOZ/5gHp5UTxwziS3VqO5JOKvs3WKle",
  "role": "CUSTOMER",
  "is_active": true,
  "email": "customer1@example.com"
}
```

### Ví dụ UserInfo
```json
{
  "username": "customer1",
  "full_name": "Nguyễn Văn A",
  "address": "TP. Hồ Chí Minh",
  "phone": "0901234567",
  "birth_date": "1990-01-01",
  "gender": "MALE"
}
``` 