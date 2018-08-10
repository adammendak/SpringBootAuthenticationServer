package com.adammendak.authentication.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class ExceptionResponseDto {

    @JsonProperty(value = "time_stamp")
    private Date timeStamp;
    private String message;
    private String details;
}
