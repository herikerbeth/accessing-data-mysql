package demo;

import demo.model.User;

public class TestData {

    public static User newUser() {
        return new User("John Doe", "john@example.com");
    }
}
