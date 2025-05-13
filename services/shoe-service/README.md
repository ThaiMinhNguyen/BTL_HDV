# Shoe Service

Shoe Service quản lý thông tin giày và kiểm tra tồn kho giày trong hệ thống đặt hàng trực tuyến.

## Tính năng

- Quản lý danh mục giày
- Cung cấp thông tin chi tiết giày
- Kiểm tra tồn kho giày
- Cập nhật số lượng tồn kho giày sau khi đặt hàng
- Quản lý thông tin thương hiệu và danh mục
- Hỗ trợ tìm kiếm giày theo kích cỡ, màu sắc, giới tính

## Mô hình dữ liệu

### Shoe
- `id`: Mã giày
- `name`: Tên giày
- `description`: Mô tả giày
- `price`: Giá giày
- `brand_id`: Mã thương hiệu
- `category_id`: Mã danh mục
- `image_url`: URL hình ảnh giày
- `material`: Chất liệu
- `gender`: Giới tính (MEN, WOMEN, UNISEX)
- `created_at`: Thời gian tạo
- `updated_at`: Thời gian cập nhật gần nhất

### ShoeInventory
- `id`: Mã tồn kho
- `shoe_id`: Mã giày
- `size`: Kích cỡ giày
- `color`: Màu sắc
- `quantity_in_stock`: Số lượng tồn kho
- `last_updated`: Thời gian cập nhật tồn kho gần nhất

### Brand
- `id`: Mã thương hiệu
- `name`: Tên thương hiệu
- `description`: Mô tả thương hiệu
- `logo_url`: URL logo thương hiệu
- `country`: Quốc gia xuất xứ

### Category
- `id`: Mã danh mục
- `name`: Tên danh mục
- `description`: Mô tả danh mục
- `parent_id`: Mã danh mục cha (nếu có)

## API Endpoints

- `GET /api/shoes`: Lấy danh sách tất cả giày
- `GET /api/shoes/{id}`: Lấy thông tin chi tiết của một đôi giày
- `GET /api/shoes/brands/{brandId}`: Lấy giày theo thương hiệu
- `GET /api/shoes/categories/{categoryId}`: Lấy giày theo danh mục
- `GET /api/shoes/gender/{gender}`: Lấy giày theo giới tính
- `GET /api/shoes/price`: Lấy giày theo khoảng giá
- `GET /api/shoes/{id}/inventory`: Lấy thông tin tồn kho của giày
- `GET /api/shoes/check`: Kiểm tra tính khả dụng của giày
- `PUT /api/shoes/{id}/updateInventory`: Cập nhật số lượng tồn kho
- `POST /api/shoes`: Thêm giày mới (Admin)
- `PUT /api/shoes/{id}`: Cập nhật thông tin giày (Admin)

### API Brands và Categories
- `GET /api/brands`: Lấy danh sách thương hiệu
- `GET /api/categories`: Lấy danh sách danh mục

## Tương tác với các Service khác

- **Order Service**: 
  - Kiểm tra tồn kho khi tạo đơn hàng
  - Cập nhật tồn kho khi đơn hàng được xác nhận
  
- **Cart Service**: 
  - Cung cấp thông tin giày cho giỏ hàng
  - Kiểm tra tồn kho khi thêm vào giỏ hàng

## Cổng

Shoe Service chạy trên cổng 8082 theo mặc định, có thể thay đổi qua biến môi trường `SHOE_SERVICE_PORT`.

## Eureka Client

Shoe Service đăng ký với Eureka Server để service discovery, cho phép các service khác dễ dàng tìm thấy và giao tiếp với nó thông qua tên service thay vì địa chỉ IP và cổng cố định.

## Cấu hình

Các cấu hình chính trong `application.properties`:
```properties
# Service Configuration
spring.application.name=shoe-service
server.port=8082

# Eureka Client Configuration
eureka.client.service-url.defaultZone=http://localhost:8761/eureka/

# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/shoe_db
spring.datasource.username=root
spring.datasource.password=password
spring.jpa.hibernate.ddl-auto=update

# Image Storage Configuration
shoe.image.storage.location=uploads/images/shoes
```

## Ví dụ Dữ liệu

### Ví dụ Giày
```json
{
  "id": 1,
  "name": "Air Jordan 1 Retro High",
  "description": "Giày bóng rổ cổ cao Air Jordan 1",
  "price": 3200000,
  "brand_id": 1,
  "category_id": 2,
  "image_url": "/images/shoes/jordan1.jpg",
  "material": "Leather",
  "gender": "UNISEX"
}
```

### Ví dụ Tồn kho
```json
{
  "id": 1,
  "shoe_id": 1,
  "size": 42,
  "color": "Black/Red",
  "quantity_in_stock": 15
}
``` 