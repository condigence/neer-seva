package com.sample.sample.controller;

import com.sample.sample.entity.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

public class SystemTest {

//    @Test
//    public void testCreateReadDelete() {
//        RestTemplate restTemplate = new RestTemplate();
//
//        String url = "http://localhost:8080/employee";
//
//        User user = new User();
//        user.setUserId(1l);
//        user.setUserName("Vish");
//        user.setDepartmentId(1l);
//
//        ResponseEntity<User> entity = restTemplate.postForEntity(url, user, User.class);
//
//        User[] employees = restTemplate.getForObject(url, User[].class);
//        Assertions.assertThat(employees).extracting(User::getUserName).containsOnly("Vish");
//
////        restTemplate.delete(url + "/" + entity.getBody().getId());
////        Assertions.assertThat(restTemplate.getForObject(url, User[].class)).isEmpty();
//    }

//    @Test
//    public void testErrorHandlingReturnsBadRequest() {
//
//        RestTemplate restTemplate = new RestTemplate();
//        String url = "http://localhost:9099/error";
//
//        try {
//            restTemplate.getForEntity(url, String.class);
//        } catch (HttpClientErrorException e) {
//            Assertions.assertThat(e.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
//        }
//    }

}
