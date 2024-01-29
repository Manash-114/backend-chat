package com.whatsapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalException {


    @ExceptionHandler(UserException.class)
    public ResponseEntity<ErrorDetails> handleUserException(UserException e , WebRequest request){
        ErrorDetails errorDetails = new ErrorDetails(e.getMessage(),request.getDescription(false), LocalDateTime.now());
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }


    //this exception handler must be last
    @ExceptionHandler(MessageException.class)
    public ResponseEntity<ErrorDetails> messageExceptionHandler(MessageException e , WebRequest request){
        ErrorDetails errorDetails = new ErrorDetails(e.getMessage(),request.getDescription(false), LocalDateTime.now());
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoEndPointExcepton.class)
    public ResponseEntity<ErrorDetails> noEndpointHandlerException(NoEndPointExcepton e , WebRequest request){
        ErrorDetails errorDetails = new ErrorDetails("End Point not found",e.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }
}
