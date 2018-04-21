package com.thexaxo.seenit.controllers;

import com.thexaxo.seenit.entities.User;
import com.thexaxo.seenit.exceptions.UserNotFoundException;
import com.thexaxo.seenit.models.ChangePasswordBindingModel;
import com.thexaxo.seenit.models.EditUserBindingModel;
import com.thexaxo.seenit.models.LoginUserBindingModel;
import com.thexaxo.seenit.models.RegisterUserBindingModel;
import com.thexaxo.seenit.services.RoleService;
import com.thexaxo.seenit.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;

@Controller
public class UserController {
    private UserService userService;
    private RoleService roleService;

    @Autowired
    public UserController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
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

    @GetMapping("/u/{username}")
    public ModelAndView profile(ModelAndView modelAndView, @PathVariable String username, Principal principal, @PageableDefault(size = 10) Pageable pageable) {
        User user = this.userService.getUserByUsername(username);

        if (user == null) {
            throw new UserNotFoundException();
        }

        modelAndView.addObject("user", user);
        modelAndView.addObject("submittedPostsPages",
                userService.getSubmittedPostsPages(user, pageable.getPageSize()));
        modelAndView.addObject("submittedCommentsPages",
                userService.getSubmittedCommentsPages(user, pageable.getPageSize()));

        if (principal != null && principal.getName().equals(username)) {
            modelAndView.addObject("savedPostsPages",
                    userService.getSavedPostsPages(user, pageable.getPageSize()));
            modelAndView.addObject("upvotedPostsPages",
                    userService.getUpvotedPostsPages(user, pageable.getPageSize()));
            modelAndView.addObject("downvotedPostsPages",
                    userService.getDownvotedPostsPages(user, pageable.getPageSize()));
            modelAndView.addObject("upvotedCommentsPages",
                    userService.getUpvotedCommentsPages(user, pageable.getPageSize()));
            modelAndView.addObject("downvotedCommentsPages",
                    userService.getDownvotedCommentsPages(user, pageable.getPageSize()));
        }

        modelAndView.addObject("view", "profile/profile :: profile");
        modelAndView.setViewName("base-layout");

        return modelAndView;
    }

    @GetMapping("/user/password/change")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView changePassword(ModelAndView modelAndView, @ModelAttribute("user") ChangePasswordBindingModel bindingModel) {
        modelAndView.addObject("view", "user/password-change :: password-change");
        modelAndView.setViewName("base-layout");

        return modelAndView;
    }

    @PostMapping("/user/password/change")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView changePasswordConfirm(ModelAndView modelAndView, @Valid @ModelAttribute("user") ChangePasswordBindingModel bindingModel, BindingResult bindingResult, Principal principal) {
        modelAndView.addObject("view", "user/password-change :: password-change");
        modelAndView.setViewName("base-layout");

        if (bindingResult.hasErrors()) {
            return modelAndView;
        }

        if (!bindingModel.getNewPassword().equals(bindingModel.getConfirmPassword())) {
            modelAndView.addObject("passwordsDoNotMatch", true);
            return modelAndView;
        }

        User loggedUser = this.userService.getUserByUsername(principal.getName());

        boolean hasChanged = this.userService.changePassword(bindingModel, loggedUser);

        if (!hasChanged) {
            modelAndView.addObject("wrongPassword", true);
            return modelAndView;
        }

        modelAndView.clear();
        modelAndView.setViewName("redirect:/u/" + loggedUser.getUsername());

        return modelAndView;
    }

    @GetMapping("/user/edit/{username}")
    @PreAuthorize("hasAnyRole('GOD', 'ADMIN')")
    public ModelAndView edit(ModelAndView modelAndView, @PathVariable String username, @ModelAttribute("user") EditUserBindingModel bindingModel, Authentication authentication) {
        User user = this.userService.getUserByUsername(username);

        if (user == null) {
            throw new UserNotFoundException();
        }

        bindingModel.setUsername(user.getUsername());
        bindingModel.setEmail(user.getEmail());
        bindingModel.setRoles(new ArrayList<>());

        user.getAuthorities()
                .forEach(a -> bindingModel.getRoles().add(a.getAuthority()));

        boolean isGod = authentication.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("ROLE_GOD"));

        modelAndView.addObject("isGod", isGod);
        modelAndView.addObject("roles", this.roleService.getAllRoles());
        modelAndView.setViewName("admin/user-edit :: user-edit");

        return modelAndView;
    }

    @PostMapping("/user/edit/{username}")
    @PreAuthorize("hasAnyRole('GOD', 'ADMIN')")
    public ModelAndView editConfirm(ModelAndView modelAndView, @PathVariable String username, @Valid @ModelAttribute("user") EditUserBindingModel bindingModel, BindingResult bindingResult, Authentication authentication, Principal principal) {
        User user = this.userService.getUserByUsername(username);

        if (user == null) {
            throw new UserNotFoundException();
        }

        boolean isGod = authentication.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("ROLE_GOD"));

        this.userService.edit(isGod, principal.getName().equals(username), username, bindingModel);

        isGod = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("ROLE_GOD"));

        modelAndView.addObject("isGod", isGod);
        modelAndView.addObject("roles", this.roleService.getAllRoles());
        modelAndView.setViewName("admin/user-edit :: user-edit");

        return modelAndView;
    }
}