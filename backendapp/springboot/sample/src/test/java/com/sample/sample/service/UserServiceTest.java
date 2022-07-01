package com.sample.sample.service;


import com.sample.sample.entity.User;
import com.sample.sample.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    UserService userService;

    @Mock
    UserRepository userRepository;

    @Test
    public void testGetAllUsers_Success(){
        List<User> list = new ArrayList<>();
        User user = new User();
        user.setUserName("Vish");
        user.setUserId(1l);
        user.setDepartmentId(1l);
        list.add(user);
        when(userRepository.findAll()).thenReturn(list);

        //test
        List<User> userList = userService.getAll();
        assertEquals(1, userList.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void testCreateOrSaveEmployee()
    {
        User user = new User();
        user.setUserName("Vish");
        user.setUserId(1l);
        user.setDepartmentId(1l);
        userService.save(user);
        verify(userRepository, times(1)).save(user);
    }


}
