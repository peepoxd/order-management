package com.delivery.order_management.service;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.delivery.order_management.exception.ResourceNotFoundException;
import com.delivery.order_management.model.entity.Order;
import com.delivery.order_management.model.entity.OrderStatus;
import com.delivery.order_management.repository.OrderRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final RestaurantService restaurantService;
    private final MenuItemService menuItemService;

    /**
     * Get all orders with pagination
     */
    public Page<Order> getAllOrders(Pageable pageable) {
        log.info("Fetching all orders with pagination");
        return orderRepository.findAll(pageable);
    }

    /**
     * Get order by ID
     */
    public Order getOrderById(Long id) {
        log.info("Fetching order with id: {}", id);
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
    }

    /**
     * Get order by order number
     */
    public Order getOrderByOrderNumber(String orderNumber) {
        log.info("Fetching order with order number: {}", orderNumber);
        return orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with order number: " + orderNumber));
    }

    /**
     * Get orders by status
     */
    public List<Order> getOrdersByStatus(OrderStatus status) {
        log.info("Fetching orders with status: {}", status);
        return orderRepository.findByStatus(status);
    }

    /**
     * Get orders by customer phone
     */
    public List<Order> getOrdersByCustomerPhone(String customerPhone) {
        log.info("Fetching orders for customer phone: {}", customerPhone);
        return orderRepository.findByCustomerPhone(customerPhone);
    }

    /**
     * Generate unique order number
     */
    private String generateOrderNumber() {
        return "ORD-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    /**
     * Create order (will implement in Day 3)
     */
    @Transactional
    public Order createOrder(Order order) {
        log.info("Creating new order");
        // TODO: Implement in Day 3
        return null;
    }

    /**
     * Update order status
     */
    @Transactional
    public Order updateOrderStatus(Long orderId, OrderStatus newStatus) {
        log.info("Updating order {} to status: {}", orderId, newStatus);

        Order order = getOrderById(orderId);
        order.setStatus(newStatus);

        return orderRepository.save(order);
    }

    /**
     * Cancel order
     */
    @Transactional
    public Order cancelOrder(Long orderId) {
        log.info("Cancelling order: {}", orderId);
        return updateOrderStatus(orderId, OrderStatus.CANCELLED);
    }
}
