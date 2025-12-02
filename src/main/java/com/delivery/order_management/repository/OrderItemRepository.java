package com.delivery.order_management.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.delivery.order_management.model.entity.OrderItem;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    // Find by order ID
    List<OrderItem> findByOrderId(Long orderId);

    // Find by menu item ID
    List<OrderItem> findByMenuItemId(Long menuItemId);

    // Custom query: Get total quantity ordered for a menu item
    @Query("SELECT SUM(oi.quantity) FROM OrderItem oi WHERE oi.menuItem.id = :menuItemId")
    Integer getTotalQuantityOrdered(@Param("menuItemId") Long menuItemId);
}