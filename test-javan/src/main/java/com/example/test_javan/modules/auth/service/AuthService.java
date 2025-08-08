package com.example.test_javan.modules.auth.service;

import com.example.test_javan.modules.auth.dto.LoginRequest;
import com.example.test_javan.modules.auth.dto.LoginResponse;

public interface AuthService {
    LoginResponse login(LoginRequest request);
    LoginResponse refreshToken(String refreshToken);
}
