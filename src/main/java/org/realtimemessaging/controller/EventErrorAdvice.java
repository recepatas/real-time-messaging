package org.realtimemessaging.controller;

import org.realtimemessaging.dto.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.JmsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class EventErrorAdvice {

    private Logger logger = LoggerFactory.getLogger(EventErrorAdvice.class);

    @ExceptionHandler(JmsException.class)
    public ResponseEntity<ApiResponse> jmsErrorHandler(JmsException ex) {
        logger.error("Event publishing is failed, JMS Exception occured" + ex.getErrorCode() + ":" + ex.getMessage());
        ApiResponse errorResponse = new ApiResponse("Event publishing is failed, JMS Exception occured");
        return new ResponseEntity<>(errorResponse, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> messages = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach(error -> messages.add(error.getDefaultMessage()));
        return new ResponseEntity<>(new ApiResponse(String.join("\n", messages)), HttpStatus.BAD_REQUEST);
    }

}
