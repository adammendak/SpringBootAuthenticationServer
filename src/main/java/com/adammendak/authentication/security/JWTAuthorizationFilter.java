package com.adammendak.authentication.security;

import com.adammendak.authentication.model.Role;
import com.adammendak.authentication.model.User;
import com.adammendak.authentication.repository.UserRepository;
import com.adammendak.authentication.service.SecurityService;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@PropertySource("classpath:application.properties")
@ConfigurationProperties
@Slf4j
public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    @Value("${jwt.header_string}")
    private String HEADER_STRING = "Authorization";

    @Value("${jwt.token_prefix}")
    private String TOKEN_PREFIX = "Bearer";

    @Value("${jwt.secret}")
    private String SECRET = "verySecretSecret" ;

//    private JwtTokenUtil jwtTokenUtil;
    private SecurityService securityService;
    private UserRepository userRepository;

    public JWTAuthorizationFilter(AuthenticationManager authenticationManager, SecurityService securityService,
                                  UserRepository userRepository) {
        super(authenticationManager);
        this.securityService = securityService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws IOException, ServletException {
        String header = req.getHeader(HEADER_STRING);
        if (header == null || !header.startsWith(TOKEN_PREFIX)) {
            chain.doFilter(req, res);
            return;
        }
        UsernamePasswordAuthenticationToken authentication = getAuthentication(req);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(req, res);
    }
    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(HEADER_STRING);
        if (token != null) {
            String user = Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                    .getBody()
                    .getSubject();
            if (user != null) {
                //todo tutaj trzeba USTAWIC !!!
                Optional<User> user1 = userRepository.findByLogin(user);
//                user1.ifPresent(user2 -> log.info("###########UDALO SIE TO JEST USER {}", user2.getLogin()));
//                userRepository.findByLogin(user.)
//                Collection<GrantedAuthority> grantedAuthorities = securityService.getAuthorities(user)
//                Collection<GrantedAuthority> grantedAuthorities = securityService.getAuthorities(user1.get().getRoles());




                return new UsernamePasswordAuthenticationToken(user, user1.get(), getAuthorities(user1.get().getRoles()) );
            }
            return null;
        }
        return null;
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
