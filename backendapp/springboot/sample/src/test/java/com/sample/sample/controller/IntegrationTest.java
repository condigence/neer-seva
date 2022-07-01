package com.sample.sample.controller;



import com.sample.sample.entity.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.validation.ValidationException;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class IntegrationTest {


    @Autowired
    UserController userController;

//    @Test
//    public void testCreateReadDelete() {
//
//        User user = new User();
//        user.setUserId(1l);
//        user.setUserName("Vish");
//        user.setDepartmentId(1l);
//        User userResult = userController.create(user);
//        Iterable<User> employees = userController.read();
//        Assertions.assertThat(employees).first().hasFieldOrPropertyWithValue("userName", "Vish");
//        userController.delete(userResult.getId());
//        Assertions.assertThat(userController.read()).isEmpty();
//    }
//
//    @Test
//    public void errorHandlingValidationExceptionThrown() {
//        Assertions.assertThatExceptionOfType(ValidationException.class)
//                .isThrownBy(() -> userController.somethingIsWrong());
//    }
}
