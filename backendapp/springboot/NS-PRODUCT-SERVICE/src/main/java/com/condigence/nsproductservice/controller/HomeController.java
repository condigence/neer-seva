package com.condigence.nsproductservice.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@CrossOrigin(origins = "*")
public class HomeController {

    @GetMapping(value="/", produces = MediaType.APPLICATION_JSON_VALUE)
    public String home(){
        return "Welcome to our Neerseva Application!";
    }

    @GetMapping(value="/welcome", produces = MediaType.APPLICATION_JSON_VALUE)
    public String welcome(){
        return "WELCOME!";
    }


}
