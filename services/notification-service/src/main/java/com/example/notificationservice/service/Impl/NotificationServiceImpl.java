package com.example.notificationservice.service.Impl;

import com.example.notificationservice.service.NotificationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final JavaMailSender emailSender;
    
    @Value("${mail.from}")
    private String fromEmail;

    @Override
    public void sendOrderConfirmationEmail(String email, Long orderId, String status, String items, double totalPrice) {
        try {
            logger.debug("Bắt đầu gửi email xác nhận cho đơn hàng ID: {} tới email: {}", orderId, email);

            // Phân tích items từ JSON
            List<Map<String, Object>> itemList = new ArrayList<>();
            try {
                itemList = objectMapper.readValue(items, List.class);
            } catch (Exception e) {
                logger.warn("Không thể phân tích items: {}, sử dụng thông tin mặc định", items, e);
                itemList = new ArrayList<>();
            }

            // Lấy thông tin sản phẩm cho từng item
            StringBuilder itemsDetails = new StringBuilder();
            for (Map<String, Object> item : itemList) {
                Long shoeId = item.get("shoeId") != null ? ((Number) item.get("shoeId")).longValue() : null;
                double size = item.get("size") != null ? ((Number) item.get("size")).doubleValue() : 0;
                String color = (String) item.get("color");
                int quantity = item.get("quantity") != null ? ((Number) item.get("quantity")).intValue() : 0;
                double unitPrice = item.get("unitPrice") != null ? ((Number) item.get("unitPrice")).doubleValue() : 0;

                if (shoeId == null || shoeId == 0) {
                    logger.warn("shoeId không hợp lệ trong item: {}", item);
                    itemsDetails.append("Sản phẩm không xác định (ID: null), Số lượng: ").append(quantity).append("<br/>");
                    continue;
                }

                try {
                    String productUrl = "http://shoe-service:8082/api/shoes/" + shoeId;
                    logger.debug("Gửi yêu cầu lấy thông tin giày tới: {}", productUrl);
                    Map<String, Object> shoe = restTemplate.getForObject(productUrl, Map.class);
                    if (shoe != null) {
                        String shoeName = (String) shoe.get("name");
                        itemsDetails.append("Giày: ").append(shoeName)
                                .append(", Kích cỡ: ").append(size)
                                .append(", Màu sắc: ").append(color)
                                .append(", Số lượng: ").append(quantity)
                                .append(", Giá: ").append(String.format("%,.0f", unitPrice)).append(" VNĐ")
                                .append("<br/>");
                    } else {
                        logger.warn("Không tìm thấy thông tin giày với ID: {}", shoeId);
                        itemsDetails.append("Giày không xác định (ID: ").append(shoeId)
                                .append("), Kích cỡ: ").append(size)
                                .append(", Màu sắc: ").append(color)
                                .append(", Số lượng: ").append(quantity)
                                .append(", Giá: ").append(String.format("%,.0f", unitPrice)).append(" VNĐ")
                                .append("<br/>");
                    }
                } catch (Exception e) {
                    logger.error("Lỗi khi lấy thông tin giày ID {}: {}", shoeId, e.getMessage());
                    itemsDetails.append("Giày không xác định (ID: ").append(shoeId)
                            .append("), Kích cỡ: ").append(size)
                            .append(", Màu sắc: ").append(color)
                            .append(", Số lượng: ").append(quantity)
                            .append(", Giá: ").append(String.format("%,.0f", unitPrice)).append(" VNĐ")
                            .append("<br/>");
                }
            }

            // Tạo nội dung email
            String subject = "Xác nhận đơn hàng #" + orderId + " - Shoe Store";
            
            String htmlContent = "<html><body>" +
                    "<h2>Kính gửi Quý khách,</h2>" +
                    "<p>Cảm ơn Quý khách đã đặt hàng tại Shoe Store!</p>" +
                    "<h3>Chi tiết đơn hàng:</h3>" +
                    "<p><strong>Mã đơn hàng:</strong> " + orderId + "</p>" +
                    "<p><strong>Trạng thái:</strong> " + status + "</p>" +
                    "<h3>Sản phẩm đã đặt:</h3>" +
                    "<p>" + itemsDetails.toString() + "</p>" +
                    "<p><strong>Tổng giá trị đơn hàng:</strong> " + String.format("%,.0f", totalPrice) + " VNĐ</p>" +
                    "<p>Chúng tôi sẽ thông báo cho Quý khách khi đơn hàng được xử lý.</p>" +
                    "<p>Nếu có bất kỳ thắc mắc nào, vui lòng liên hệ với chúng tôi qua email support@shoestore.com hoặc số điện thoại 1900-123-456.</p>" +
                    "<p>Trân trọng,<br/>Đội ngũ Shoe Store</p>" +
                    "</body></html>";

            // Gửi email sử dụng JavaMailSender
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setFrom(fromEmail);
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(htmlContent, true); // true để chỉ định đây là HTML
            
            emailSender.send(message);
            
            logger.info("Gửi email xác nhận thành công cho đơn hàng ID: {}", orderId);
        } catch (MessagingException e) {
            logger.error("Lỗi khi gửi email xác nhận cho đơn hàng {}: {}", orderId, e.getMessage(), e);
            throw new RuntimeException("Không thể gửi email xác nhận: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Lỗi không xác định khi gửi email xác nhận cho đơn hàng {}: {}", orderId, e.getMessage(), e);
            throw new RuntimeException("Không thể gửi email xác nhận: " + e.getMessage());
        }
    }
}