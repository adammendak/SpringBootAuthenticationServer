package com.adammendak.authentication.bootstrap;

import com.adammendak.authentication.model.Role;
import com.adammendak.authentication.model.User;
import com.adammendak.authentication.repository.RoleRepository;
import com.adammendak.authentication.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Arrays;

@Profile(value = "dev")
@Component
@Slf4j
public class Bootstrap implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public Bootstrap(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        User testUser = new User();
        testUser.setId(1L);
        testUser.setLogin("test");
        testUser.setEnabled(Boolean.TRUE);
        testUser.setRoles(Arrays.asList(roleRepository.findByName("ROLE_USER")));
        testUser.setPassword(bCryptPasswordEncoder.encode("test"));
        userRepository.save(testUser);
        log.info("added testUser login: {} password {} granted roles contain USER_ROLE : {}", testUser.getLogin(), testUser.getPassword(),
                testUser.getRoles().contains(roleRepository.findByName("ROLE_USER")));
    }
}
