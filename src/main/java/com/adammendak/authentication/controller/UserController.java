package com.adammendak.authentication.controller;

import com.adammendak.authentication.model.Role;
import com.adammendak.authentication.model.User;
import com.adammendak.authentication.model.dto.UserDto;
import com.adammendak.authentication.model.mapper.UserMapperImpl;
import com.adammendak.authentication.repository.RoleRepository;
import com.adammendak.authentication.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Optional;

@RestController()
@RequestMapping( value = "/api/user", produces = MediaType.APPLICATION_JSON_VALUE )
@Slf4j
public class UserController {

    private final String ROLE_USER = "ROLE_USER";

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RoleRepository roleRepository;

    public UserController(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder,
                          RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.roleRepository = roleRepository;
    }

    @GetMapping("/getInfo")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> getUserInfo(UsernamePasswordAuthenticationToken userToken) {
        Optional<User> userFromDB = userRepository.findByLogin(userToken.getPrincipal().toString());
        if (userHasPermissionToRead()) {
            return ResponseEntity.ok().body(UserMapperImpl.INSTANCE.userToUserDto(userFromDB.get()));
        } else {
            return ResponseEntity.status(403).body("no privilage to use this method");
        }
    }

    @PostMapping
    public void signUp(@RequestBody UserDto userDto) {

        User newUser = UserMapperImpl.INSTANCE.userDtoToUser(userDto);
        newUser.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));
        try {
            newUser.setRoles(Arrays.asList(new Role(ROLE_USER)));
            User savedUser = userRepository.save(newUser);
            log.info("new user with id {} and login {}", savedUser.getId(), savedUser.getLogin());
        } catch (Exception e){
            log.error("Exception : ", e.getMessage());
        }
    }

    private Boolean userHasPermissionToRead( ) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("User Permissions{}", SecurityContextHolder.getContext().getAuthentication().getAuthorities());

        Optional<User> userToCheck = userRepository.findByLogin(username);
        Role userRole = roleRepository.findByName(ROLE_USER);

        if(userToCheck.get().getRoles().contains(userRole)) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }
}
