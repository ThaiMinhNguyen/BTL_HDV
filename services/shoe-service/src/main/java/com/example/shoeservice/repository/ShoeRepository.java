package com.example.shoeservice.repository;

import com.example.shoeservice.entity.Shoe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ShoeRepository extends JpaRepository<Shoe, Long> {
    Optional<Shoe> findById(Long id);
    Optional<Shoe> findByName(String name);
    List<Shoe> findByBrandId(Long brandId);
    List<Shoe> findByCategoryId(Long categoryId);
    List<Shoe> findByGender(Shoe.Gender gender);
    List<Shoe> findByPriceBetween(double minPrice, double maxPrice);
} 