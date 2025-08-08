package com.example.test_javan.modules.auth.service.impl;

import com.example.test_javan.modules.auth.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.example.test_javan.modules.auth.entity.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @BeforeEach
    void setUp() {
        // Initialize @Mock annotations
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void loadUserByUsername_success() {
        // Create user
        User mockUser = User.builder()
                .username("john")
                .password("password")
                .build();

        when(userRepository.findByUsername("john")).thenReturn(Optional.of(mockUser));

        // Act
        UserDetails result = userDetailsService.loadUserByUsername("john");

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo("john");
        verify(userRepository, times(1)).findByUsername("john");
    }

    @Test
    void loadUserByUsername_userNotFound_throwsException() {
        // Mock failure process
        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        // Assert -> Verify that custom exception is thrown
        assertThatThrownBy(() -> userDetailsService.loadUserByUsername("unknown"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("User not found with username: unknown");

        verify(userRepository, times(1)).findByUsername("unknown");
    }
}
