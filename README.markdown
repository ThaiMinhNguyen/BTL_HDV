# Hệ thống Đặt hàng Trực tuyến - Shoe Store

Hệ thống đặt hàng trực tuyến Shoe Store là ứng dụng microservices cho phép người dùng đăng nhập, xem sản phẩm giày, thêm vào giỏ hàng, đặt hàng, và nhận email xác nhận. Hệ thống được xây dựng bằng **Spring Boot**, **MySQL**, và giao diện người dùng bằng **HTML/JavaScript**.

## Mục lục
- [Tính năng](#tính-năng)
- [Kiến trúc](#kiến-trúc)
- [Yêu cầu](#yêu-cầu)
- [Cài đặt](#cài-đặt)
- [Cấu hình](#cấu-hình)
- [Chạy ứng dụng](#chạy-ứng-dụng)
- [Kiểm tra](#kiểm-tra)
- [API Endpoints](#api-endpoints)
- [Cấu trúc thư mục](#cấu-trúc-thư-mục)

## Tính năng
- **Quản lý người dùng**: Đăng nhập, lưu thông tin khách hàng.
- **Quản lý sản phẩm giày**: Xem danh sách giày, kiểm tra tồn kho theo size và màu sắc.
- **Giỏ hàng**: Thêm, cập nhật, xóa sản phẩm khỏi giỏ hàng.
- **Đặt hàng**: Tạo đơn hàng với kiểm tra tồn kho và thời gian giao hàng (phải sau 2 ngày).
- **Thông báo**: Gửi email xác nhận đơn hàng qua **Gmail SMTP**.
- **Giao diện**: Frontend đơn giản với HTML và JavaScript.

## Kiến trúc
Hệ thống bao gồm các microservices sau, giao tiếp qua REST API:
- **User Service**: Quản lý thông tin và quyền người dùng.
- **Shoe Service**: Quản lý sản phẩm giày và tồn kho.
- **Cart Service**: Quản lý giỏ hàng.
- **Order Service**: Xử lý đơn hàng và tích hợp với các dịch vụ khác.
- **Notification Service**: Gửi email xác nhận qua Gmail SMTP.
- **Eureka Server**: Đăng ký và khám phá dịch vụ.
- **API Gateway**: Cổng API cho frontend.
- **Frontend**: Giao diện người dùng.

![Kiến trúc Microservices](docs/asset/architecture-diagram.png)

Cơ sở dữ liệu: **MySQL** với các bảng riêng cho từng service.

## Yêu cầu
- **Java 17** (cho Spring Boot)
- **Maven** (quản lý dự án)
- **MySQL 8.0** (cơ sở dữ liệu)
- **Gmail Account** (cho gửi email)
- **App Password** cho Gmail (bảo mật 2 lớp)

## Cài đặt
1. **Clone repository**:
   ```bash
   git clone https://github.com/your-username/shoe-store.git
   cd shoe-store
   ```

2. **Cấu hình cơ sở dữ liệu**:
   - Tạo các schema MySQL cho từng service
   - Chạy scripts trong thư mục `scripts/db`

3. **Cấu hình email**:
   - Cập nhật file `services/notification-service/src/main/resources/application.properties` với thông tin Gmail của bạn

## Chạy ứng dụng
1. **Khởi động Eureka Server**:
   ```bash
   cd services/eureka-server
   mvn spring-boot:run
   ```

2. **Khởi động các microservices**:
   ```bash
   # Từ thư mục gốc, mở terminal riêng cho mỗi service
   cd services/user-service
   mvn spring-boot:run
   
   cd services/shoe-service
   mvn spring-boot:run
   
   # Tương tự cho các service khác
   ```

3. **Khởi động API Gateway**:
   ```bash
   cd api-gateway
   mvn spring-boot:run
   ```

4. **Chạy frontend**:
   - Mở file `frontend/index.html` trong trình duyệt hoặc sử dụng server tĩnh

## Kiểm tra
1. **Kiểm tra Eureka Server**:
   - Truy cập `http://localhost:8761` để kiểm tra các dịch vụ đã đăng ký

2. **Đăng nhập và đặt hàng**:
   - Sử dụng tài khoản đã tạo để đăng nhập
   - Thêm giày vào giỏ hàng
   - Tiến hành thanh toán
   - Kiểm tra email xác nhận

## API Endpoints
Tất cả API endpoints có thể được truy cập thông qua API Gateway tại `http://localhost:8080`.

Chi tiết API chủ yếu:
- **User API**: `/api/users`
- **Shoe API**: `/api/shoes`
- **Cart API**: `/api/cart`
- **Order API**: `/api/orders`
- **Notification API**: `/api/notifications`

## Cấu trúc thư mục
```
./
├── README.markdown                 # Tài liệu hướng dẫn
├── docs/                           # Tài liệu
│   ├── asset/                      # Hình ảnh, sơ đồ
│   └── api-specs/                  # Đặc tả API
├── scripts/                        # Scripts tiện ích
│   └── db/                         # Scripts khởi tạo DB
├── services/                       # Các microservices
│   ├── order-service/
│   ├── shoe-service/
│   ├── user-service/
│   ├── cart-service/
│   ├── notification-service/
│   └── eureka-server/
├── api-gateway/                    # API Gateway
└── frontend/                       # Giao diện người dùng
```

## Tác giả
- Nguyễn Thái Minh
- Email: nguyenthaiminh2201@gmail.com

## Giấy phép
Dự án này được phân phối dưới giấy phép MIT. Xem file `LICENSE` để biết thêm chi tiết.