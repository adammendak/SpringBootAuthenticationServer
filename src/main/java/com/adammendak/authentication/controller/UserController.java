package com.adammendak.authentication.controller;

import com.adammendak.authentication.model.User;
import com.adammendak.authentication.model.dto.UserDto;
import com.adammendak.authentication.model.mapper.UserMapperImpl;
import com.adammendak.authentication.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping( value = "/api/user", produces = MediaType.APPLICATION_JSON_VALUE )
@Slf4j
public class UserController {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserController(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @PostMapping
    public void signUp(@RequestBody UserDto userDto) {

        User newUser = UserMapperImpl.INSTANCE.userDtoToUser(userDto);
        newUser.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));
        try {
            User savedUser = userRepository.save(newUser);
            log.info("new user with id {} and login ", savedUser.getId(), savedUser.getLogin());
        } catch (Exception e){
            log.error("Exception : ", e.getMessage());
        }
    }
}
