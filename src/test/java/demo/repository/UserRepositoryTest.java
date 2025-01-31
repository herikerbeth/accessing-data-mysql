package demo.repository;

import demo.TestData;
import demo.model.User;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.StreamSupport;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void cleanDatabase() {
        userRepository.deleteAll();
        assertThat(userRepository.findAll()).isEmpty();
    }

    @Test
    void shouldSavedAndQueryAllUser() {
        User newUser = TestData.newUser();
        userRepository.save(newUser);

        Iterable<User> users = userRepository.findAll();

        List<User> userList = StreamSupport.stream(users.spliterator(), false)
                .toList();

        assertThat(users).isNotEmpty();
        assertThat(users).hasSize(1);
        User firstUser = userList.get(0);
        assertThat(firstUser.getId()).isEqualTo(1);
        assertThat(firstUser.getName()).isEqualTo(newUser.getName());
        assertThat(firstUser.getEmail()).isEqualTo(newUser.getEmail());
    }

    @AfterEach
    void cleanData() {
        userRepository.deleteAll();
        assertThat(userRepository.findAll()).isEmpty();
    }
}
