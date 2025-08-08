package com.example.test_javan.modules.auth.controller;

import com.example.test_javan.dto.BaseResponse;
import com.example.test_javan.modules.auth.dto.LoginRequest;
import com.example.test_javan.modules.auth.dto.LoginResponse;
import com.example.test_javan.modules.auth.dto.RefreshTokenRequest;
import com.example.test_javan.modules.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Login and get access & refresh token")
    @PostMapping("/login")
    public ResponseEntity<BaseResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(
                BaseResponse.<LoginResponse>builder()
                        .status(200)
                        .message("Login successful")
                        .data(response)
                        .build()
        );
    }

    @Operation(
            summary = "Test secured endpoint with valid access token",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/test-access")
    public ResponseEntity<BaseResponse<String>> testLogin() {
        return ResponseEntity.ok(
                BaseResponse.<String>builder()
                        .status(200)
                        .message("Access Token is valid. You are authenticated.")
                        .data("You are inside a secured endpoint")
                        .build()
        );
    }

    @Operation(summary = "Refresh token and get new access & refresh token")
    @PostMapping("/refresh-token")
    public ResponseEntity<BaseResponse<LoginResponse>> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        LoginResponse response = authService.refreshToken(request.getRefreshToken());
        return ResponseEntity.ok(
                BaseResponse.<LoginResponse>builder()
                        .status(200)
                        .message("Token refreshed successfully")
                        .data(response)
                        .build()
        );
    }

}
