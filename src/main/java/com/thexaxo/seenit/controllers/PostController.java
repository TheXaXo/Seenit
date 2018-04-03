package com.thexaxo.seenit.controllers;

import com.thexaxo.seenit.entities.Post;
import com.thexaxo.seenit.entities.Subseenit;
import com.thexaxo.seenit.entities.User;
import com.thexaxo.seenit.models.SubmitLinkBindingModel;
import com.thexaxo.seenit.models.SubmitTextPostBindingModel;
import com.thexaxo.seenit.services.PostService;
import com.thexaxo.seenit.services.SubseenitService;
import com.thexaxo.seenit.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
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

        Post post = this.postService.createTextPost(bindingModel, userService.getUserByUsername(principal.getName()), subseenit);

        modelAndView.clear();
        modelAndView.setViewName("redirect:/s/" + subseenit.getName() + "/comments/" + post.getId());

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

        Post post = this.postService.createLinkPost(bindingModel, userService.getUserByUsername(principal.getName()), subseenit);

        modelAndView.clear();
        modelAndView.setViewName("redirect:/s/" + subseenit.getName() + "/comments/" + post.getId());

        return modelAndView;
    }

    @GetMapping("/upvote/{postId}")
    public ResponseEntity upvote(@PathVariable String postId, Principal principal) {
        this.postService.upvote(postId, userService.getUserByUsername(principal.getName()));

        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/downvote/{postId}")
    public ResponseEntity downvote(@PathVariable String postId, Principal principal) {
        this.postService.downvote(postId, userService.getUserByUsername(principal.getName()));

        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/s/{subseenitName}/comments/{postId}")
    public ModelAndView postPage(@PathVariable String subseenitName, @PathVariable String postId, ModelAndView modelAndView, Principal principal) {
        Subseenit subseenit = this.subseenitService.findOneSubseenitByName(subseenitName);
        Post post = this.postService.findById(postId);

        if (subseenit == null || post == null) {
            //TODO add exceptions
            return modelAndView;
        }

        if (principal != null) {
            User user = this.userService.getUserByUsername(principal.getName());

            this.postService.populateUpvotedDownvotedFields(post, user);
            modelAndView.addObject("isSubscribed", user.isSubscribedTo(subseenit.getName()));
        }

        modelAndView.addObject("post", post);
        modelAndView.addObject("view", "post/post-page :: post-page");
        modelAndView.addObject("subseenitName", subseenit.getName());
        modelAndView.setViewName("base-layout");

        return modelAndView;
    }
}