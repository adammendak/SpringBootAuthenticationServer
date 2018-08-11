package com.adammendak.authentication.security;

import com.adammendak.authentication.model.Role;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@Data
@Slf4j
public class SecurityUtil {

    @Value("${jwt.header_string}")
    private String HEADER_STRING;

    @Value("${jwt.token_prefix}")
    private String TOKEN_PREFIX;

    @Value("${jwt.secret}")
    private String SECRET;

    @Value("${jwt.expiration_time}")
    private Long EXPIRATION_TIME;

    @Value("${jwt.login_url}")
    private String LOGIN_URL = "/auth/login";

    @Value("${jwt.create_new_user_url}")
    private String CREATE_NEW_USER_URL = "/api/user";

    public Collection<? extends GrantedAuthority> getAuthorities(
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
