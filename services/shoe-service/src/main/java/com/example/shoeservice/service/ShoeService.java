package com.example.shoeservice.service;

import com.example.shoeservice.entity.Shoe;
import com.example.shoeservice.entity.ShoeInventory;

import java.util.List;
import java.util.Optional;

public interface ShoeService {
    // Tìm giày theo ID
    Optional<Shoe> findById(Long id);

    // Lấy danh sách tất cả giày
    List<Shoe> findAll();

    // Lấy danh sách giày theo thương hiệu
    List<Shoe> findByBrandId(Long brandId);

    // Lấy danh sách giày theo danh mục
    List<Shoe> findByCategoryId(Long categoryId);

    // Lấy danh sách giày theo giới tính
    List<Shoe> findByGender(Shoe.Gender gender);

    // Lấy danh sách giày theo khoảng giá
    List<Shoe> findByPriceRange(double minPrice, double maxPrice);

    // Lấy thông tin tồn kho của một đôi giày
    List<ShoeInventory> getShoeInventory(Long shoeId);

    // Kiểm tra số lượng tồn kho của một đôi giày với size và màu cụ thể
    int getStockQuantity(Long shoeId, double size, String color);

    // Kiểm tra xem giày có đủ số lượng hay không
    boolean checkAvailability(Long shoeId, double size, String color, int quantity);

    // Cập nhật số lượng tồn kho
    void updateInventoryQuantity(Long shoeId, double size, String color, int quantity);
} 