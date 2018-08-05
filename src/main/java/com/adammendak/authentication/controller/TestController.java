package com.adammendak.authentication.controller;

import com.adammendak.authentication.model.dto.UserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
public class TestController {

    private final Logger logger = LoggerFactory.getLogger(TestController.class);

    @PostMapping(path = "/api/test")
    public ResponseEntity<String> testContoller(@RequestBody UserDto testUser) {
        logger.info(testUser.toString());

        return ResponseEntity.ok().header("Content-Type","application/json;; charset=UTF-8").body("test works");
    }
}
