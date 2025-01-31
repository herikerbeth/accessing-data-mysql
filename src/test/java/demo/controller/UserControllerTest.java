package demo.controller;

import demo.TestData;
import demo.model.User;
import demo.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserRepository repository;

    @Test
    void shouldAddNewUser() throws Exception {

        when(repository.save(any(User.class))).thenReturn(new User());

        mockMvc.perform(post("/user/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("name", TestData.newUser().getName())
                        .param("email", TestData.newUser().getEmail()))
                .andExpect(status().isOk())
                .andExpect(content().string("Saved"));
    }

    @Test
    void shouldGetAllUsers() throws Exception {
        User user = TestData.newUser();
        User user1 = TestData.newUser();

        when(repository.findAll()).thenReturn(Arrays.asList(user, user1));

        mockMvc.perform(get("/user/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value(user.getName()))
                .andExpect(jsonPath("$[0].email").value(user.getEmail()));
    }
}
