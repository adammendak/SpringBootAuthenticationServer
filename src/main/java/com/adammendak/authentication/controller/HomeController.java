package com.adammendak.authentication.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController(value = "/")
public class HomeController {

    @GetMapping
    public String home() {
        return "works";
    }

}
