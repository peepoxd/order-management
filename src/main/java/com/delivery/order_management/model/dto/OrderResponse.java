package com.delivery.order_management.model.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.delivery.order_management.model.entity.OrderStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    private Long id;
    private String orderNumber;
    private Long restaurantId;
    private String restaurantName;
    private String customerName;
    private String customerPhone;
    private String deliveryAddress;
    private BigDecimal totalAmount;
    private OrderStatus status;
    private String notes;
    private List<OrderItemResponse> items;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
