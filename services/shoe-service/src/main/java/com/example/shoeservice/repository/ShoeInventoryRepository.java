package com.example.shoeservice.repository;

import com.example.shoeservice.entity.ShoeInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ShoeInventoryRepository extends JpaRepository<ShoeInventory, Long> {
    List<ShoeInventory> findByShoeId(Long shoeId);
    
    Optional<ShoeInventory> findByShoeIdAndSizeAndColor(Long shoeId, double size, String color);
    
    @Query("SELECT si.quantityInStock FROM ShoeInventory si WHERE si.shoe.id = :shoeId AND si.size = :size AND si.color = :color")
    Integer findQuantityByShoeIdAndSizeAndColor(
            @Param("shoeId") Long shoeId, 
            @Param("size") double size, 
            @Param("color") String color);
    
    @Modifying
    @Query("UPDATE ShoeInventory si SET si.quantityInStock = si.quantityInStock + :quantity " +
           "WHERE si.shoe.id = :shoeId AND si.size = :size AND si.color = :color")
    int updateInventoryQuantity(
            @Param("shoeId") Long shoeId, 
            @Param("size") double size, 
            @Param("color") String color, 
            @Param("quantity") int quantity);
} 