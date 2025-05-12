package com.example.cartservice.service;

import com.example.cartservice.entity.CartItem;

import java.util.List;

public interface CartService {
    // Thêm sản phẩm vào giỏ hàng
    void addItemToCart(String username, Long shoeId, double size, String color, int quantity);

    // Cập nhật số lượng sản phẩm trong giỏ hàng
    void updateItemQuantity(String username, Long itemId, int quantity);

    // Xóa sản phẩm khỏi giỏ hàng
    void removeItemFromCart(String username, Long itemId);

    // Xóa sản phẩm khỏi giỏ hàng theo shoeId
    void removeShoeFromCart(String username, Long shoeId);

    // Lấy danh sách sản phẩm trong giỏ hàng
    List<CartItem> getCartItems(String username);
}