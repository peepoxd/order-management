package com.delivery.order_management.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Connection;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
class ConnectionTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    void testDatabaseConnection() throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            assertNotNull(connection);
            assertTrue(connection.isValid(1));
        }
    }

    @Test
    void testRedisConnection() {
        redisTemplate.opsForValue().set("test-key", "test-value");
        String value = (String) redisTemplate.opsForValue().get("test-key");
        assertEquals("test-value", value);
        redisTemplate.delete("test-key");
    }
}
