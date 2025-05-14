package at.aau.serg.scotlandyard.controller;

import at.aau.serg.scotlandyard.service.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @Test
    void register_ShouldCallAuthServiceRegisterAndReturnResult() {
        // Arrange
        String username = "testUser";
        String password = "testPassword";
        String expectedResult = "Registration successful";
        when(authService.register(username, password)).thenReturn(expectedResult);

        // Act
        String actualResult = authController.register(username, password);

        // Assert
        assertEquals(expectedResult, actualResult);
        verify(authService, times(1)).register(username, password);
    }

    @Test
    void login_ShouldCallAuthServiceLoginAndReturnResult() {
        // Arrange
        String username = "existingUser";
        String password = "correctPassword";
        String expectedResult = "Login successful";
        when(authService.login(username, password)).thenReturn(expectedResult);

        // Act
        String actualResult = authController.login(username, password);

        // Assert
        assertEquals(expectedResult, actualResult);
        verify(authService, times(1)).login(username, password);
    }

    @Test
    void register_ShouldHandleRegistrationFailure() {
        // Arrange
        String username = "duplicateUser";
        String password = "anyPassword";
        String expectedResult = "Username already exists";
        when(authService.register(username, password)).thenReturn(expectedResult);

        // Act
        String actualResult = authController.register(username, password);

        // Assert
        assertEquals(expectedResult, actualResult);
        verify(authService, times(1)).register(username, password);
    }

    @Test
    void login_ShouldHandleLoginFailure() {
        // Arrange
        String username = "wrongUser";
        String password = "wrongPassword";
        String expectedResult = "Invalid credentials";
        when(authService.login(username, password)).thenReturn(expectedResult);

        // Act
        String actualResult = authController.login(username, password);

        // Assert
        assertEquals(expectedResult, actualResult);
        verify(authService, times(1)).login(username, password);
    }
}