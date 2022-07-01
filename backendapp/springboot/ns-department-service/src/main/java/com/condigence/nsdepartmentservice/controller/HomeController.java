package com.condigence.nsdepartmentservice.controller;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    private static final String TEMPLATE = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping("/greeting/{name}")
    public String greeting(@PathVariable String name) {
        return counter.incrementAndGet()+
                            String.format(TEMPLATE, name);
    }

//    @RequestMapping(method = RequestMethod.GET)
//    public JSONObject HelloWorld() {
//        JSONObject res = new JSONObject();
//        res.put("data", "hello world!");
//        res.put("errCode", 0);
//        return res;
//    }
}
