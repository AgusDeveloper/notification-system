package com.example.notification_system.controller;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class HomeControllerTest {

    private final HomeController controller = new HomeController();

    @Test
    void homeShouldReturnServiceIndex() {
        Map<String, Object> result = controller.home();

        assertEquals("notification-system", result.get("application"));
        assertEquals("running", result.get("status"));
        assertTrue(result.containsKey("endpoints"));
    }
}
