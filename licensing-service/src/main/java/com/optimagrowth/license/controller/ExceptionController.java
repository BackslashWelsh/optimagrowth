/**
 *
 */
package com.optimagrowth.license.controller;

import com.optimagrowth.license.model.utils.ErrorMessage;
import com.optimagrowth.license.model.utils.ResponseWrapper;
import com.optimagrowth.license.model.utils.RestErrorList;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;

import static java.util.Collections.singletonMap;

@RestControllerAdvice
@EnableWebMvc
public class ExceptionController extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseWrapper handleException(
            HttpServletRequest request,
            ResponseWrapper responseWrapper
    ) {

        return responseWrapper;
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseWrapper handleIOException(HttpServletRequest request, RuntimeException e) {

        var errorList = new RestErrorList(HttpStatus.NOT_ACCEPTABLE, new ErrorMessage(e.getMessage(),
                e.getMessage()));

        return new ResponseWrapper(null, singletonMap("status", HttpStatus.NOT_ACCEPTABLE)
                , errorList);
    }
}