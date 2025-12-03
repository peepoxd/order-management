package com.delivery.order_management.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.delivery.order_management.model.dto.CreateMenuItemRequest;
import com.delivery.order_management.model.dto.MenuItemResponse;
import com.delivery.order_management.model.entity.MenuItem;
import com.delivery.order_management.service.MenuItemService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/menu-items")
@RequiredArgsConstructor
@Tag(name = "Menu Item", description = "Menu item management APIs")
public class MenuItemController {

    private final MenuItemService menuItemService;

    @GetMapping
    @Operation(summary = "Get all menu items")
    public ResponseEntity<List<MenuItemResponse>> getAllMenuItems() {
        List<MenuItem> menuItems = menuItemService.getAllMenuItems();
        List<MenuItemResponse> response = menuItems.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get menu item by ID")
    public ResponseEntity<MenuItemResponse> getMenuItemById(@PathVariable Long id) {
        MenuItem menuItem = menuItemService.getMenuItemById(id);
        return ResponseEntity.ok(toResponse(menuItem));
    }

    @GetMapping("/restaurant/{restaurantId}")
    @Operation(summary = "Get menu items by restaurant")
    public ResponseEntity<List<MenuItemResponse>> getMenuItemsByRestaurant(
            @PathVariable Long restaurantId,
            @RequestParam(required = false, defaultValue = "false") boolean availableOnly) {

        List<MenuItem> menuItems = availableOnly ? menuItemService.getAvailableMenuItems(restaurantId)
                : menuItemService.getMenuItemsByRestaurant(restaurantId);

        List<MenuItemResponse> response = menuItems.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/category/{category}")
    @Operation(summary = "Get menu items by category")
    public ResponseEntity<List<MenuItemResponse>> getMenuItemsByCategory(
            @PathVariable String category) {

        List<MenuItem> menuItems = menuItemService.getMenuItemsByCategory(category);
        List<MenuItemResponse> response = menuItems.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/price-range")
    @Operation(summary = "Get menu items by price range")
    public ResponseEntity<List<MenuItemResponse>> getMenuItemsByPriceRange(
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice) {

        List<MenuItem> menuItems = menuItemService.getMenuItemsByPriceRange(minPrice, maxPrice);
        List<MenuItemResponse> response = menuItems.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/restaurant/{restaurantId}")
    @Operation(summary = "Create new menu item for restaurant")
    public ResponseEntity<MenuItemResponse> createMenuItem(
            @PathVariable Long restaurantId,
            @Valid @RequestBody CreateMenuItemRequest request) {

        MenuItem menuItem = toEntity(request);
        MenuItem created = menuItemService.createMenuItem(restaurantId, menuItem);

        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(created));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update menu item")
    public ResponseEntity<MenuItemResponse> updateMenuItem(
            @PathVariable Long id,
            @Valid @RequestBody CreateMenuItemRequest request) {

        MenuItem menuItem = toEntity(request);
        MenuItem updated = menuItemService.updateMenuItem(id, menuItem);

        return ResponseEntity.ok(toResponse(updated));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete menu item")
    public ResponseEntity<Void> deleteMenuItem(@PathVariable Long id) {
        menuItemService.deleteMenuItem(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/check-availability")
    @Operation(summary = "Check menu item availability")
    public ResponseEntity<Boolean> checkAvailability(
            @PathVariable Long id,
            @RequestParam Integer quantity) {

        boolean available = menuItemService.checkAvailability(id, quantity);
        return ResponseEntity.ok(available);
    }

    // Helper methods
    private MenuItem toEntity(CreateMenuItemRequest request) {
        return MenuItem.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .category(request.getCategory())
                .stockQuantity(request.getStockQuantity())
                .isAvailable(request.getIsAvailable())
                .build();
    }

    private MenuItemResponse toResponse(MenuItem menuItem) {
        return MenuItemResponse.builder()
                .id(menuItem.getId())
                .restaurantId(menuItem.getRestaurant().getId())
                .restaurantName(menuItem.getRestaurant().getName())
                .name(menuItem.getName())
                .description(menuItem.getDescription())
                .price(menuItem.getPrice())
                .category(menuItem.getCategory())
                .isAvailable(menuItem.getIsAvailable())
                .stockQuantity(menuItem.getStockQuantity())
                .createdAt(menuItem.getCreatedAt())
                .updatedAt(menuItem.getUpdatedAt())
                .build();
    }
}
