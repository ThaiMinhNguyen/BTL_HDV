package com.example.notificationservice.controller;

import com.example.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private static final Logger logger = LoggerFactory.getLogger(NotificationController.class);
    private final NotificationService notificationService;

    @PostMapping("/email")
    public ResponseEntity<Void> sendOrderConfirmationEmail(@RequestBody EmailRequest request) {
        logger.info("Nhận yêu cầu gửi email cho đơn hàng: {}", request.getOrderId());
        notificationService.sendOrderConfirmationEmail(
                request.getEmail(),
                request.getOrderId(),
                request.getStatus(),
                request.getItems(),
                request.getTotalPrice()
        );
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/test-email")
    public ResponseEntity<String> testEmail(@RequestParam String to) {
        try {
            // Tạo dữ liệu mẫu cho một đơn hàng test
            String itemsJson = "[{\"shoeId\":1,\"size\":42,\"color\":\"Black\",\"quantity\":1,\"unitPrice\":1000000}]";
            
            logger.info("Gửi email test đến: {}", to);
            notificationService.sendOrderConfirmationEmail(
                    to,
                    123456L,
                    "TEST",
                    itemsJson,
                    1000000
            );
            
            return ResponseEntity.ok("Email test đã được gửi thành công!");
        } catch (Exception e) {
            logger.error("Lỗi khi gửi email test: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body("Lỗi khi gửi email: " + e.getMessage());
        }
    }

    // DTO để nhận thông tin từ request
    public static class EmailRequest {
        private String email;
        private Long orderId;
        private String status;
        private String items; // Danh sách items dưới dạng JSON
        private double totalPrice;
        private String statusMessage; // Trường này giờ không còn cần thiết nhưng giữ lại để tương thích

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public Long getOrderId() { return orderId; }
        public void setOrderId(Long orderId) { this.orderId = orderId; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public String getItems() { return items; }
        public void setItems(String items) { this.items = items; }
        public double getTotalPrice() { return totalPrice; }
        public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }
        public String getStatusMessage() { return statusMessage; }
        public void setStatusMessage(String statusMessage) { this.statusMessage = statusMessage; }
    }
}