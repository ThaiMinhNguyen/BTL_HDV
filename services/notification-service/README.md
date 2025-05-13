# Notification Service

Notification Service quản lý việc gửi thông báo cho người dùng, chủ yếu là gửi email xác nhận đơn hàng.

## Tính năng

- Gửi email xác nhận đơn hàng
- Quản lý template email
- Lưu lịch sử thông báo
- Hỗ trợ nhiều kênh thông báo (hiện tại mới triển khai email)
- Cơ chế thử lại (retry) khi gửi email thất bại

## Mô hình dữ liệu

### Notification
- `id`: Mã thông báo
- `type`: Loại thông báo (EMAIL, SMS, PUSH)
- `recipient`: Người nhận (email, số điện thoại)
- `subject`: Chủ đề thông báo
- `content`: Nội dung thông báo
- `status`: Trạng thái gửi
- `created_at`: Thời gian tạo
- `sent_at`: Thời gian gửi

## API Endpoints

- `POST /api/notifications/email`: Gửi email xác nhận đơn hàng

## Tích hợp

- **SMTP Gmail**: Gửi email thông qua SMTP của Gmail
- Cấu hình trong `application.properties`:
  ```properties
  spring.mail.host=smtp.gmail.com
  spring.mail.port=587
  spring.mail.username=your-email@gmail.com
  spring.mail.password=your-app-password
  spring.mail.properties.mail.smtp.auth=true
  spring.mail.properties.mail.smtp.starttls.enable=true
  ```
- Yêu cầu sử dụng App Password cho Gmail (bật xác thực 2 lớp)

## Tương tác với các Service khác

- **Order Service**: Nhận yêu cầu gửi thông báo khi đơn hàng được tạo
- **User Service**: Lấy thông tin liên hệ của người dùng

## Cổng

Notification Service chạy trên cổng 8085 theo mặc định, có thể thay đổi qua biến môi trường `NOTIFICATION_SERVICE_PORT`. 