package com.delivery.order_management.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.delivery.order_management.exception.ResourceNotFoundException;
import com.delivery.order_management.model.entity.Restaurant;
import com.delivery.order_management.repository.RestaurantRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    /**
     * Get all restaurants
     */
    public List<Restaurant> getAllRestaurants() {
        log.info("Fetching all restaurants");
        return restaurantRepository.findAll();
    }

    /**
     * Get active restaurants only
     */
    public List<Restaurant> getActiveRestaurants() {
        log.info("Fetching active restaurants");
        return restaurantRepository.findByIsActiveTrue();
    }

    /**
     * Get restaurant by ID
     */
    public Restaurant getRestaurantById(Long id) {
        log.info("Fetching restaurant with id: {}", id);
        return restaurantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id: " + id));
    }

    /**
     * Search restaurants by name
     */
    public List<Restaurant> searchRestaurants(String keyword) {
        log.info("Searching restaurants with keyword: {}", keyword);
        return restaurantRepository.findByNameContainingIgnoreCase(keyword);
    }

    /**
     * Create new restaurant
     */
    @Transactional
    public Restaurant createRestaurant(Restaurant restaurant) {
        log.info("Creating new restaurant: {}", restaurant.getName());

        // Validate: Check if restaurant name already exists
        if (restaurantRepository.existsByNameIgnoreCase(restaurant.getName())) {
            throw new IllegalArgumentException("Restaurant with name already exists: " + restaurant.getName());
        }

        return restaurantRepository.save(restaurant);
    }

    /**
     * Update restaurant
     */
    @Transactional
    public Restaurant updateRestaurant(Long id, Restaurant restaurantDetails) {
        log.info("Updating restaurant with id: {}", id);

        Restaurant restaurant = getRestaurantById(id);

        // Update fields
        restaurant.setName(restaurantDetails.getName());
        restaurant.setDescription(restaurantDetails.getDescription());
        restaurant.setAddress(restaurantDetails.getAddress());
        restaurant.setPhone(restaurantDetails.getPhone());
        restaurant.setOpeningTime(restaurantDetails.getOpeningTime());
        restaurant.setClosingTime(restaurantDetails.getClosingTime());
        restaurant.setIsActive(restaurantDetails.getIsActive());

        return restaurantRepository.save(restaurant);
    }

    /**
     * Delete restaurant (soft delete by setting inactive)
     */
    @Transactional
    public void deleteRestaurant(Long id) {
        log.info("Deleting restaurant with id: {}", id);

        Restaurant restaurant = getRestaurantById(id);
        restaurant.setIsActive(false);
        restaurantRepository.save(restaurant);
    }

    /**
     * Check if restaurant is open at current time
     */
    public boolean isRestaurantOpen(Long id) {
        Restaurant restaurant = getRestaurantById(id);

        if (!restaurant.getIsActive()) {
            return false;
        }

        java.time.LocalTime now = java.time.LocalTime.now();
        return !now.isBefore(restaurant.getOpeningTime()) &&
                !now.isAfter(restaurant.getClosingTime());
    }
}
