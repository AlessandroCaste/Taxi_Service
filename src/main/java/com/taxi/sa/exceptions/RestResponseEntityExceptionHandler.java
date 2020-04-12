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

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseBody
    ResponseEntity<JsonResponse> handleHttpMediaTypeNotSupportedException(HttpServletRequest request, Throwable ex) {
        return new ResponseEntity<>(new JsonResponse("json data required", 415), HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    ResponseEntity<JsonResponse> handleHttpMessageNotReadableException(HttpServletRequest request, Throwable ex) {
        return new ResponseEntity<>(new JsonResponse("malformed submission",400),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CityMapParsingException.class)
    @ResponseBody
    ResponseEntity<JsonResponse> handleCityMapParsingException(Throwable ex) {
        return new ResponseEntity<>(new JsonResponse("invalid map data",400),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TaxiValidationException.class)
    @ResponseBody
    ResponseEntity<JsonResponse> handleTaxiValidationException(Throwable ex) {
        return new ResponseEntity<>(new JsonResponse("invalid taxi submission",400), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserRequestException.class)
    @ResponseBody
    ResponseEntity<JsonResponse> handleUserRequestException(Throwable ex) {
        return new ResponseEntity<>(new JsonResponse("invalid user request", 400),HttpStatus.BAD_REQUEST);
    }

    // A bit raw perhaps, but well suited to hiding internal logic to the external user
    @ExceptionHandler(Exception.class)
    @ResponseBody
    ResponseEntity<JsonResponse> handleGenericException(HttpServletRequest request, Throwable ex) {
        return new ResponseEntity<>(new JsonResponse("internal error",500),HttpStatus.INTERNAL_SERVER_ERROR);
    }

}