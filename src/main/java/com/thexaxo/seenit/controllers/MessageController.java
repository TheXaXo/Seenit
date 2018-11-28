package com.thexaxo.seenit.controllers;

import com.thexaxo.seenit.entities.Message;
import com.thexaxo.seenit.entities.Thread;
import com.thexaxo.seenit.entities.User;
import com.thexaxo.seenit.exceptions.ThreadNotFoundException;
import com.thexaxo.seenit.models.SendMessageBindingModel;
import com.thexaxo.seenit.services.MessageService;
import com.thexaxo.seenit.services.ThreadService;
import com.thexaxo.seenit.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
public class MessageController {
    private UserService userService;
    private ThreadService threadService;
    private MessageService messageService;

    @Autowired
    public MessageController(UserService userService, ThreadService threadService, MessageService messageService) {
        this.userService = userService;
        this.threadService = threadService;
        this.messageService = messageService;
    }

    @GetMapping("/messages")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView inbox(ModelAndView modelAndView, Principal principal) {
        modelAndView.addObject("username", this.userService.getUserByUsername(principal.getName()));
        modelAndView.addObject("view", "message/inbox :: inbox");
        modelAndView.setViewName("base-layout");

        if (this.threadService.isInboxEmpty(this.userService.getUserByUsername(principal.getName()))) {
            modelAndView.addObject("isInboxEmpty", "true");
        }

        return modelAndView;
    }

    @GetMapping("/u/{username}/message")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView newMessage(ModelAndView modelAndView, @PathVariable String username, @ModelAttribute("message") SendMessageBindingModel bindingModel, Principal principal) {
        if (username.equals(principal.getName()) || !this.userService.userWithUsernameExists(username)) {
            throw new IllegalArgumentException();
        }

        modelAndView.addObject("recipient", username);
        modelAndView.addObject("view", "message/send-message-from-profile :: send-message-from-profile");
        modelAndView.setViewName("base-layout");

        return modelAndView;
    }

    @PostMapping("/u/{username}/message")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView newMessageConfirm(ModelAndView modelAndView, @PathVariable String username, @Valid @ModelAttribute("message") SendMessageBindingModel bindingModel, BindingResult bindingResult, Principal principal) {
        User recipient = this.userService.getUserByUsername(username);

        if (username.equals(principal.getName()) || recipient == null) {
            throw new IllegalArgumentException();
        }

        if (bindingResult.hasErrors()) {
            throw new IllegalArgumentException();
        }

        User loggedUser = this.userService.getUserByUsername(principal.getName());
        Thread thread = this.threadService.findThreadByCreatorAndAndRecipient(loggedUser, recipient);

        if (thread == null) {
            thread = this.threadService.createNewThread(loggedUser, recipient);
        }

        Message message = this.messageService.createMessage(bindingModel, loggedUser, thread);
        message.setSentByLoggedUser(message.getCreator() == loggedUser);

        modelAndView.addObject("message", message);
        modelAndView.setViewName("message/message :: message");
        return modelAndView;
    }

    @GetMapping("/threads/{threadId}/messages")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView getAllMessagesByThreadId(ModelAndView modelAndView, @PathVariable String threadId, Principal principal, @PageableDefault(size = 5) Pageable pageable) {
        Thread thread = this.threadService.findThreadById(threadId);

        if (thread == null) {
            throw new ThreadNotFoundException();
        }

        Page<Message> messages = this.messageService.getMessagesByThreadAndPage(
                thread, this.userService.getUserByUsername(principal.getName()), pageable);

        modelAndView.addObject("recipientUsername", thread.getOtherParticipantUsername());
        modelAndView.addObject("messages", messages);
        modelAndView.setViewName("message/messages :: messages");

        return modelAndView;
    }
}