package com.adammendak.authentication.controller;

import org.springframework.web.bind.annotation.CrossOrigin;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@CrossOrigin(origins = {"http://localhost:4200", "http://login.adammendak.pl/", "http://localhost:3000"})
public @interface CorsFilter {
}
