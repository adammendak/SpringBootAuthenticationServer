package com.adammendak.authentication.security.filter;

import com.adammendak.authentication.model.User;
import com.adammendak.authentication.repository.UserRepository;
import com.adammendak.authentication.security.SecurityUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@Slf4j
public class JWTAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private AuthenticationManager authenticationManager;
    private SecurityUtil securityUtil;
    private UserRepository userRepository;

    private Long EXPIRATION_TIME;
    private String SECRET;
    private String HEADER_STRING;
    private String TOKEN_PREFIX;

    public JWTAuthenticationFilter(String defaultFilterProcessesUrl, AuthenticationManager authenticationManager,
                                   SecurityUtil securityUtil, UserRepository userRepository) {
        super(defaultFilterProcessesUrl);
        this.EXPIRATION_TIME = securityUtil.getEXPIRATION_TIME();
        this.SECRET = securityUtil.getSECRET();
        this.HEADER_STRING = securityUtil.getHEADER_STRING();
        this.TOKEN_PREFIX = securityUtil.getTOKEN_PREFIX();
        this.authenticationManager = authenticationManager;
        this.securityUtil = securityUtil;
        this.userRepository = userRepository;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        try {
            User credentials = new ObjectMapper()
                    .readValue(request.getInputStream(), User.class);
            Optional<User> userFromReq = userRepository.findByLogin(credentials.getLogin());
            if (!userFromReq.isPresent()) {
                throw new UsernameNotFoundException("no such user in DB");
            }

            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            credentials.getLogin(),
                            credentials.getPassword(),
                            securityUtil.getAuthorities(userFromReq.get().getRoles()))
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new UsernameNotFoundException(e.getMessage());
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
        res.addHeader(HEADER_STRING, TOKEN_PREFIX + " " + token);
    }
}
