package com.thexaxo.seenit.controllers;

import com.thexaxo.seenit.entities.Subseenit;
import com.thexaxo.seenit.models.SubmitLinkBindingModel;
import com.thexaxo.seenit.models.SubmitTextPostBindingModel;
import com.thexaxo.seenit.services.PostService;
import com.thexaxo.seenit.services.SubseenitService;
import com.thexaxo.seenit.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
public class PostController {
    private PostService postService;
    private UserService userService;
    private SubseenitService subseenitService;

    @Autowired
    public PostController(PostService postService, UserService userService, SubseenitService subseenitService) {
        this.postService = postService;
        this.userService = userService;
        this.subseenitService = subseenitService;
    }

    @GetMapping("/submit/text")
    public ModelAndView submitText(ModelAndView modelAndView, @ModelAttribute("post") SubmitTextPostBindingModel bindingModel, Principal principal) {
        List<Subseenit> subscribedSubseenits = this.userService.getUserByUsername(principal.getName()).getSubscribedSubseenits();

        modelAndView.addObject("subscribedSubseenits", subscribedSubseenits);
        modelAndView.addObject("view", "post/submit-text :: submit-text");
        modelAndView.setViewName("base-layout");

        return modelAndView;
    }

    @GetMapping("/submit/link")
    public ModelAndView submitLink(ModelAndView modelAndView, @ModelAttribute("post") SubmitLinkBindingModel bindingModel, Principal principal) {
        List<Subseenit> subscribedSubseenits = this.userService.getUserByUsername(principal.getName()).getSubscribedSubseenits();

        modelAndView.addObject("subscribedSubseenits", subscribedSubseenits);
        modelAndView.addObject("view", "post/submit-link :: submit-link");
        modelAndView.setViewName("base-layout");

        return modelAndView;
    }

    @PostMapping("/submit/text")
    public ModelAndView submitTextConfirm(ModelAndView modelAndView, @Valid @ModelAttribute("post") SubmitTextPostBindingModel bindingModel, BindingResult bindingResult, Principal principal) {
        List<Subseenit> subscribedSubseenits = this.userService.getUserByUsername(principal.getName()).getSubscribedSubseenits();

        modelAndView.addObject("subscribedSubseenits", subscribedSubseenits);
        modelAndView.addObject("view", "post/submit-text :: submit-text");
        modelAndView.setViewName("base-layout");

        if (bindingResult.hasErrors()) {
            return modelAndView;
        }

        Subseenit subseenit = this.subseenitService.findOneSubseenitByName(bindingModel.getSubseenit());

        if (subseenit == null) {
            return modelAndView;
        }

        this.postService.createTextPost(bindingModel, userService.getUserByUsername(principal.getName()), subseenit);

        modelAndView.clear();
        //TODO redirect to post instead of index
        modelAndView.setViewName("redirect:/");

        return modelAndView;
    }

    @PostMapping("/submit/link")
    public ModelAndView submitLinkConfirm(ModelAndView modelAndView, @Valid @ModelAttribute("post") SubmitLinkBindingModel bindingModel, BindingResult bindingResult, Principal principal) {
        List<Subseenit> subscribedSubseenits = this.userService.getUserByUsername(principal.getName()).getSubscribedSubseenits();

        modelAndView.addObject("subscribedSubseenits", subscribedSubseenits);
        modelAndView.addObject("view", "post/submit-link :: submit-link");
        modelAndView.setViewName("base-layout");

        if (bindingResult.hasErrors()) {
            return modelAndView;
        }

        Subseenit subseenit = this.subseenitService.findOneSubseenitByName(bindingModel.getSubseenit());

        if (subseenit == null) {
            return modelAndView;
        }

        this.postService.createLinkPost(bindingModel, userService.getUserByUsername(principal.getName()), subseenit);

        modelAndView.clear();
        //TODO redirect to post instead of index
        modelAndView.setViewName("redirect:/");

        return modelAndView;
    }
}