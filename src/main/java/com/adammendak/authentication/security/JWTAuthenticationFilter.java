package com.adammendak.authentication.security;

import com.adammendak.authentication.model.User;
import com.adammendak.authentication.repository.UserRepository;
import com.adammendak.authentication.service.SecurityService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@PropertySource("classpath:application.properties")
@ConfigurationProperties
public class JWTAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    @Value("${jwt.expiration_time}")
    private Long EXPIRATION_TIME = 864000000L;

    @Value("${jwt.secret}")
    private String SECRET = "verySecretSecret";

    @Value("${jwt.header_string}")
    private String HEADER_STRING = "Authentication";

    @Value("${jwt.token_prefix}")
    private String TOKEN_PREFIX = "Bearer ";

    private AuthenticationManager authenticationManager;
    private SecurityService securityService;
    private UserRepository userRepository;

    public JWTAuthenticationFilter(String defaultFilterProcessesUrl, AuthenticationManager authenticationManager
        ,SecurityService securityService, UserRepository userRepository) {
        super(defaultFilterProcessesUrl);
        this.authenticationManager = authenticationManager;
        this.securityService = securityService;
        this.userRepository = userRepository;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        try {
            User credentials = new ObjectMapper()
                    .readValue(request.getInputStream(), User.class);
            Optional<User> userFromReq = userRepository.findByLogin(credentials.getLogin());
            if(!userFromReq.isPresent()) {
                throw new Error("no such user in DB");
            }

            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            credentials.getLogin(),
                            credentials.getPassword(),
                            securityService.getAuthorities(userFromReq.get().getRoles()))
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {
        String token = Jwts.builder()
                .setSubject(((org.springframework.security.core.userdetails.User) auth.getPrincipal()).getUsername())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
        res.addHeader(HEADER_STRING, TOKEN_PREFIX + token);
    }
}
