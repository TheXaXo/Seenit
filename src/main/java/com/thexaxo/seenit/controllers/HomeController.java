package com.thexaxo.seenit.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {
    @GetMapping("/")
    public ModelAndView home(ModelAndView modelAndView) {
        modelAndView.addObject("view", "home/index :: index");
        modelAndView.setViewName("base-layout");

        return modelAndView;
    }

    @GetMapping("/unauthorized")
    public ModelAndView unauthorized(ModelAndView modelAndView) {
        modelAndView.addObject("view", "unauthorized :: unauthorized");
        modelAndView.setViewName("base-layout");

        return modelAndView;
    }
}