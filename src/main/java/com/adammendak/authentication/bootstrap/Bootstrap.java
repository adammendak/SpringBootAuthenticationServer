package com.adammendak.authentication.bootstrap;

import com.adammendak.authentication.model.Privilege;
import com.adammendak.authentication.model.Role;
import com.adammendak.authentication.model.User;
import com.adammendak.authentication.repository.PrivilegeRepository;
import com.adammendak.authentication.repository.RoleRepository;
import com.adammendak.authentication.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;

@Profile(value = "dev")
@Component
@Slf4j
public class Bootstrap implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PrivilegeRepository privilegeRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public Bootstrap(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder bCryptPasswordEncoder,
                     PrivilegeRepository privilegeRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.privilegeRepository = privilegeRepository;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        Privilege readPrivilege = new Privilege("READ_PRIVILEGE");
        Privilege writePrivilege = new Privilege("WRITE_PRIVILEGE");

        privilegeRepository.save(writePrivilege);
        privilegeRepository.save(readPrivilege);

        List<Privilege> adminPrivileges = Arrays.asList(readPrivilege, writePrivilege);
        List<Privilege> userPrivileges = Arrays.asList(readPrivilege);

        Role userRole = new Role("ROLE_USER");
        userRole.setPrivileges(userPrivileges);

        Role adminRole = new Role("ROLE_ADMIN");
        adminRole.setPrivileges(adminPrivileges);

        roleRepository.save(adminRole);
        roleRepository.save(userRole);

        log.info("####added RoleUser {}", userRole.toString());
        log.info("####added RoleAdmin {}", adminRole.toString());
        log.info("####added ReadPrivilege {}", readPrivilege.toString());
        log.info("####added WritePrivilege {}", writePrivilege.toString());

        User testUser = new User();
        testUser.setId(1L);
        testUser.setLogin("test");
        testUser.setFirstName("użytkownik");
        testUser.setLastName("testowy");
        testUser.setEmail("test@test");
        testUser.setEnabled(Boolean.TRUE);
        testUser.setRoles(Arrays.asList(roleRepository.findByName("ROLE_USER")));
        testUser.setPassword(bCryptPasswordEncoder.encode("test"));
        userRepository.save(testUser);

        log.info("####added testUser login: {} password {} granted roles contain USER_ROLE : {}", testUser.getLogin(), testUser.getPassword(),
                testUser.getRoles().contains(roleRepository.findByName("ROLE_USER")));
    }
}
