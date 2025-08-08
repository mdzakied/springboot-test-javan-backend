package com.example.test_javan.modules.auth.service.impl;

import com.example.test_javan.exception.InvalidCredentialException;
import com.example.test_javan.modules.auth.dto.LoginRequest;
import com.example.test_javan.modules.auth.dto.LoginResponse;
import com.example.test_javan.modules.auth.util.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class AuthServiceImplTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private UserDetailsService userDetailsService;

    @InjectMocks
    private AuthServiceImpl authServiceImpl;

    @BeforeEach
    void setUp() {
        // Initialize all @Mock annotations
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void login_success() {
        // Mock data
        LoginRequest request = LoginRequest.builder()
                .username("john")
                .password("password")
                .build();

        // Mock authentication process
        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.getName()).thenReturn("john");

        // Mock JWT token generation
        when(jwtUtils.generateAccessToken("john")).thenReturn("access-token");
        when(jwtUtils.generateRefreshToken("john")).thenReturn("refresh-token");

        // Act
        LoginResponse response = authServiceImpl.login(request);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getAccessToken()).isEqualTo("access-token");
        assertThat(response.getRefreshToken()).isEqualTo("refresh-token");
    }

    @Test
    void login_invalidCredentials_throwsException() {
        // Mock data
        LoginRequest request = LoginRequest.builder()
                .username("wrong")
                .password("wrongpass")
                .build();

        // Mock authentication failure process
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        // Assert -> Verify that custom exception is thrown
        assertThatThrownBy(() -> authServiceImpl.login(request))
                .isInstanceOf(InvalidCredentialException.class)
                .hasMessage("Invalid username or password");
    }


    @Test
    void refreshToken_success() {
        // Mock data
        String refreshToken = "valid-refresh";

        // Mock valid refresh token
        when(jwtUtils.validateRefreshToken(refreshToken)).thenReturn(true);
        when(jwtUtils.getUsernameFromRefreshToken(refreshToken)).thenReturn("john");

        // Mock user details
        UserDetails userDetails = User.withUsername("john").password("pass").roles("USER").build();
        when(userDetailsService.loadUserByUsername("john")).thenReturn(userDetails);

        // Mock new tokens
        when(jwtUtils.generateAccessToken("john")).thenReturn("new-access");
        when(jwtUtils.generateRefreshToken("john")).thenReturn("new-refresh");

        // Act
        LoginResponse response = authServiceImpl.refreshToken(refreshToken);

        // Assert
        assertThat(response.getAccessToken()).isEqualTo("new-access");
        assertThat(response.getRefreshToken()).isEqualTo("new-refresh");
    }

    @Test
    void refreshToken_invalidToken_throwsException() {
        // Mock data
        String refreshToken = "invalid-refresh";

        // Mock invalid refresh token
        when(jwtUtils.validateRefreshToken(refreshToken)).thenReturn(false);

        // Assert > Verify that custom exception is thrown
        assertThatThrownBy(() -> authServiceImpl.refreshToken(refreshToken))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Invalid refresh token");
    }
}
