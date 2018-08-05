package com.adammendak.authentication.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:application.properties")
@ConfigurationProperties
@Getter
@Setter
public class ConfigHelper {

    @Value("${jwt.header_string}")
    public String HEADER_STRING;
}
