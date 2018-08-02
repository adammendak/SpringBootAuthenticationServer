package com.adammendak.authentication.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@CorsFilter
public class TestController {

    @GetMapping(path = "/api/test")
    public ResponseEntity<String> testContoller() {

        return ResponseEntity.ok().header("Content-Type","application/json;; charset=UTF-8").body("test works");
    }
}
