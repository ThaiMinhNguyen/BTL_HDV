package com.example.cartservice.controller;

import com.example.cartservice.entity.CartItem;
import com.example.cartservice.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/{username}/items")
    public ResponseEntity<Void> addItemToCart(
            @PathVariable String username,
            @RequestBody CartItemRequest request) {
        cartService.addItemToCart(username, request.getShoeId(), request.getSize(), 
                                 request.getColor(), request.getQuantity());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{username}/items/{itemId}")
    public ResponseEntity<Void> updateItemQuantity(
            @PathVariable String username,
            @PathVariable Long itemId,
            @RequestBody CartItemUpdateRequest request) {
        cartService.updateItemQuantity(username, itemId, request.getQuantity());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{username}/items/{itemId}")
    public ResponseEntity<Void> removeItemFromCart(
            @PathVariable String username,
            @PathVariable Long itemId) {
        cartService.removeItemFromCart(username, itemId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{username}/items")
    public ResponseEntity<List<CartItem>> getCartItems(@PathVariable String username) {
        List<CartItem> items = cartService.getCartItems(username);
        return ResponseEntity.ok(items);
    }

    // DTO cho yêu cầu thêm sản phẩm vào giỏ hàng
    public static class CartItemRequest {
        private Long shoeId;
        private double size;
        private String color;
        private int quantity;

        public Long getShoeId() { return shoeId; }
        public void setShoeId(Long shoeId) { this.shoeId = shoeId; }
        
        public double getSize() { return size; }
        public void setSize(double size) { this.size = size; }
        
        public String getColor() { return color; }
        public void setColor(String color) { this.color = color; }
        
        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
    }
    
    // DTO cho yêu cầu cập nhật số lượng
    public static class CartItemUpdateRequest {
        private int quantity;
        
        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
    }
}