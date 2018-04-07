package com.thexaxo.seenit.controllers;

import com.thexaxo.seenit.entities.Subseenit;
import com.thexaxo.seenit.entities.User;
import com.thexaxo.seenit.exceptions.SubseenitNotFoundException;
import com.thexaxo.seenit.models.CreateSubseenitBindingModel;
import com.thexaxo.seenit.services.PostService;
import com.thexaxo.seenit.services.SubseenitService;
import com.thexaxo.seenit.services.UserService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
import java.util.List;

@Controller
public class SubseenitController {
    private SubseenitService subseenitService;
    private UserService userService;
    private PostService postService;

    public SubseenitController(SubseenitService subseenitService, UserService userService, PostService postService) {
        this.subseenitService = subseenitService;
        this.userService = userService;
        this.postService = postService;
    }

    @GetMapping("/")
    public ModelAndView index(ModelAndView modelAndView) {
        modelAndView.setViewName("redirect:/s/all");

        return modelAndView;
    }

    @GetMapping("/s/{name}")
    public ModelAndView subseenit(ModelAndView modelAndView, @PathVariable String name, Principal principal, @PageableDefault(size = 10) Pageable pageable) {
        User user;

        if (name.equals("all")) {
            modelAndView.addObject("subseenitName", "all");
            modelAndView.addObject("totalPages", this.postService.getAllPostsPagesCount(pageable.getPageSize()));
        } else {
            Subseenit subseenit = this.subseenitService.findOneSubseenitByName(name);

            if (subseenit == null) {
                throw new SubseenitNotFoundException();
            }

            if (principal != null) {
                user = this.userService.getUserByUsername(principal.getName());
                modelAndView.addObject("isSubscribed", user.isSubscribedTo(subseenit.getName()));
            }

            modelAndView.addObject("subseenitName", subseenit.getName());
            modelAndView.addObject("totalPages", this.subseenitService.getPostsPagesCount(subseenit, pageable.getPageSize()));
        }

        modelAndView.addObject("currentPage", pageable.getPageNumber());
        modelAndView.addObject("view", "subseenit/subseenit :: subseenit");
        modelAndView.setViewName("base-layout");

        return modelAndView;
    }

    @GetMapping("/subseenits/create")
    public ModelAndView create(ModelAndView modelAndView, @ModelAttribute("subseenit") CreateSubseenitBindingModel bindingModel) {
        modelAndView.addObject("view", "subseenit/create :: create");
        modelAndView.setViewName("base-layout");

        return modelAndView;
    }

    @PostMapping("/subseenits/create")
    public ModelAndView createConfirm(ModelAndView modelAndView, @Valid @ModelAttribute("subseenit") CreateSubseenitBindingModel bindingModel, BindingResult bindingResult, Principal principal) {
        modelAndView.addObject("view", "subseenit/create :: create");
        modelAndView.setViewName("base-layout");

        if (bindingResult.hasErrors()) {
            return modelAndView;
        }

        if (bindingModel.getName().equals("all") || bindingModel.getName().equals("front")) {
            modelAndView.addObject("alreadyExists", true);

            return modelAndView;
        }

        User loggedUser = this.userService.getUserByUsername(principal.getName());
        boolean created = this.subseenitService.create(bindingModel, loggedUser);

        if (!created) {
            modelAndView.addObject("alreadyExists", true);

            return modelAndView;
        }

        modelAndView.clear();
        modelAndView.setViewName("redirect:/s/" + bindingModel.getName());

        return modelAndView;
    }

    @GetMapping("/subscribe/{subseenitName}")
    public ModelAndView subscribe(ModelAndView modelAndView, @PathVariable String subseenitName, Principal principal) {
        Subseenit subseenit = this.subseenitService.findOneSubseenitByName(subseenitName);

        if (subseenit == null) {
            throw new SubseenitNotFoundException();
        }

        User loggedUser = this.userService.getUserByUsername(principal.getName());
        this.userService.subscribe(subseenit, loggedUser);

        modelAndView.setViewName("redirect:/s/" + subseenitName);
        return modelAndView;
    }

    @GetMapping("/subseenits/subscribed")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView getSubscribedSubseenits(ModelAndView modelAndView, Principal principal) {
        User user = this.userService.getUserByUsername(principal.getName());
        List<Subseenit> subscribedSubseenits = user.getSubscribedSubseenits();

        modelAndView.addObject("subseenits", subscribedSubseenits);
        modelAndView.setViewName("subseenit/subscribed");

        return modelAndView;
    }
}