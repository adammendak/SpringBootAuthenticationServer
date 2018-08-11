package com.adammendak.authentication.model.exceptions;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

@Slf4j
@Data
public class NoUserInDBException extends Exception{
    @JsonProperty(value = "time_stamp")
    private Date timeStamp;
    private String message;
    private String details;
}
