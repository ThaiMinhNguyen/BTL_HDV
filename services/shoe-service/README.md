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

### ShoeInventory
- `id`: Mã tồn kho
- `shoe_id`: Mã giày
- `size`: Kích cỡ giày
- `color`: Màu sắc
- `quantity_in_stock`: Số lượng tồn kho

### Brand
- `id`: Mã thương hiệu
- `name`: Tên thương hiệu
- `description`: Mô tả thương hiệu
- `logo_url`: URL logo thương hiệu

### Category
- `id`: Mã danh mục
- `name`: Tên danh mục
- `description`: Mô tả danh mục

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

## Tương tác với các Service khác

- **Order Service**: Kiểm tra tồn kho khi tạo đơn hàng
- **Cart Service**: Cung cấp thông tin giày cho giỏ hàng

## Cổng

Shoe Service chạy trên cổng 8082 theo mặc định, có thể thay đổi qua biến môi trường `SHOE_SERVICE_PORT`. 