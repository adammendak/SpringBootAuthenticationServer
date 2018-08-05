package com.adammendak.authentication.bootstrap;

import com.adammendak.authentication.model.User;
import com.adammendak.authentication.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Profile(value = "dev")
@Component
@Slf4j
public class Bootstrap implements CommandLineRunner {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public Bootstrap(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {

        User testUser = new User();
        testUser.setId(1L);
        testUser.setLogin("test");
        testUser.setPassword(bCryptPasswordEncoder.encode("test"));

        userRepository.save(testUser);
        log.info("added testUser login: {} password {}", testUser.getLogin(), testUser.getPassword());

    }
}
