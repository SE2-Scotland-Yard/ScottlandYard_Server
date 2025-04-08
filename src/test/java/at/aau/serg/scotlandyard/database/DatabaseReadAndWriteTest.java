package at.aau.serg.scotlandyard.database;

import at.aau.serg.scotlandyard.Application;
import at.aau.serg.scotlandyard.repository.UserRepository;
import at.aau.serg.scotlandyard.model.User;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = Application.class)
@Transactional // Rolls back DB changes after each test
public class DatabaseReadAndWriteTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testWriteAndReadUser() {
        User testUser = new User();
        testUser.setUsername("testuser123");
        testUser.setPassword("secret123");

        userRepository.save(testUser);

        User loaded = userRepository.findByUsername("testuser123").orElse(null);

        assertNotNull(loaded, "User should be loaded from the database");
        assertEquals("testuser123", loaded.getUsername());
        assertEquals("secret123", loaded.getPassword());
    }
}
