package com.condigence.imageservice.service;

import com.condigence.imageservice.util.RestConnector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class ExternalServiceTest {

    private RestConnector restConnector;
    private ExternalService externalService;

    @BeforeEach
    void setUp() {
        restConnector = Mockito.mock(RestConnector.class);
        // Provide a non-empty URL via constructor
        externalService = new ExternalService(restConnector, "http://localhost:12345/ping");
    }

    @Test
    void pingExternal_success() {
        when(restConnector.get(anyString(), Mockito.eq(String.class)))
                .thenReturn(new ResponseEntity<>("pong", HttpStatus.OK));

        ResponseEntity<String> resp = externalService.pingExternal();
        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertEquals("pong", resp.getBody());
    }

    @Test
    void pingExternal_notConfigured() {
        ExternalService es2 = new ExternalService(restConnector, "");
        ResponseEntity<String> resp = es2.pingExternal();
        assertEquals(HttpStatus.NOT_FOUND, resp.getStatusCode());
    }
}

