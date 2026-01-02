package com.example.demo.dto;


import lombok.Data;


public record LoginResponse(String accessToken, String refreshToken) {}
