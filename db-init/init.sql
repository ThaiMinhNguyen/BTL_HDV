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
    ('testuser', 'password', 'CUSTOMER', TRUE, 'test@example.com', 'User', 'Hanoi, Vietnam', '0987654321'),
    ('admin', 'adminpass', 'ADMIN', TRUE, 'admin@example.com', 'Admin', 'Ho Chi Minh City, Vietnam', '0123456789');

-- Thêm dữ liệu mẫu: Brands
INSERT INTO brands (name, description, logo_url)
VALUES 
    ('Nike', 'Leading sports shoe brand in the world', 'https://example.com/nike.png'),
    ('Adidas', 'Famous sports shoe brand from Germany', 'https://example.com/adidas.png'),
    ('Converse', 'Longstanding shoe brand with iconic designs', 'https://example.com/converse.png'),
    ('Vans', 'Famous skateboarding shoe brand from the USA', 'https://example.com/vans.png');

-- Thêm dữ liệu mẫu: Categories
INSERT INTO categories (name, description)
VALUES 
    ('Sports', 'Shoes for sports activities'),
    ('Fashion', 'Shoes for fashion purposes'),
    ('Running', 'Specialized shoes for running'),
    ('Walking', 'Comfortable shoes for walking and daily activities');

-- Thêm dữ liệu mẫu: Shoes
INSERT INTO shoes (name, description, price, brand_id, category_id, material, gender, image_url)
VALUES 
    ('Nike Air Max 270', 'Sports shoes with the largest Max Air cushioning', 150.00, 1, 1, 'Mesh, rubber sole', 'UNISEX', 'https://example.com/airmax270.jpg'),
    ('Adidas Ultraboost', 'Running shoes with Boost cushioning technology', 180.00, 2, 3, 'Primeknit, Boost sole', 'UNISEX', 'https://example.com/ultraboost.jpg'),
    ('Converse Chuck Taylor', 'Iconic fashion shoes', 70.00, 3, 2, 'Canvas, rubber sole', 'UNISEX', 'https://example.com/chucktaylor.jpg'),
    ('Vans Old Skool', 'Classic skateboarding shoes with signature side stripe', 65.00, 4, 2, 'Suede, canvas', 'UNISEX', 'https://example.com/oldskool.jpg'),
    ('Nike Revolution 6', 'Lightweight and breathable running shoes', 85.00, 1, 3, 'Mesh', 'MEN', 'https://example.com/revolution6.jpg'),
    ('Adidas Cloudfoam', 'Comfortable daily walking shoes', 75.00, 2, 4, 'Mesh, Cloudfoam sole', 'WOMEN', 'https://example.com/cloudfoam.jpg');

-- Thêm dữ liệu mẫu: ShoeInventory
INSERT INTO shoe_inventory (shoe_id, size, color, quantity_in_stock)
VALUES 
    (1, 40.0, 'Black', 15),
    (1, 41.0, 'Black', 10),
    (1, 42.0, 'Black', 8),
    (1, 40.0, 'White', 12),
    (1, 41.0, 'White', 7),
    (2, 39.0, 'Grey', 9),
    (2, 40.0, 'Grey', 11),
    (2, 41.0, 'Grey', 6),
    (2, 39.0, 'Red', 7),
    (3, 38.0, 'White', 20),
    (3, 39.0, 'White', 15),
    (3, 40.0, 'White', 10),
    (3, 38.0, 'Black', 18),
    (4, 39.0, 'Black', 14),
    (4, 40.0, 'Black', 12),
    (4, 41.0, 'Black', 8),
    (4, 39.0, 'Navy Blue', 10),
    (5, 42.0, 'Blue', 9),
    (5, 43.0, 'Blue', 7),
    (5, 44.0, 'Blue', 5),
    (6, 37.0, 'Pink', 12),
    (6, 38.0, 'Pink', 10),
    (6, 39.0, 'Pink', 8);

-- Dữ liệu mẫu cho CartItems (giỏ hàng của testuser)
INSERT INTO cartitems (username, shoe_id, size, color, quantity)
VALUES 
    ('testuser', 1, 41.0, 'Black', 1),
    ('testuser', 3, 39.0, 'White', 1);