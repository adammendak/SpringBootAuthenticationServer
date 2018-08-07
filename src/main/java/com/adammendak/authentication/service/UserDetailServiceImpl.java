package com.adammendak.authentication.service;

import com.adammendak.authentication.model.Role;
import com.adammendak.authentication.model.User;
import com.adammendak.authentication.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
    private final SecurityService securityService;

    public UserDetailServiceImpl(UserRepository userRepository, SecurityService securityService) {
        this.userRepository = userRepository;
        this.securityService = securityService;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByLogin(login);
        if( user.isPresent()) {
            return new org.springframework.security.core.userdetails.User (user.get().getLogin(),
//todo tutaj jest to przekombinowane
//                    user.get().getPassword(), securityService.getAuthorities(user.get().getRoles()));
                    user.get().getPassword(), getAuthorities(user.get().getRoles()));
        } else {
            throw new UsernameNotFoundException(login);
        }

    }

    private Collection<? extends GrantedAuthority> getAuthorities(
            Collection<Role> roles) {
        List<GrantedAuthority> authorities
                = new ArrayList<>();
        for (Role role: roles) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
            role.getPrivileges().stream()
                    .map(p -> new SimpleGrantedAuthority(p.getName()))
                    .forEach(authorities::add);
        }

        return authorities;
    }

}

