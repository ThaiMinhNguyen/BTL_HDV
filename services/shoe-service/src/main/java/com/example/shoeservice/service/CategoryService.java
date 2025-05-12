package com.example.shoeservice.service;

import com.example.shoeservice.entity.Category;
import java.util.List;
import java.util.Optional;

public interface CategoryService {
    // Tìm danh mục theo ID
    Optional<Category> findById(Long id);
    
    // Tìm danh mục theo tên
    Optional<Category> findByName(String name);
    
    // Lấy danh sách tất cả danh mục
    List<Category> findAll();
} 