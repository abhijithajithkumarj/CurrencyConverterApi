package com.currencyexchange.currency_converter.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Map;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    private final int errorCode;

    private String message;

    private Map<String, String> messageList;

    ErrorResponse(HttpStatus errorCode, String message){
        this.errorCode = errorCode.value();
        this.message = message;
    }

    ErrorResponse(HttpStatus errorCode,Map<String, String> messageList){
        this.errorCode = errorCode.value();
        this.messageList = messageList;
    }
}