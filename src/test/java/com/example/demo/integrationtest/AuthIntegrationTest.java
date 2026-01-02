package com.example.demo.integrationtest;

import com.example.demo.baseintegrationtest.BaseIntegrationTest;
import com.example.demo.dto.LoginResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AuthIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private TestRestTemplate rest;

    @Test
    void register_and_login_successfully() {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("x-tenant-id", "tenant1");


        Map<String, String> register = Map.of("email", "user1@nike.com", "password", "123456");

        HttpEntity<Map<String, String>> registerEntity = new HttpEntity<>(register, headers);

        rest.postForEntity("/auth/register", registerEntity, Void.class);
        Map<String, String> loginReq = Map.of("email", "user1@nike.com", "password", "123456");

        HttpEntity<Map<String, String>> loginEntity = new HttpEntity<>(loginReq, headers);

        ResponseEntity<LoginResponse> login = rest.postForEntity("/auth/login", loginEntity, LoginResponse.class);

        assertEquals(HttpStatus.OK, login.getStatusCode());
        assertNotNull(login.getBody());
        assertNotNull(login.getBody().accessToken());
    }
}
