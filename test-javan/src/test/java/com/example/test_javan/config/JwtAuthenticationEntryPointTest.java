package com.example.test_javan.config;

import com.example.test_javan.dto.BaseResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class JwtAuthenticationEntryPointTest {

    private JwtAuthenticationEntryPoint entryPoint;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        entryPoint = new JwtAuthenticationEntryPoint();
        objectMapper = new ObjectMapper();
    }

    @Test
    void commence_shouldReturnUnauthorizedJsonResponse() throws IOException, ServletException {
        // Mock data
        HttpServletRequest request = mock(HttpServletRequest.class);
        MockHttpServletResponse response = new MockHttpServletResponse();

        // Act
        entryPoint.commence(request, response, mock(org.springframework.security.core.AuthenticationException.class));

        // Assert: Status dan content type
        assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_UNAUTHORIZED);
        assertThat(response.getContentType()).isEqualTo("application/json");

        // Assert: Body JSON
        BaseResponse<?> actualResponse = objectMapper.readValue(response.getContentAsString(), BaseResponse.class);
        assertThat(actualResponse.getStatus()).isEqualTo(HttpServletResponse.SC_UNAUTHORIZED);
        assertThat(actualResponse.getMessage()).isEqualTo("Unauthorized: Invalid or missing token");
        assertThat(actualResponse.getData()).isNull();
    }
}
