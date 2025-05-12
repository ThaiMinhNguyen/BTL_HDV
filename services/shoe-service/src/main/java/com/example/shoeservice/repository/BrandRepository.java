package com.example.shoeservice.repository;

import com.example.shoeservice.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BrandRepository extends JpaRepository<Brand, Long> {
    Optional<Brand> findById(Long id);
    Optional<Brand> findByName(String name);
    List<Brand> findAll();
} 