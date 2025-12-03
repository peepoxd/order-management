package com.delivery.order_management.controller;

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

import com.delivery.order_management.model.dto.CreateRestaurantRequest;
import com.delivery.order_management.model.dto.RestaurantResponse;
import com.delivery.order_management.model.entity.Restaurant;
import com.delivery.order_management.service.RestaurantService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/restaurants")
@RequiredArgsConstructor
@Tag(name = "Restaurant", description = "Restaurant management APIs")
public class RestaurantController {

    private final RestaurantService restaurantService;

    @GetMapping
    @Operation(summary = "Get all restaurants")
    public ResponseEntity<List<RestaurantResponse>> getAllRestaurants(
            @RequestParam(required = false, defaultValue = "false") boolean activeOnly) {

        List<Restaurant> restaurants = activeOnly ? restaurantService.getActiveRestaurants()
                : restaurantService.getAllRestaurants();

        List<RestaurantResponse> response = restaurants.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get restaurant by ID")
    public ResponseEntity<RestaurantResponse> getRestaurantById(@PathVariable Long id) {
        Restaurant restaurant = restaurantService.getRestaurantById(id);
        return ResponseEntity.ok(toResponse(restaurant));
    }

    @GetMapping("/search")
    @Operation(summary = "Search restaurants by name")
    public ResponseEntity<List<RestaurantResponse>> searchRestaurants(
            @RequestParam String keyword) {

        List<Restaurant> restaurants = restaurantService.searchRestaurants(keyword);
        List<RestaurantResponse> response = restaurants.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "Create new restaurant")
    public ResponseEntity<RestaurantResponse> createRestaurant(
            @Valid @RequestBody CreateRestaurantRequest request) {

        Restaurant restaurant = toEntity(request);
        Restaurant created = restaurantService.createRestaurant(restaurant);

        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(created));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update restaurant")
    public ResponseEntity<RestaurantResponse> updateRestaurant(
            @PathVariable Long id,
            @Valid @RequestBody CreateRestaurantRequest request) {

        Restaurant restaurant = toEntity(request);
        Restaurant updated = restaurantService.updateRestaurant(id, restaurant);

        return ResponseEntity.ok(toResponse(updated));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete restaurant (soft delete)")
    public ResponseEntity<Void> deleteRestaurant(@PathVariable Long id) {
        restaurantService.deleteRestaurant(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/is-open")
    @Operation(summary = "Check if restaurant is currently open")
    public ResponseEntity<Boolean> isRestaurantOpen(@PathVariable Long id) {
        boolean isOpen = restaurantService.isRestaurantOpen(id);
        return ResponseEntity.ok(isOpen);
    }

    // Helper methods
    private Restaurant toEntity(CreateRestaurantRequest request) {
        return Restaurant.builder()
                .name(request.getName())
                .description(request.getDescription())
                .address(request.getAddress())
                .phone(request.getPhone())
                .openingTime(request.getOpeningTime())
                .closingTime(request.getClosingTime())
                .isActive(request.getIsActive())
                .build();
    }

    private RestaurantResponse toResponse(Restaurant restaurant) {
        return RestaurantResponse.builder()
                .id(restaurant.getId())
                .name(restaurant.getName())
                .description(restaurant.getDescription())
                .address(restaurant.getAddress())
                .phone(restaurant.getPhone())
                .isActive(restaurant.getIsActive())
                .openingTime(restaurant.getOpeningTime())
                .closingTime(restaurant.getClosingTime())
                .createdAt(restaurant.getCreatedAt())
                .updatedAt(restaurant.getUpdatedAt())
                .build();
    }
}
