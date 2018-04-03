package com.thexaxo.seenit.controllers;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionController {
    private static final String DEFAULT_ERROR_MESSAGE = "There was an error with your request.";

    @ExceptionHandler(RuntimeException.class)
    public ModelAndView getException(RuntimeException e) {
        String errorMessage =
                e.getClass().isAnnotationPresent(ResponseStatus.class)
                        ? e.getClass().getAnnotation(ResponseStatus.class).reason()
                        : DEFAULT_ERROR_MESSAGE;

        ModelAndView modelAndView = new ModelAndView();

        modelAndView.addObject("error", errorMessage);
        modelAndView.addObject("view", "error-template :: error");
        modelAndView.setViewName("base-layout");

        return modelAndView;
    }
}