package com.taxi.sa.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class RestResponseEntityExceptionHandler {

    //TODO better message formatting
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseBody
    ResponseEntity<String> handleHttpMediaTypeNotSupportedException(HttpServletRequest request, Throwable ex) {
        return new ResponseEntity<>("json data required", HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    ResponseEntity<String> handleHttpMessageNotReadableException(HttpServletRequest request, Throwable ex) {
        return new ResponseEntity<>("malformed submission", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CityMapParsingException.class)
    @ResponseBody
    ResponseEntity<String> handleCityMapParsingException(Throwable ex) {
        return new ResponseEntity<>("invalid map data",HttpStatus.BAD_REQUEST);
    }

   /* @ExceptionHandler(Exception.class)
    @ResponseBody
    ResponseEntity<String> handleGenericException(HttpServletRequest request, Throwable ex) {
        return new ResponseEntity<>("internal error",HttpStatus.INTERNAL_SERVER_ERROR);
    }*/



}