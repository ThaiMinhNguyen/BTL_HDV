package com.example.shoeservice.service.impl;

import com.example.shoeservice.entity.Shoe;
import com.example.shoeservice.entity.ShoeInventory;
import com.example.shoeservice.repository.ShoeRepository;
import com.example.shoeservice.repository.ShoeInventoryRepository;
import com.example.shoeservice.service.ShoeService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ShoeServiceImpl implements ShoeService {

    private static final Logger logger = LoggerFactory.getLogger(ShoeServiceImpl.class);
    private final ShoeRepository shoeRepository;
    private final ShoeInventoryRepository shoeInventoryRepository;

    @Override
    public Optional<Shoe> findById(Long id) {
        logger.debug("Tìm kiếm giày với ID: {}", id);
        try {
            return shoeRepository.findById(id);
        } catch (Exception e) {
            logger.error("Lỗi khi tìm kiếm giày {}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Không thể tìm kiếm giày: " + e.getMessage());
        }
    }

    @Override
    public List<Shoe> findAll() {
        logger.debug("Lấy danh sách tất cả giày");
        try {
            return shoeRepository.findAll();
        } catch (Exception e) {
            logger.error("Lỗi khi lấy danh sách giày: {}", e.getMessage(), e);
            throw new RuntimeException("Không thể lấy danh sách giày: " + e.getMessage());
        }
    }

    @Override
    public List<Shoe> findByBrandId(Long brandId) {
        logger.debug("Lấy danh sách giày theo thương hiệu ID: {}", brandId);
        try {
            return shoeRepository.findByBrandId(brandId);
        } catch (Exception e) {
            logger.error("Lỗi khi lấy danh sách giày theo thương hiệu: {}", e.getMessage(), e);
            throw new RuntimeException("Không thể lấy danh sách giày theo thương hiệu: " + e.getMessage());
        }
    }

    @Override
    public List<Shoe> findByCategoryId(Long categoryId) {
        logger.debug("Lấy danh sách giày theo danh mục ID: {}", categoryId);
        try {
            return shoeRepository.findByCategoryId(categoryId);
        } catch (Exception e) {
            logger.error("Lỗi khi lấy danh sách giày theo danh mục: {}", e.getMessage(), e);
            throw new RuntimeException("Không thể lấy danh sách giày theo danh mục: " + e.getMessage());
        }
    }

    @Override
    public List<Shoe> findByGender(Shoe.Gender gender) {
        logger.debug("Lấy danh sách giày theo giới tính: {}", gender);
        try {
            return shoeRepository.findByGender(gender);
        } catch (Exception e) {
            logger.error("Lỗi khi lấy danh sách giày theo giới tính: {}", e.getMessage(), e);
            throw new RuntimeException("Không thể lấy danh sách giày theo giới tính: " + e.getMessage());
        }
    }

    @Override
    public List<Shoe> findByPriceRange(double minPrice, double maxPrice) {
        logger.debug("Lấy danh sách giày theo khoảng giá từ {} đến {}", minPrice, maxPrice);
        try {
            return shoeRepository.findByPriceBetween(minPrice, maxPrice);
        } catch (Exception e) {
            logger.error("Lỗi khi lấy danh sách giày theo khoảng giá: {}", e.getMessage(), e);
            throw new RuntimeException("Không thể lấy danh sách giày theo khoảng giá: " + e.getMessage());
        }
    }

    @Override
    public List<ShoeInventory> getShoeInventory(Long shoeId) {
        logger.debug("Lấy thông tin tồn kho của giày ID: {}", shoeId);
        try {
            return shoeInventoryRepository.findByShoeId(shoeId);
        } catch (Exception e) {
            logger.error("Lỗi khi lấy thông tin tồn kho giày {}: {}", shoeId, e.getMessage(), e);
            throw new RuntimeException("Không thể lấy thông tin tồn kho: " + e.getMessage());
        }
    }

    @Override
    public int getStockQuantity(Long shoeId, double size, String color) {
        logger.debug("Kiểm tra số lượng tồn kho cho giày ID: {}, size: {}, màu: {}", shoeId, size, color);
        try {
            Integer quantity = shoeInventoryRepository.findQuantityByShoeIdAndSizeAndColor(shoeId, size, color);
            return quantity != null ? quantity : 0;
        } catch (Exception e) {
            logger.error("Lỗi khi kiểm tra tồn kho giày {}: {}", shoeId, e.getMessage(), e);
            throw new RuntimeException("Không thể kiểm tra tồn kho: " + e.getMessage());
        }
    }

    @Override
    public boolean checkAvailability(Long shoeId, double size, String color, int quantity) {
        logger.debug("Kiểm tra tính khả dụng của giày ID: {}, size: {}, màu: {}, số lượng: {}", 
                    shoeId, size, color, quantity);
        try {
            if (quantity <= 0) {
                logger.warn("Số lượng yêu cầu không hợp lệ: {}", quantity);
                return false;
            }
            int stockQuantity = getStockQuantity(shoeId, size, color);
            boolean isAvailable = stockQuantity >= quantity;
            if (!isAvailable) {
                logger.warn("Giày ID: {}, size: {}, màu: {} không đủ số lượng (yêu cầu: {}, tồn kho: {})",
                        shoeId, size, color, quantity, stockQuantity);
            }
            return isAvailable;
        } catch (Exception e) {
            logger.error("Lỗi khi kiểm tra tính khả dụng giày {}: {}", shoeId, e.getMessage(), e);
            throw new RuntimeException("Không thể kiểm tra tính khả dụng: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public void updateInventoryQuantity(Long shoeId, double size, String color, int quantity) {
        logger.debug("Cập nhật số lượng tồn kho cho giày ID: {}, size: {}, màu: {} với số lượng: {}", 
                    shoeId, size, color, quantity);
        try {
            int updatedRows = shoeInventoryRepository.updateInventoryQuantity(shoeId, size, color, quantity);
            if (updatedRows == 0) {
                Optional<Shoe> shoe = shoeRepository.findById(shoeId);
                if (shoe.isEmpty()) {
                    throw new IllegalArgumentException("Không tìm thấy giày với ID: " + shoeId);
                }
                
                // Tạo mới bản ghi tồn kho nếu không tồn tại
                ShoeInventory newInventory = new ShoeInventory();
                newInventory.setShoe(shoe.get());
                newInventory.setSize(size);
                newInventory.setColor(color);
                newInventory.setQuantityInStock(quantity);
                shoeInventoryRepository.save(newInventory);
            }
            logger.info("Cập nhật số lượng tồn kho thành công cho giày ID: {}", shoeId);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Lỗi khi cập nhật số lượng tồn kho giày {}: {}", shoeId, e.getMessage(), e);
            throw new RuntimeException("Không thể cập nhật số lượng tồn kho: " + e.getMessage());
        }
    }
} 