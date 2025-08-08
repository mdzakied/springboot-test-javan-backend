package com.example.test_javan.exception;

import com.example.test_javan.dto.BaseResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void handleInvalidCredential_shouldReturnUnauthorizedResponse() {
        // Mock data
        String errorMsg = "Invalid username or password";
        InvalidCredentialException ex = new InvalidCredentialException(errorMsg);

        // Act
        ResponseEntity<BaseResponse<Void>> responseEntity = exceptionHandler.handleInvalidCredential(ex);

        // Assert
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(401);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().getStatus()).isEqualTo(401);
        assertThat(responseEntity.getBody().getMessage()).isEqualTo(errorMsg);
        assertThat(responseEntity.getBody().getData()).isNull();
    }

    @Test
    void handleValidationExceptions_shouldReturnBadRequestResponse() {
        // Mock data
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("objectName", "username", "must not be blank");
        when(bindingResult.getFieldErrors()).thenReturn(Collections.singletonList(fieldError));

        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, bindingResult);

        // Act
        ResponseEntity<BaseResponse<Object>> responseEntity = exceptionHandler.handleValidationExceptions(ex);

        // Assert
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(400);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().getStatus()).isEqualTo(400);
        assertThat(responseEntity.getBody().getMessage()).isEqualTo("username: must not be blank");
        assertThat(responseEntity.getBody().getData()).isNull();
    }

    @Test
    void handleValidationExceptions_shouldReturnDefaultMessage_whenNoFieldErrors() {
        // Mock data
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.getFieldErrors()).thenReturn(Collections.emptyList());

        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, bindingResult);

        // Act
        ResponseEntity<BaseResponse<Object>> responseEntity = exceptionHandler.handleValidationExceptions(ex);

        // Assert
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().getMessage()).isEqualTo("Validation error");
    }
}
