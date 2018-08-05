package com.adammendak.authentication.bootstrap;

import com.adammendak.authentication.model.User;
import com.adammendak.authentication.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile(value = "dev")
@Component
@Slf4j
public class Bootstrap implements CommandLineRunner {

    private final UserRepository userRepository;

    public Bootstrap(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        User testUser = new User();
        testUser.setId(1L);
        testUser.setLogin("testowyLogin");
        testUser.setPassword("test123");

        userRepository.save(testUser);
        log.info("added testUser login: {} password {}", testUser.getLogin(), testUser.getPassword());

    }
}
