package com.sample.sample.repository;


import com.sample.sample.entity.User;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import org.assertj.core.api.Assertions;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

//    @Test
//    public void testCreateReadDelete(){
//
//        User user = new User();
//        user.setUserName("Vish");
//        user.setUserId(1l);
//        user.setDepartmentId(1l);
//
//        userRepository.save(user);
//
//        Iterable<User> users = userRepository.findAll();
//
//        Assertions.assertThat(users).extracting(User::getUserName).containsOnly("Vish");
//
//        userRepository.deleteAll();
//        Assertions.assertThat(userRepository.findAll()).isEmpty();
//
//
//    }

}
