package com.example.shoeservice.service;

import com.example.shoeservice.entity.Brand;
import java.util.List;
import java.util.Optional;

public interface BrandService {
    // Tìm thương hiệu theo ID
    Optional<Brand> findById(Long id);
    
    // Tìm thương hiệu theo tên
    Optional<Brand> findByName(String name);
    
    // Lấy danh sách tất cả thương hiệu
    List<Brand> findAll();
} 