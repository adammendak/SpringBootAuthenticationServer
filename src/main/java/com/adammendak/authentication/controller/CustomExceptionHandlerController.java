package com.adammendak.authentication.controller;

import com.adammendak.authentication.model.dto.ExceptionResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.ServletException;
import java.util.Date;

@Slf4j
@ControllerAdvice
public class CustomExceptionHandlerController extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGenericException(Exception ex) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createGenericExceptionResponse(ex));
    }

    @ExceptionHandler(ServletException.class)
    public ResponseEntity<Object> handleServletException(ServletException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createGenericExceptionResponse(ex));
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Object> handleUserNotFoundException(Exception ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createGenericExceptionResponse(ex));
    }

//    @ExceptionHandler(NoUserInDBException.class)
//    public ResponseEntity<Object> handleNoUserInDBException(Exception ex) {
//
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(createGenericExceptionResponse(ex));
//    }

    private ExceptionResponseDto createGenericExceptionResponse(Exception ex) {
        log.info("#### Exception controller advice");
        return new ExceptionResponseDto(new Date(), ex.getMessage(), ex.getMessage());
    }

}
