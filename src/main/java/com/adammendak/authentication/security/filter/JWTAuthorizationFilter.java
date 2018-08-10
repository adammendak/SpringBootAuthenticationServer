package com.adammendak.authentication.security.filter;

import com.adammendak.authentication.model.User;
import com.adammendak.authentication.repository.UserRepository;
import com.adammendak.authentication.security.SecurityUtil;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Slf4j
public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    private SecurityUtil securityUtil;
    private UserRepository userRepository;

    private String HEADER_STRING;
    private String TOKEN_PREFIX;
    private String SECRET;



    public JWTAuthorizationFilter(AuthenticationManager authenticationManager, SecurityUtil securityUtil,
                                  UserRepository userRepository) {
        super(authenticationManager);
        this.securityUtil = securityUtil;
        this.userRepository = userRepository;
        this.HEADER_STRING = securityUtil.getHEADER_STRING();
        this.TOKEN_PREFIX = securityUtil.getTOKEN_PREFIX();
        this.SECRET = securityUtil.getSECRET();
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
                Optional<User> userInDB = userRepository.findByLogin(user);
                return new UsernamePasswordAuthenticationToken(user, userInDB.get(), securityUtil.getAuthorities(userInDB.get().getRoles()) );
            }
            return null;
        }
        return null;
    }
}
