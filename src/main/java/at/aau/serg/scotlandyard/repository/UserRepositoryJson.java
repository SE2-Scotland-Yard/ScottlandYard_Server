package at.aau.serg.scotlandyard.repository;

import at.aau.serg.scotlandyard.model.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.util.*;
@Repository
public class UserRepositoryJson {
    private static final String FILE_PATH = "users.json";
    private final ObjectMapper objectMapper = new ObjectMapper();
    private List<User> users;

    public UserRepositoryJson() {
        loadUsers();
    }

    private void loadUsers() {
        try {
            File file = new File(FILE_PATH);
            if (file.exists()) {
                users = objectMapper.readValue(file, new TypeReference<List<User>>() {});
            } else {
                users = new ArrayList<>();
            }
        } catch (IOException e) {
            e.printStackTrace();
            users = new ArrayList<>();
        }
    }

    private void saveUsers() {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(FILE_PATH), users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<User> findAll() {
        return new ArrayList<>(users);
    }

    public Optional<User> findByUsername(String username) {
        return users.stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst();
    }

    public void save(User user) {
        if (user.getId() == null) {
            user.setId(generateId());
        }
        users.add(user);
        saveUsers();
    }

    public void deleteByUsername(String username) {
        users.removeIf(u -> u.getUsername().equals(username));
        saveUsers();
    }




    private Long generateId() {
        return users.stream()
                .mapToLong(User::getId)
                .max()
                .orElse(0L) + 1;
    }
}
