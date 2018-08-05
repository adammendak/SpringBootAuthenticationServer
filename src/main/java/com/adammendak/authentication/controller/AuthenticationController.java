package com.adammendak.authentication.controller;

import com.adammendak.authentication.model.User;
import com.adammendak.authentication.model.dto.UserDto;
import com.adammendak.authentication.model.mapper.UserMapperImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping( value = "/auth", produces = MediaType.APPLICATION_JSON_VALUE )
@Slf4j
public class AuthenticationController {

    @PostMapping(path = "/login")
    public ResponseEntity<?> login(@RequestBody UserDto userDto) {

        User user = UserMapperImpl.INSTANCE.userDtoToUser(userDto);
        log.info(user.toString());

        return ResponseEntity.ok().header("Content-Type","application/json;; charset=UTF-8").body("test works");
    }


}
