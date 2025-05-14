package at.aau.serg.scotlandyard.repository;

import at.aau.serg.scotlandyard.model.User;
import org.junit.jupiter.api.*;
import java.io.File;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

public class UserRepositoryJsonTest {

    private static final String PROD_FILE = "users.json";
    private static final String TEST_FILE = "users_test.json";
    private UserRepositoryJson repo;

    @BeforeEach
    void setUp(){
        // Testdatei vor jedem Test löschen
        File prod = new File((PROD_FILE));
        File test = new File(TEST_FILE);
        if (prod.exists()) {
            prod.renameTo(test);
        }
        if (prod.exists()) prod.delete();
        repo = new UserRepositoryJson();

    }

    @AfterEach
    void tearDown() {
        File prod = new File(PROD_FILE);
        if (prod.exists()) prod.delete();
        File test = new File(TEST_FILE);
        if (test.exists()) test.renameTo(prod);
    }

    @Test
    void testSaveAndFindAll() {
        User user1 = new User("alice", "pw1");
        User user2 = new User("bob", "pw2");

        repo.save(user1);
        repo.save(user2);

        List<User> all = repo.findAll();
        assertThat(all).hasSize(2);
        assertThat(all).extracting(User::getUsername).containsExactlyInAnyOrder("alice", "bob");
    }

    @Test
    void testFindByUsername() {
        User user = new User("charlie", "pw3");
        repo.save(user);

        Optional<User> found = repo.findByUsername("charlie");
        assertThat(found).isPresent();
        assertThat(found.get().getPassword()).isEqualTo("pw3");
    }

    @Test
    void testFindByUsernameNotExists() {
        assertThat(repo.findByUsername("nobody")).isEmpty();
    }

    @Test
    void testDeleteByUsername() {
        repo.save(new User("dave", "pw4"));
        assertThat(repo.findByUsername("dave")).isPresent();

        repo.deleteByUsername("dave");
        assertThat(repo.findByUsername("dave")).isEmpty();
    }

    /*@Test
    void testGeneratedIdsAreIncremented() {
        repo.save(new User("eve", "pw5"));
        repo.save(new User("frank", "pw6"));
        List<User> users = repo.findAll();
        assertThat(users.get(0).getId()).isNotNull();
        assertThat(users.get(1).getId()).isEqualTo(users.get(0).getId() + 1);
    }*/
}
