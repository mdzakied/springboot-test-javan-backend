package com.example.test_javan.modules.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
@Schema(description = "Login response")
public class LoginResponse {

    @Schema(example = "eyJhbGciOiJIUzI1NiIsInR...")
    private String accessToken;

    @Schema(example = "eyJhbGciOiJIUzI1NiIsInR...")
    private String refreshToken;
}
