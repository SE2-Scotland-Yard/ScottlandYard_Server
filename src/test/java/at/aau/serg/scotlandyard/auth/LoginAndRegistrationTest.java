package at.aau.serg.scotlandyard.auth;

import at.aau.serg.scotlandyard.repository.UserRepositoryJson;
import at.aau.serg.scotlandyard.service.AuthService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoginAndRegistrationTest {

    private AuthService authService;
    private UserRepositoryJson userRepository;

    private final String username = "testuser";
    private final String password = "testpass123";

    @BeforeEach
    public void setUp() {
        userRepository = new UserRepositoryJson();
        authService = new AuthService(userRepository, new BCryptPasswordEncoder());


        userRepository.findByUsername(username).ifPresentOrElse(
                u -> {}, // already exists
                () -> authService.register(username, password)
        );
    }

    @Test
    public void testRegistrationDuplicate() {
        String result = authService.register(username, password);
        assertEquals("Username already exists", result);
    }
    @Test
    public void testRegistration() {
        String result = authService.register("TestRegistration", password);
        assertEquals("Registration successful", result);
    }

    @Test
    public void testLoginSuccess() {
        String result = authService.login(username, password);
        assertEquals("Login successful", result);
    }

    @Test
    public void testLoginWrongPassword() {
        String wrongPassword = "wrongPassword123";
        String result = authService.login(username, wrongPassword);
        assertEquals("Incorrect password", result);
    }

    @Test
    public void testLoginNonexistentUser() {
        String result = authService.login("nonexistent_user_abc", password);
        assertEquals("User not found", result);

    }

    @Test
    public void testRegistrationWithEmptyUsername() {
        String result = authService.register("   ", password);
        assertEquals("Username and password must not be empty", result);
    }

    @Test
    public void testRegistrationWithEmptyPassword() {
        String result = authService.register(username, "");
        assertEquals("Username and password must not be empty", result);
    }

    @AfterEach
    public void tearDown() {
        userRepository.deleteByUsername(username);
        userRepository.deleteByUsername("TestRegistration");
    }



}
