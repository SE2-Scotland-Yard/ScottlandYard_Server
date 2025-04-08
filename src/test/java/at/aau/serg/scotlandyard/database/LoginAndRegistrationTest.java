package at.aau.serg.scotlandyard.database;


import at.aau.serg.scotlandyard.Application;
import at.aau.serg.scotlandyard.model.User;
import at.aau.serg.scotlandyard.repository.UserRepository;
import at.aau.serg.scotlandyard.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = Application.class)
@Transactional // Rolls back DB changes after each test
public class LoginAndRegistrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthService authService;

    @Test
    public void testRegistration() {
       String testusername = "ScotlandYardPro";
       String testpassword = "kslKj#2381";


       String result =authService.register(testusername, testpassword);

       assertEquals("Registration successful", result);

       String result2 = authService.register(testusername,testpassword);

       assertEquals("Username already exists",result2);


    }

    @Test
    public void testLogin(){

        String CorrectUsername = "ScotlandYardPro";
        String WrongUsername = "Scotland";
        String CorrectPassword = "kslKj#2381";
        String WrongPassword = "2348h2834";
        authService.register(CorrectUsername, CorrectPassword);

        String result = authService.login(CorrectUsername, CorrectPassword);

        assertEquals("Login successful", result);

        String result2=authService.login(CorrectUsername, WrongPassword );

        assertEquals("Incorrect password", result2);

        String result3 = authService.login(WrongUsername,CorrectPassword);

        assertEquals("User not found", result3);

    }


}
