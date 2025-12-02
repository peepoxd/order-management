package com.delivery.order_management.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.delivery.order_management.model.entity.MenuItem;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {

       // Find by restaurant ID
       List<MenuItem> findByRestaurantId(Long restaurantId);

       // Find available items by restaurant
       List<MenuItem> findByRestaurantIdAndIsAvailableTrue(Long restaurantId);

       // Find by category
       List<MenuItem> findByCategory(String category);

       // Find by price range
       List<MenuItem> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);

       // Find by restaurant and category
       List<MenuItem> findByRestaurantIdAndCategory(Long restaurantId, String category);

       // Custom query: Update stock quantity
       @Modifying
       @Query("UPDATE MenuItem m SET m.stockQuantity = m.stockQuantity - :quantity " +
                     "WHERE m.id = :menuItemId AND m.stockQuantity >= :quantity")
       int decreaseStock(@Param("menuItemId") Long menuItemId, @Param("quantity") Integer quantity);

       // Check if item is available with sufficient stock
       @Query("SELECT CASE WHEN COUNT(m) > 0 THEN true ELSE false END " +
                     "FROM MenuItem m " +
                     "WHERE m.id = :id AND m.isAvailable = true AND m.stockQuantity >= :quantity")
       boolean isAvailableWithStock(@Param("id") Long id, @Param("quantity") Integer quantity);
}