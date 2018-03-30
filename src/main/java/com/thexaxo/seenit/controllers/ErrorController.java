package com.thexaxo.seenit.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ErrorController {
    @GetMapping("/unauthorized")
    public ModelAndView unauthorized(ModelAndView modelAndView) {
        modelAndView.addObject("view", "unauthorized :: unauthorized");
        modelAndView.setViewName("base-layout");

        return modelAndView;
    }
}