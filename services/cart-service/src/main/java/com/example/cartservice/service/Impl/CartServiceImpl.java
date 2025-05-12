package com.example.cartservice.service.Impl;

import com.example.cartservice.entity.CartItem;
import com.example.cartservice.repository.CartItemRepository;
import com.example.cartservice.service.CartService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private static final Logger logger = LoggerFactory.getLogger(CartServiceImpl.class);
    private final CartItemRepository cartItemRepository;

    @Override
    @Transactional
    public void addItemToCart(String username, Long shoeId, double size, String color, int quantity) {
        logger.debug("Thêm giày {} vào giỏ hàng của người dùng: {}", shoeId, username);
        try {
            if (quantity <= 0) {
                logger.warn("Số lượng không hợp lệ: {}", quantity);
                throw new IllegalArgumentException("Số lượng phải lớn hơn 0");
            }

            // Kiểm tra xem sản phẩm đã có trong giỏ hàng chưa
            Optional<CartItem> existingItem = cartItemRepository.findByUsernameAndShoeIdAndSizeAndColor(
                username, shoeId, size, color);
                
            if (existingItem.isPresent()) {
                CartItem cartItem = existingItem.get();
                cartItem.setQuantity(cartItem.getQuantity() + quantity);
                cartItemRepository.save(cartItem);
                logger.info("Cập nhật số lượng giày {} trong giỏ hàng của {}", shoeId, username);
            } else {
                CartItem cartItem = new CartItem();
                cartItem.setUsername(username);
                cartItem.setShoeId(shoeId);
                cartItem.setSize(size);
                cartItem.setColor(color);
                cartItem.setQuantity(quantity);
                cartItemRepository.save(cartItem);
                logger.info("Thêm giày {} vào giỏ hàng của {}", shoeId, username);
            }
        } catch (IllegalArgumentException e) {
            logger.error("Lỗi khi thêm giày vào giỏ hàng: {}", e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            logger.error("Lỗi không xác định khi thêm giày vào giỏ hàng: {}", e.getMessage(), e);
            throw new RuntimeException("Không thể thêm giày vào giỏ hàng: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public void updateItemQuantity(String username, Long itemId, int quantity) {
        logger.debug("Cập nhật số lượng item {} trong giỏ hàng của người dùng: {}", itemId, username);
        try {
            if (quantity <= 0) {
                logger.warn("Số lượng không hợp lệ: {}", quantity);
                throw new IllegalArgumentException("Số lượng phải lớn hơn 0");
            }

            Optional<CartItem> cartItemOptional = cartItemRepository.findById(itemId);
            if (cartItemOptional.isPresent()) {
                CartItem cartItem = cartItemOptional.get();
                // Kiểm tra xem item có thuộc về user không
                if (!cartItem.getUsername().equals(username)) {
                    logger.warn("Item {} không thuộc về người dùng {}", itemId, username);
                    throw new IllegalArgumentException("Item không thuộc về người dùng này");
                }
                
                cartItem.setQuantity(quantity);
                cartItemRepository.save(cartItem);
                logger.info("Cập nhật số lượng item {} thành {} trong giỏ hàng của {}", itemId, quantity, username);
            } else {
                logger.warn("Item {} không tồn tại", itemId);
                throw new IllegalArgumentException("Item không tồn tại trong giỏ hàng");
            }
        } catch (IllegalArgumentException e) {
            logger.error("Lỗi khi cập nhật số lượng item: {}", e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            logger.error("Lỗi không xác định khi cập nhật số lượng item: {}", e.getMessage(), e);
            throw new RuntimeException("Không thể cập nhật số lượng item: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public void removeItemFromCart(String username, Long itemId) {
        logger.debug("Xóa item {} khỏi giỏ hàng của người dùng: {}", itemId, username);
        try {
            Optional<CartItem> cartItemOptional = cartItemRepository.findById(itemId);
            if (cartItemOptional.isPresent()) {
                CartItem cartItem = cartItemOptional.get();
                // Kiểm tra xem item có thuộc về user không
                if (!cartItem.getUsername().equals(username)) {
                    logger.warn("Item {} không thuộc về người dùng {}", itemId, username);
                    throw new IllegalArgumentException("Item không thuộc về người dùng này");
                }
                
                cartItemRepository.deleteById(itemId);
                logger.info("Xóa item {} khỏi giỏ hàng của {}", itemId, username);
            } else {
                logger.warn("Item {} không tồn tại", itemId);
                throw new IllegalArgumentException("Item không tồn tại trong giỏ hàng");
            }
        } catch (IllegalArgumentException e) {
            logger.error("Lỗi khi xóa item khỏi giỏ hàng: {}", e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            logger.error("Lỗi không xác định khi xóa item khỏi giỏ hàng: {}", e.getMessage(), e);
            throw new RuntimeException("Không thể xóa item khỏi giỏ hàng: " + e.getMessage());
        }
    }

    @Override
    public List<CartItem> getCartItems(String username) {
        logger.debug("Lấy danh sách sản phẩm trong giỏ hàng của người dùng: {}", username);
        try {
            return cartItemRepository.findByUsername(username);
        } catch (Exception e) {
            logger.error("Lỗi khi lấy danh sách sản phẩm trong giỏ hàng: {}", e.getMessage(), e);
            throw new RuntimeException("Không thể lấy danh sách sản phẩm trong giỏ hàng: " + e.getMessage());
        }
    }
    
    @Override
    @Transactional
    public void removeShoeFromCart(String username, Long shoeId) {
        logger.debug("Xóa tất cả sản phẩm có shoeId {} khỏi giỏ hàng của người dùng: {}", shoeId, username);
        try {
            List<CartItem> items = cartItemRepository.findByUsernameAndShoeId(username, shoeId);
            if (items.isEmpty()) {
                logger.warn("Không tìm thấy sản phẩm với shoeId {} trong giỏ hàng của {}", shoeId, username);
                return; // Không có gì để xóa, trả về thành công
            }
            
            for (CartItem item : items) {
                cartItemRepository.deleteById(item.getId());
                logger.info("Đã xóa sản phẩm với id={}, shoeId={} khỏi giỏ hàng của {}", 
                           item.getId(), shoeId, username);
            }
        } catch (Exception e) {
            logger.error("Lỗi khi xóa sản phẩm theo shoeId khỏi giỏ hàng: {}", e.getMessage(), e);
            throw new RuntimeException("Không thể xóa sản phẩm khỏi giỏ hàng: " + e.getMessage());
        }
    }
}