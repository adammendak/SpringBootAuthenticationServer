package com.adammendak.authentication.security;

import com.adammendak.authentication.repository.UserRepository;
import com.adammendak.authentication.security.filters.JWTAuthenticationFilter;
import com.adammendak.authentication.security.filters.JWTAuthorizationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private UserDetailsService userDetailsService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private SecurityUtil securityUtil;
    private UserRepository userRepository;
    private CustomAccessDeniedHandler customAccessDeniedHandler;

    private String LOGIN_URL;
    private String CREATE_NEW_USER_URL;

    public WebSecurityConfig(UserDetailServiceImpl userDetailsService, BCryptPasswordEncoder bCryptPasswordEncoder
        , SecurityUtil securityUtil, UserRepository userRepository, CustomAccessDeniedHandler customAccessDeniedHandler) {
        this.userDetailsService = userDetailsService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.securityUtil = securityUtil;
        this.userRepository = userRepository;
        this.customAccessDeniedHandler = customAccessDeniedHandler;
        this.LOGIN_URL = securityUtil.getLOGIN_URL();
        this.CREATE_NEW_USER_URL = securityUtil.getCREATE_NEW_USER_URL();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }



    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .cors().and()
            .csrf().disable()
            .authorizeRequests()
            .antMatchers("/h2-console/**").permitAll()
            .antMatchers("/").permitAll()
            .antMatchers("/actuator/health").permitAll()
            .antMatchers("/actuator/info").permitAll()
            .antMatchers(HttpMethod.POST, LOGIN_URL).permitAll()
            .antMatchers(HttpMethod.POST, CREATE_NEW_USER_URL).permitAll()
            .anyRequest().authenticated()
            .and()
            .addFilterBefore( new JWTAuthenticationFilter(LOGIN_URL , authenticationManager(),
                    securityUtil, userRepository), UsernamePasswordAuthenticationFilter.class)
            .addFilter(new JWTAuthorizationFilter(authenticationManager(), securityUtil, userRepository))
                //potrzebny do przegladania h2-console
            .headers().frameOptions().disable()
            .and()
            .exceptionHandling().accessDeniedHandler(customAccessDeniedHandler);
        ;
    }
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }
}
