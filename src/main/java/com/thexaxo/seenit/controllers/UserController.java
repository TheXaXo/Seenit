package com.thexaxo.seenit.controllers;

import com.thexaxo.seenit.models.LoginUserBindingModel;
import com.thexaxo.seenit.models.RegisterUserBindingModel;
import com.thexaxo.seenit.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    @PreAuthorize("isAnonymous()")
    public ModelAndView register(ModelAndView modelAndView, @ModelAttribute("user") RegisterUserBindingModel bindingModel) {
        modelAndView.addObject("view", "user/register :: register");
        modelAndView.setViewName("base-layout");

        return modelAndView;
    }

    @PostMapping("/register")
    @PreAuthorize("isAnonymous()")
    public ModelAndView registerConfirm(ModelAndView modelAndView, @Valid @ModelAttribute("user") RegisterUserBindingModel bindingModel, BindingResult bindingResult) {
        modelAndView.addObject("view", "user/register :: register");
        modelAndView.setViewName("base-layout");

        if (bindingResult.hasErrors()) {
            return modelAndView;
        }

        if (!bindingModel.getPassword().equals(bindingModel.getConfirmPassword())) {
            modelAndView.addObject("passwordsDoNotMatch", true);
            return modelAndView;
        }

        if (this.userService.userWithUsernameExists(bindingModel.getUsername())) {
            modelAndView.addObject("usernameTaken", true);
            return modelAndView;
        }

        if (this.userService.userWithEmailExists(bindingModel.getEmail())) {
            modelAndView.addObject("emailTaken", true);
            return modelAndView;
        }

        this.userService.register(bindingModel);
        modelAndView.clear();
        modelAndView.setViewName("redirect:/login");

        return modelAndView;
    }

    @GetMapping("/login")
    @PreAuthorize("isAnonymous()")
    public ModelAndView login(ModelAndView modelAndView, @ModelAttribute("user") LoginUserBindingModel bindingModel, @RequestParam(required = false, name = "error") String error) {
        if (error != null) {
            modelAndView.addObject("error", error);
        }

        modelAndView.addObject("view", "user/login :: login");
        modelAndView.setViewName("base-layout");
        return modelAndView;
    }
}