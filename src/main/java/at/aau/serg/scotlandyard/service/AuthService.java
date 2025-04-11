package at.aau.serg.scotlandyard.service;

import at.aau.serg.scotlandyard.model.User;
import at.aau.serg.scotlandyard.repository.UserRepositoryJson;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;



@Service
public class AuthService {

    private final UserRepositoryJson userRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    private static final int MAX_USERNAME_LENGTH = 20;

    public AuthService(UserRepositoryJson userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String register(String username, String password) {

        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            return "Username and password must not be empty";
        }


        if (username.length() > MAX_USERNAME_LENGTH) {
            return "Username cannot be longer than " + MAX_USERNAME_LENGTH + " characters";
        }

        if (userRepository.findByUsername(username).isPresent()) {
            return "Username already exists";
        }

        String hashedPassword = passwordEncoder.encode(password);
        userRepository.save(new User(username, hashedPassword));
        return "Registration successful";
    }

    public String login(String username, String password) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) return "User not found";

        boolean matches = passwordEncoder.matches(password, user.get().getPassword());
        return matches ? "Login successful" : "Incorrect password";
    }
}

