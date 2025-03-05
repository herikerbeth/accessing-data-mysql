package demo.controller;

import demo.TestData;
import demo.model.User;
import demo.repository.UserRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

@SpringBootTest(webEnvironment =
SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerIT {

    @LocalServerPort
    private Integer port;

    static MySQLContainer<?> mysql = new MySQLContainer<>(
            "mysql:latest"
    );

    @BeforeAll
    static void beforeAll() {
        mysql.start();
    }

    @AfterAll
    static void afterAll() {
        mysql.stop();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);
    }

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost:" + port;
        userRepository.deleteAll();
    }

    @Test
    void testAddNewUserAndRetrieveIt() throws Exception {

        User newUser = TestData.newUser();

        given()
                .contentType(ContentType.URLENC)
                .formParam("name", newUser.getName())
                .formParam("email", newUser.getEmail())
                .when()
                .post("/user/add")
                .then()
                .statusCode(200)
                .body(equalTo("Saved"));

        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/user/all")
                .then()
                .statusCode(200)
                .body("$", hasSize(1))
                .body("[0].name", equalTo(newUser.getName()))
                .body("[0].email", equalTo(newUser.getEmail()));
    }
}
