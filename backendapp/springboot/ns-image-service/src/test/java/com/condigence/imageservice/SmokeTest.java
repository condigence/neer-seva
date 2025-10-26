package com.condigence.imageservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("local")
public class SmokeTest {

    @Test
    void contextLoads() {
        // If the Spring context starts with the local profile, this test passes
    }
}

