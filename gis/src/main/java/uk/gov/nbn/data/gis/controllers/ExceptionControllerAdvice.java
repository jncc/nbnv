package uk.gov.nbn.data.gis.controllers;

import org.hibernate.validator.method.MethodConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Christopher Johnson
 */
@ControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(MethodConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView handleMethodConstraintViolationException(MethodConstraintViolationException exception) {
        return new ModelAndView("invalid-request", "exception", exception);
    }
    
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public String handleMethodConstraintViolationException(IllegalArgumentException ex) {
        return ex.getMessage();
    }
}
