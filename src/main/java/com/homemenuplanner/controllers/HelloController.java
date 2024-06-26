package com.homemenuplanner.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello, was the rolling restart successful?";
    }

    @GetMapping("/helloAuth")
    public String helloAuth() {
        return "Hello from Home Menu Planner - Authenticated";
    }

}
