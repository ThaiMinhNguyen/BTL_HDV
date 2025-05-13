# Cart Service

Cart Service quản lý giỏ hàng của người dùng, cho phép thêm, xóa và cập nhật sản phẩm giày trong giỏ hàng.

## Tính năng

- Tạo và quản lý giỏ hàng cho mỗi người dùng
- Thêm sản phẩm giày vào giỏ hàng với size và màu sắc cụ thể
- Cập nhật số lượng sản phẩm trong giỏ hàng
- Xóa sản phẩm khỏi giỏ hàng
- Lấy thông tin giỏ hàng hiện tại
- Kiểm tra tồn kho khi thêm sản phẩm vào giỏ

## Mô hình dữ liệu

### CartItem
- `id`: Mã mục giỏ hàng
- `username`: Tên đăng nhập người dùng
- `shoe_id`: Mã sản phẩm giày
- `size`: Kích cỡ giày
- `color`: Màu sắc
- `quantity`: Số lượng
- `price`: Giá sản phẩm tại thời điểm thêm vào giỏ
- `created_at`: Thời gian tạo
- `updated_at`: Thời gian cập nhật gần nhất

## API Endpoints

- `GET /api/cart/{username}/items`: Lấy tất cả các sản phẩm trong giỏ hàng của người dùng
- `POST /api/cart/{username}/items`: Thêm sản phẩm vào giỏ hàng
- `PUT /api/cart/{username}/items/{itemId}`: Cập nhật số lượng sản phẩm trong giỏ hàng
- `DELETE /api/cart/{username}/items/{itemId}`: Xóa sản phẩm khỏi giỏ hàng
- `DELETE /api/cart/{username}/items`: Xóa tất cả sản phẩm trong giỏ hàng (sau khi đặt hàng thành công)

## Tương tác với các Service khác

- **Shoe Service**: 
  - Lấy thông tin sản phẩm giày khi thêm vào giỏ hàng
  - Kiểm tra tồn kho của size và màu sắc cụ thể
  
- **User Service**: 
  - Xác thực người dùng
  - Lấy thông tin người dùng cho giỏ hàng
  
- **Order Service**: 
  - Cung cấp thông tin giỏ hàng để tạo đơn hàng
  - Nhận thông báo khi đơn hàng được tạo thành công để xóa giỏ hàng

## Cổng

Cart Service chạy trên cổng 8084 theo mặc định, có thể thay đổi qua biến môi trường `CART_SERVICE_PORT`.

## Eureka Client

Cart Service đăng ký với Eureka Server để service discovery, cho phép các service khác dễ dàng tìm thấy và giao tiếp với nó thông qua tên service thay vì địa chỉ IP và cổng cố định.

## Cấu hình

Các cấu hình chính trong `application.properties`:
```properties
# Service Configuration
spring.application.name=cart-service
server.port=8084

# Eureka Client Configuration
eureka.client.service-url.defaultZone=http://localhost:8761/eureka/

# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/cart_db
spring.datasource.username=root
spring.datasource.password=password
spring.jpa.hibernate.ddl-auto=update
``` 