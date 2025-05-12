package com.example.cartservice.repository;

import com.example.cartservice.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByUsernameAndShoeIdAndSizeAndColor(String username, Long shoeId, double size, String color);
    List<CartItem> findByUsername(String username);
    List<CartItem> findByUsernameAndShoeId(String username, Long shoeId);
    void deleteById(Long id);
}