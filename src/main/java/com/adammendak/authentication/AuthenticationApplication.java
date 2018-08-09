package com.adammendak.authentication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication
public class AuthenticationApplication {

    //todo sprawdzic na froncie czy odpowiedni token w headerach authorization czy authentication leci
    //todo custom exception handler
    // todo jwt token utils implement
    //todo zrobic exception mappinga jakiegos generycznego
    //todo zrobic enumy z role i privilegesow
    //todo implement token expired in user
    //todo WERSJA 2.0 z rsa public key tokenem
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedMethods("HEAD", "OPTIONS", "GET", "PUT", "POST", "DELETE", "PATCH")
                        .exposedHeaders("Authentication");
            }
        };
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigInDev() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public static void main(String[] args) {
        SpringApplication.run(AuthenticationApplication.class, args);


    }
}
