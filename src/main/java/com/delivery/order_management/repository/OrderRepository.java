package com.delivery.order_management.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.delivery.order_management.model.entity.Order;
import com.delivery.order_management.model.entity.OrderStatus;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    // Find by order number
    Optional<Order> findByOrderNumber(String orderNumber);

    // Find by status
    List<Order> findByStatus(OrderStatus status);

    // Find by restaurant ID
    Page<Order> findByRestaurantId(Long restaurantId, Pageable pageable);

    // Find by customer phone
    List<Order> findByCustomerPhone(String customerPhone);

    // Find orders by date range
    List<Order> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    // Find recent orders
    @Query("SELECT o FROM Order o ORDER BY o.createdAt DESC")
    Page<Order> findRecentOrders(Pageable pageable);

    // Find orders by restaurant and status
    List<Order> findByRestaurantIdAndStatus(Long restaurantId, OrderStatus status);

    // Count orders by status
    long countByStatus(OrderStatus status);

    // Custom query: Find orders with total amount greater than
    @Query("SELECT o FROM Order o WHERE o.totalAmount > :amount ORDER BY o.createdAt DESC")
    List<Order> findOrdersAboveAmount(@Param("amount") java.math.BigDecimal amount);
}