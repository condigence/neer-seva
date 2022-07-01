package com.sample.sample.service;


import com.sample.sample.entity.User;
import com.sample.sample.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
	
	@Autowired
    UserRepository userRepository;

    public List<User> getAll(){
//        List<User> userList = new ArrayList<User>();
//        User user = new User();
//        user.setUserId(1l);
//        user.setUserName("Hello");
//        user.setDepartmentId(1l);
//        userList.add(user);



        return userRepository.findAll();
    }


    public void save(User user) {
        userRepository.save(user);
    }

    public Optional<User> findById(long id) {
        return userRepository.findById(id);
    }
}
