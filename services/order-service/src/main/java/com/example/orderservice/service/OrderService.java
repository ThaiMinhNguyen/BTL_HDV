package com.example.orderservice.service;

import com.example.orderservice.model.Order;
import java.util.List;

public interface OrderService {
    // Tạo đơn hàng mới
    Order createOrder(Order order);

    // Lấy thông tin đơn hàng theo ID
    Order getOrderById(Long orderId);
    
    // Lấy danh sách đơn hàng theo username
    List<Order> getOrdersByUsername(String username);
    
    // Cập nhật trạng thái đơn hàng
    Order updateOrderStatus(Long orderId, String status);
}