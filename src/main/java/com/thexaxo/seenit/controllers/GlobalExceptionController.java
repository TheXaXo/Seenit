package com.thexaxo.seenit.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class GlobalExceptionController {
    private static final String DEFAULT_ERROR_MESSAGE = "There was an error with your request.";

    @ExceptionHandler(RuntimeException.class)
    public ModelAndView getException(RuntimeException e, HttpServletResponse response) {
        String errorMessage;

        if (e.getClass().isAnnotationPresent(ResponseStatus.class)) {
            errorMessage = e.getClass().getAnnotation(ResponseStatus.class).reason();
            response.setStatus(e.getClass().getAnnotation(ResponseStatus.class).code().value());
        } else {
            errorMessage = DEFAULT_ERROR_MESSAGE;
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        ModelAndView modelAndView = new ModelAndView();

        modelAndView.addObject("error", errorMessage);
        modelAndView.addObject("view", "error-template :: error");
        modelAndView.setViewName("base-layout");

        return modelAndView;
    }
}