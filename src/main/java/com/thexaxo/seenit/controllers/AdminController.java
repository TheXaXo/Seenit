package com.thexaxo.seenit.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@PreAuthorize("hasAnyRole('ADMIN', 'GOD')")
public class AdminController {
    @GetMapping("/admin/user-panel")
    public ModelAndView usersPanel(ModelAndView modelAndView) {
        modelAndView.addObject("view", "admin/user-panel :: user-panel");
        modelAndView.setViewName("base-layout");

        return modelAndView;
    }
}