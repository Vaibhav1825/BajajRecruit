package com.apiTesting;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserApiTests {

    private final String BASE_URL = "https://bfhldevapigw.healthrx.co.in/automation-campus/create/user";
    private final RestTemplate restTemplate = new RestTemplate();

    @Test
    void testCreateUserWithValidData() {
        String json = "{ \"firstName\": \"Test\", \"lastName\": \"User\", \"phoneNumber\": 1234567890, \"emailId\": \"test.user@test.com\" }";
        ResponseEntity<String> response = restTemplate.exchange(BASE_URL, HttpMethod.POST, createHttpEntityWithRollNumber(json), String.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void testCreateUserWithDuplicatePhoneNumber() {
        String json = "{ \"firstName\": \"Test\", \"lastName\": \"User\", \"phoneNumber\": 1234567890, \"emailId\": \"unique.email@test.com\" }";
        restTemplate.exchange(BASE_URL, HttpMethod.POST, createHttpEntityWithRollNumber(json), String.class); // First call
        ResponseEntity<String> response = restTemplate.exchange(BASE_URL, HttpMethod.POST, createHttpEntityWithRollNumber(json), String.class); // Second call
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testCreateUserWithoutRollNumber() {
        String json = "{ \"firstName\": \"Test\", \"lastName\": \"User\", \"phoneNumber\": 1234567890, \"emailId\": \"test.user@test.com\" }";
        ResponseEntity<String> response = restTemplate.exchange(BASE_URL, HttpMethod.POST, createHttpEntityWithoutRollNumber(json), String.class);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void testCreateUserWithDuplicateEmail() {
        String json = "{ \"firstName\": \"Test\", \"lastName\": \"User\", \"phoneNumber\": 2345678901, \"emailId\": \"test.user@test.com\" }";
        restTemplate.exchange(BASE_URL, HttpMethod.POST, createHttpEntityWithRollNumber(json), String.class); // First call
        ResponseEntity<String> response = restTemplate.exchange(BASE_URL, HttpMethod.POST, createHttpEntityWithRollNumber(json), String.class); // Second call
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    private HttpEntity<String> createHttpEntityWithRollNumber(String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("roll-number", "1");
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(json, headers);
    }

    private HttpEntity<String> createHttpEntityWithoutRollNumber(String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(json, headers);
    }
}
