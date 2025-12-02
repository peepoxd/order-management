package com.delivery.order_management.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.delivery.order_management.model.entity.Restaurant;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    // Find by name (case-insensitive)
    Optional<Restaurant> findByNameIgnoreCase(String name);

    // Find active restaurants
    List<Restaurant> findByIsActiveTrue();

    // Find by name containing (search)
    List<Restaurant> findByNameContainingIgnoreCase(String keyword);

    // Custom query: Find restaurants with available menu items
    @Query("SELECT DISTINCT r FROM Restaurant r " +
            "JOIN r.menuItems m " +
            "WHERE r.isActive = true AND m.isAvailable = true")
    List<Restaurant> findRestaurantsWithAvailableItems();

    // Check if restaurant exists by name
    boolean existsByNameIgnoreCase(String name);
}
