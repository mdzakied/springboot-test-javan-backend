package com.example.test_javan.modules.auth.controller;

import com.example.test_javan.dto.BaseResponse;
import com.example.test_javan.modules.auth.dto.LoginRequest;
import com.example.test_javan.modules.auth.dto.LoginResponse;
import com.example.test_javan.modules.auth.dto.RefreshTokenRequest;
import com.example.test_javan.modules.auth.service.AuthService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    public AuthControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void login() {
        // Mock data
        LoginRequest request = LoginRequest.builder()
                .username("john")
                .password("secret")
                .build();
        LoginResponse mockResponse = LoginResponse.builder()
                .accessToken("accessToken123")
                .refreshToken("refreshToken123")
                .build();
        when(authService.login(any(LoginRequest.class))).thenReturn(mockResponse);

        // Act
        ResponseEntity<BaseResponse<LoginResponse>> responseEntity = authController.login(request);

        // Assert
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().getData()).isEqualTo(mockResponse);
        assertThat(responseEntity.getBody().getMessage()).isEqualTo("Login successful");
    }

    @Test
    void testLogin() {
        // Act
        ResponseEntity<BaseResponse<String>> responseEntity = authController.testLogin();

        // Assert
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().getData())
                .isEqualTo("You are inside a secured endpoint");
        assertThat(responseEntity.getBody().getMessage())
                .isEqualTo("Access Token is valid. You are authenticated.");
    }

    @Test
    void refreshToken() {
        // Mock data
        RefreshTokenRequest request = RefreshTokenRequest.builder()
                .refreshToken("refreshToken123")
                .build();
        LoginResponse mockResponse = LoginResponse.builder()
                .accessToken("newAccessToken")
                .refreshToken("newRefreshToken")
                .build();
        when(authService.refreshToken(request.getRefreshToken())).thenReturn(mockResponse);

        // Act
        ResponseEntity<BaseResponse<LoginResponse>> responseEntity = authController.refreshToken(request);

        // Assert
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().getData()).isEqualTo(mockResponse);
        assertThat(responseEntity.getBody().getMessage()).isEqualTo("Token refreshed successfully");
    }
}
