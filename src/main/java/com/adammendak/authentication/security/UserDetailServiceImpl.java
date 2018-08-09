package com.adammendak.authentication.security;

import com.adammendak.authentication.model.User;
import com.adammendak.authentication.repository.UserRepository;
import com.adammendak.authentication.security.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Slf4j
@Transactional
public class UserDetailServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final SecurityUtil securityUtil;

    public UserDetailServiceImpl(UserRepository userRepository, SecurityUtil securityUtil) {
        this.userRepository = userRepository;
        this.securityUtil = securityUtil;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByLogin(login);
        if( user.isPresent()) {
            return new org.springframework.security.core.userdetails.User (user.get().getLogin(),
                    user.get().getPassword(), securityUtil.getAuthorities(user.get().getRoles()));
        } else {
            throw new UsernameNotFoundException(login);
        }

    }
}

