package com.thexaxo.seenit.controllers;

import com.thexaxo.seenit.entities.Subseenit;
import com.thexaxo.seenit.entities.User;
import com.thexaxo.seenit.models.CreateSubseenitBindingModel;
import com.thexaxo.seenit.services.SubseenitService;
import com.thexaxo.seenit.services.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.security.Principal;

@Controller
public class SubseenitController {
    private SubseenitService subseenitService;
    private UserService userService;

    public SubseenitController(SubseenitService subseenitService, UserService userService) {
        this.subseenitService = subseenitService;
        this.userService = userService;
    }

    @GetMapping("/s/{name}")
    @PreAuthorize("permitAll()")
    public ModelAndView subseenit(ModelAndView modelAndView, @PathVariable String name) {
        Subseenit subseenit = this.subseenitService.findOneSubseenitByName(name);

        modelAndView.addObject("subseenit", subseenit);
        modelAndView.addObject("view", "subseenit/subseenit :: subseenit");
        modelAndView.setViewName("base-layout");

        return modelAndView;
    }

    @GetMapping("/subseenits/create")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView create(ModelAndView modelAndView, @ModelAttribute("subseenit") CreateSubseenitBindingModel bindingModel) {
        modelAndView.addObject("view", "subseenit/create :: create");
        modelAndView.setViewName("base-layout");

        return modelAndView;
    }

    @PostMapping("/subseenits/create")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView createConfirm(ModelAndView modelAndView, @Valid @ModelAttribute("subseenit") CreateSubseenitBindingModel bindingModel, BindingResult bindingResult, Principal principal) {
        modelAndView.addObject("view", "subseenit/create :: create");

        if (bindingResult.hasErrors()) {
            return modelAndView;
        }

        User loggedUser = this.userService.getUserByUsername(principal.getName());
        boolean created = this.subseenitService.create(bindingModel, loggedUser);

        if (!created) {
            modelAndView.addObject("alreadyExists", true);
            modelAndView.setViewName("base-layout");

            return modelAndView;
        }

        modelAndView.clear();
        modelAndView.setViewName("redirect:/s/" + bindingModel.getName());

        return modelAndView;
    }
}