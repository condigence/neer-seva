package com.sample.sample.controller;

import com.sample.sample.entity.User;
import com.sample.sample.service.UserService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

@WebMvcTest(UserController.class)
@ExtendWith(SpringExtension.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Test
    public void testOk() {
        Assertions.assertEquals(userController.test(), "OK");
    }

    @Test
    public void getAllUsers_success() throws Exception {
        List<User> userList = new ArrayList<>();
        User user = new User();
        user.setUserId(1l);
        user.setUserName("Vish");
        user.setDepartmentId(1l);
        userList.add(user);

        Mockito.when(userService.getAll()).thenReturn(userList);
        mockMvc.perform(
                get("http://localhost:9099/all").
                        contentType(MediaType.APPLICATION_JSON)).
                andExpect(jsonPath("$", Matchers.hasSize(1))).
                andExpect(jsonPath("$[0].userName", Matchers.is("Vish"))).
                andExpect(status().isOk());

    }


}
