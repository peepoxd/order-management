package com.delivery.order_management.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.delivery.order_management.model.entity.Restaurant;

@DataJpaTest
@ActiveProfiles("test")
class RestaurantRepositoryTest {

    @Autowired
    private RestaurantRepository restaurantRepository;

    private Restaurant testRestaurant;

    @BeforeEach
    void setUp() {
        testRestaurant = Restaurant.builder()
                .name("Test Restaurant")
                .description("A test restaurant")
                .address("123 Test St")
                .phone("0812345678")
                .isActive(true)
                .openingTime(LocalTime.of(9, 0))
                .closingTime(LocalTime.of(22, 0))
                .build();
    }

    @Test
    void shouldSaveRestaurant() {
        // When
        Restaurant saved = restaurantRepository.save(testRestaurant);

        // Then
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo("Test Restaurant");
    }

    @Test
    void shouldFindByNameIgnoreCase() {
        // Given
        restaurantRepository.save(testRestaurant);

        // When
        Optional<Restaurant> found = restaurantRepository.findByNameIgnoreCase("test restaurant");

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Test Restaurant");
    }

    @Test
    void shouldFindActiveRestaurants() {
        // Given
        restaurantRepository.save(testRestaurant);

        Restaurant inactive = Restaurant.builder()
                .name("Inactive Restaurant")
                .isActive(false)
                .build();
        restaurantRepository.save(inactive);

        // When
        List<Restaurant> activeRestaurants = restaurantRepository.findByIsActiveTrue();

        // Then
        assertThat(activeRestaurants).hasSize(1);
        assertThat(activeRestaurants.get(0).getName()).isEqualTo("Test Restaurant");
    }
}
