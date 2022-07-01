package com.sample.sample;

import com.sample.sample.controller.UserController;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Assertions;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//https://www.youtube.com/watch?v=sCcuUMn1vdM


@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
class SampleApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    UserController userController;

    @Test
    void contextLoads() {
        Assertions.assertNotNull(userController);
    }

    @Test
    public void getAll() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.
                get("/all").
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk());
    }

//    @Test
//    public void getUser() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.
//                get("/all/1").
//                accept(MediaType.APPLICATION_JSON)).
//                andExpect(status().isOk());
//    }

    @Test
    public void hello() {
        Assertions.assertEquals("Say Hello", "Say Hello");
    }

    @Test
    public void messageEmpty() {
        Assertions.assertEquals("", "");
    }

}
