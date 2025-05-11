-- Tạo database
CREATE DATABASE IF NOT EXISTS shoe_order_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Sử dụng shoe_order_db
USE shoe_order_db;

-- Tạo bảng users
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    is_active BOOLEAN NOT NULL,
    email VARCHAR(255) NOT NULL,
    address VARCHAR(255),
    phone VARCHAR(20),
    name VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_email (email),
    INDEX idx_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tạo bảng Brands (Thương hiệu giày)
CREATE TABLE brands (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    logo_url VARCHAR(255),
    INDEX idx_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Tạo bảng Categories (Loại giày)
CREATE TABLE categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    INDEX idx_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Tạo bảng Shoes (Sản phẩm giày)
CREATE TABLE shoes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price DECIMAL(10, 2) NOT NULL,
    brand_id BIGINT NOT NULL,
    category_id BIGINT NOT NULL,
    image_url VARCHAR(255),
    material VARCHAR(100),
    gender ENUM('MEN', 'WOMEN', 'UNISEX') NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (brand_id) REFERENCES brands(id) ON DELETE RESTRICT ON UPDATE CASCADE,
    FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE RESTRICT ON UPDATE CASCADE,
    INDEX idx_name (name),
    INDEX idx_brand (brand_id),
    INDEX idx_category (category_id),
    INDEX idx_gender (gender)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tạo bảng ShoeInventory (Kho giày theo size và màu)
CREATE TABLE shoe_inventory (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    shoe_id BIGINT NOT NULL,
    size DECIMAL(3, 1) NOT NULL,
    color VARCHAR(50) NOT NULL,
    quantity_in_stock INT NOT NULL,
    FOREIGN KEY (shoe_id) REFERENCES shoes(id) ON DELETE CASCADE ON UPDATE CASCADE,
    UNIQUE KEY idx_shoe_size_color (shoe_id, size, color),
    INDEX idx_size (size),
    INDEX idx_color (color)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tạo bảng Orders
CREATE TABLE orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_username VARCHAR(255) NOT NULL,
    total_price DECIMAL(10, 2) NOT NULL,
    delivery_date DATETIME NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    shipping_address VARCHAR(255) NOT NULL,
    payment_method VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_username) REFERENCES users(username) ON DELETE RESTRICT ON UPDATE CASCADE,
    INDEX idx_customer_username (customer_username),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tạo bảng OrderItems
CREATE TABLE orderitems (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT NOT NULL,
    shoe_id BIGINT NOT NULL,
    size DECIMAL(3, 1) NOT NULL,
    color VARCHAR(50) NOT NULL,
    quantity INT NOT NULL,
    unit_price DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (shoe_id) REFERENCES shoes(id) ON DELETE RESTRICT ON UPDATE CASCADE,
    INDEX idx_order_id (order_id),
    INDEX idx_shoe_id (shoe_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tạo bảng CartItems
CREATE TABLE cartitems (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    shoe_id BIGINT NOT NULL,
    size DECIMAL(3, 1) NOT NULL,
    color VARCHAR(50) NOT NULL,
    quantity INT NOT NULL,
    FOREIGN KEY (username) REFERENCES users(username) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (shoe_id) REFERENCES shoes(id) ON DELETE CASCADE ON UPDATE CASCADE,
    INDEX idx_username (username),
    INDEX idx_shoe_id (shoe_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Thêm dữ liệu mẫu: Users
INSERT INTO users (username, password, role, is_active, email, name, address, phone)
VALUES 
    ('testuser', 'password', 'CUSTOMER', TRUE, 'test@example.com', 'Người Dùng', 'Hà Nội, Việt Nam', '0987654321'),
    ('admin', 'adminpass', 'ADMIN', TRUE, 'admin@example.com', 'Admin', 'TP HCM, Việt Nam', '0123456789');

-- Thêm dữ liệu mẫu: Brands
INSERT INTO brands (name, description, logo_url)
VALUES 
    ('Nike', 'Thương hiệu giày thể thao hàng đầu thế giới', 'https://example.com/nike.png'),
    ('Adidas', 'Thương hiệu giày thể thao nổi tiếng từ Đức', 'https://example.com/adidas.png'),
    ('Converse', 'Thương hiệu giày lâu đời với thiết kế mang tính biểu tượng', 'https://example.com/converse.png'),
    ('Vans', 'Thương hiệu giày trượt ván nổi tiếng từ Mỹ', 'https://example.com/vans.png');

-- Thêm dữ liệu mẫu: Categories
INSERT INTO categories (name, description)
VALUES 
    ('Thể thao', 'Giày dành cho các hoạt động thể thao'),
    ('Thời trang', 'Giày dành cho mục đích thời trang'),
    ('Chạy bộ', 'Giày chuyên dụng cho chạy bộ'),
    ('Đi bộ', 'Giày thoải mái cho đi bộ và đi lại hàng ngày');

-- Thêm dữ liệu mẫu: Shoes
INSERT INTO shoes (name, description, price, brand_id, category_id, material, gender, image_url)
VALUES 
    ('Nike Air Max 270', 'Giày thể thao với đệm khí Max Air lớn nhất', 150.00, 1, 1, 'Vải lưới, đế cao su', 'UNISEX', 'https://example.com/airmax270.jpg'),
    ('Adidas Ultraboost', 'Giày chạy bộ với công nghệ đệm Boost', 180.00, 2, 3, 'Primeknit, đế Boost', 'UNISEX', 'https://example.com/ultraboost.jpg'),
    ('Converse Chuck Taylor', 'Giày thời trang biểu tượng', 70.00, 3, 2, 'Vải canvas, đế cao su', 'UNISEX', 'https://example.com/chucktaylor.jpg'),
    ('Vans Old Skool', 'Giày trượt ván cổ điển với dải bên hông đặc trưng', 65.00, 4, 2, 'Da lộn, vải canvas', 'UNISEX', 'https://example.com/oldskool.jpg'),
    ('Nike Revolution 6', 'Giày chạy bộ nhẹ và thoáng khí', 85.00, 1, 3, 'Vải lưới', 'MEN', 'https://example.com/revolution6.jpg'),
    ('Adidas Cloudfoam', 'Giày đi bộ thoải mái hàng ngày', 75.00, 2, 4, 'Vải lưới, đế Cloudfoam', 'WOMEN', 'https://example.com/cloudfoam.jpg');

-- Thêm dữ liệu mẫu: ShoeInventory
INSERT INTO shoe_inventory (shoe_id, size, color, quantity_in_stock)
VALUES 
    (1, 40.0, 'Đen', 15),
    (1, 41.0, 'Đen', 10),
    (1, 42.0, 'Đen', 8),
    (1, 40.0, 'Trắng', 12),
    (1, 41.0, 'Trắng', 7),
    (2, 39.0, 'Xám', 9),
    (2, 40.0, 'Xám', 11),
    (2, 41.0, 'Xám', 6),
    (2, 39.0, 'Đỏ', 7),
    (3, 38.0, 'Trắng', 20),
    (3, 39.0, 'Trắng', 15),
    (3, 40.0, 'Trắng', 10),
    (3, 38.0, 'Đen', 18),
    (4, 39.0, 'Đen', 14),
    (4, 40.0, 'Đen', 12),
    (4, 41.0, 'Đen', 8),
    (4, 39.0, 'Xanh Navy', 10),
    (5, 42.0, 'Xanh dương', 9),
    (5, 43.0, 'Xanh dương', 7),
    (5, 44.0, 'Xanh dương', 5),
    (6, 37.0, 'Hồng', 12),
    (6, 38.0, 'Hồng', 10),
    (6, 39.0, 'Hồng', 8);

-- Dữ liệu mẫu cho CartItems (giỏ hàng của testuser)
INSERT INTO cartitems (username, shoe_id, size, color, quantity)
VALUES 
    ('testuser', 1, 41.0, 'Đen', 1),
    ('testuser', 3, 39.0, 'Trắng', 1);