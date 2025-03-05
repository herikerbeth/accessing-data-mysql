package demo.repository;

import demo.TestData;
import demo.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.stream.StreamSupport;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@DataJpaTest
public class UserRepositoryTest {

    @Container
    static MySQLContainer<?> mysqlContainer = new MySQLContainer<>("mysql:latest");

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mysqlContainer::getUsername);
        registry.add("spring.datasource.password", mysqlContainer::getPassword);
    }

    @Autowired
    private UserRepository userRepository;

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
}
