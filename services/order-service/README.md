# Order Service

Order Service quản lý toàn bộ quá trình đặt hàng, từ tạo đơn hàng đến kiểm tra tính hợp lệ và lưu trữ thông tin.

## Tính năng

- Tạo đơn hàng mới
- Kiểm tra ngày giao hàng (phải sau ít nhất 2 ngày)
- Kiểm tra tồn kho của sản phẩm giày (size, màu sắc)
- Lưu trữ thông tin đơn hàng và chi tiết đơn hàng
- Gửi thông báo email xác nhận thông qua Notification Service
- Thông báo cho Cart Service để xóa giỏ hàng sau khi đặt hàng thành công

## Mô hình dữ liệu

### Order
- `id`: Mã đơn hàng
- `customerUsername`: Tên đăng nhập khách hàng
- `totalPrice`: Tổng giá trị đơn hàng
- `deliveryDate`: Ngày giao hàng
- `shippingAddress`: Địa chỉ giao hàng
- `status`: Trạng thái đơn hàng (PENDING, CONFIRMED, SHIPPED, DELIVERED, CANCELLED)
- `createdAt`: Thời gian tạo
- `updatedAt`: Thời gian cập nhật gần nhất

### OrderItem
- `id`: Mã chi tiết đơn hàng
- `orderId`: Mã đơn hàng
- `shoeId`: Mã sản phẩm giày
- `size`: Kích cỡ giày
- `color`: Màu sắc
- `quantity`: Số lượng
- `unitPrice`: Đơn giá

## API Endpoints

- `POST /api/orders`: Tạo đơn hàng mới
- `GET /api/orders/{id}`: Lấy thông tin đơn hàng theo ID
- `GET /api/orders/user/{username}`: Lấy danh sách đơn hàng của người dùng
- `PUT /api/orders/{id}/status`: Cập nhật trạng thái đơn hàng

## Tương tác với các Services khác

- **Shoe Service**: 
  - Kiểm tra tồn kho giày (size, màu) 
  - Cập nhật số lượng sau khi đặt hàng
  - Lấy thông tin chi tiết sản phẩm để hiển thị trong đơn hàng

- **User Service**: 
  - Xác thực người dùng 
  - Lấy thông tin liên hệ và địa chỉ giao hàng

- **Cart Service**: 
  - Lấy thông tin giỏ hàng để tạo đơn hàng
  - Thông báo xóa giỏ hàng sau khi đặt hàng thành công

- **Notification Service**: 
  - Gửi email xác nhận đơn hàng với thông tin chi tiết
  - Gửi thông báo khi trạng thái đơn hàng thay đổi

## Quy trình đặt hàng

1. Kiểm tra thông tin người dùng qua User Service
2. Lấy thông tin giỏ hàng từ Cart Service
3. Kiểm tra tồn kho của từng sản phẩm qua Shoe Service
4. Kiểm tra ngày giao hàng (phải sau ít nhất 2 ngày kể từ ngày đặt)
5. Tạo đơn hàng mới và lưu vào cơ sở dữ liệu
6. Cập nhật số lượng tồn kho qua Shoe Service
7. Gửi yêu cầu xóa giỏ hàng đến Cart Service
8. Gửi email xác nhận qua Notification Service

## Cổng

Order Service chạy trên cổng 8081 theo mặc định, có thể thay đổi qua biến môi trường `ORDER_SERVICE_PORT`.

## Eureka Client

Order Service đăng ký với Eureka Server để service discovery, cho phép các service khác dễ dàng tìm thấy và giao tiếp với nó thông qua tên service thay vì địa chỉ IP và cổng cố định.

## Cấu hình

Các cấu hình chính trong `application.properties`:
```properties
# Service Configuration
spring.application.name=order-service
server.port=8081

# Eureka Client Configuration
eureka.client.service-url.defaultZone=http://localhost:8761/eureka/

# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/order_db
spring.datasource.username=root
spring.datasource.password=password
spring.jpa.hibernate.ddl-auto=update

# Business Rules
order.minimum.delivery.days=2
``` 