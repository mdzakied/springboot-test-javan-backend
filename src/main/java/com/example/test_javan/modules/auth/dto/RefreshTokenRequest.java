package com.example.test_javan.modules.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "Refresh Token request payload")
public class RefreshTokenRequest {
    @Schema(example = "eyJhbGciOiJIUzI1NiIsInR...")
    @NotBlank(message = "Refresh Token is required")
    private String refreshToken;
}
