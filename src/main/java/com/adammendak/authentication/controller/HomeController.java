package com.adammendak.authentication.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController()
@RequestMapping(path = "/")
public class HomeController {

    @GetMapping()
    public String home() {
        Date time = new Date();
        return "Server works: " + time.toString();
    }

}
