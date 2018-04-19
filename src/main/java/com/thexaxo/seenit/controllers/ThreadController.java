package com.thexaxo.seenit.controllers;

import com.thexaxo.seenit.entities.Thread;
import com.thexaxo.seenit.entities.User;
import com.thexaxo.seenit.services.ThreadService;
import com.thexaxo.seenit.services.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.List;

@Controller
public class ThreadController {
    private UserService userService;
    private ThreadService threadService;

    public ThreadController(UserService userService, ThreadService threadService) {
        this.userService = userService;
        this.threadService = threadService;
    }

    @GetMapping("/threads/all")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView getAllThreadsOfUser(ModelAndView modelAndView, Principal principal) {
        User user = this.userService.getUserByUsername(principal.getName());
        List<Thread> threads = this.threadService.getAllThreadsOfUser(user);

        modelAndView.addObject("threads", threads);
        modelAndView.setViewName("message/threads");

        return modelAndView;
    }
}