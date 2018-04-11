package com.thexaxo.seenit.controllers;

import com.thexaxo.seenit.entities.Post;
import com.thexaxo.seenit.entities.Subseenit;
import com.thexaxo.seenit.entities.User;
import com.thexaxo.seenit.exceptions.PostNotFoundException;
import com.thexaxo.seenit.exceptions.SubseenitNotFoundException;
import com.thexaxo.seenit.exceptions.UserNotFoundException;
import com.thexaxo.seenit.models.SubmitLinkBindingModel;
import com.thexaxo.seenit.models.SubmitTextPostBindingModel;
import com.thexaxo.seenit.services.PostService;
import com.thexaxo.seenit.services.SubseenitService;
import com.thexaxo.seenit.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
            throw new SubseenitNotFoundException();
        }

        Post post = this.postService.createTextPost(bindingModel, this.userService.getUserByUsername(principal.getName()), subseenit);

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
            throw new SubseenitNotFoundException();
        }

        Post post = this.postService.createLinkPost(bindingModel, this.userService.getUserByUsername(principal.getName()), subseenit);

        modelAndView.clear();
        modelAndView.setViewName("redirect:/s/" + subseenit.getName() + "/comments/" + post.getId());

        return modelAndView;
    }

    @GetMapping("/upvote/post/{postId}")
    public ResponseEntity upvote(@PathVariable String postId, Principal principal) {
        this.postService.upvote(postId, userService.getUserByUsername(principal.getName()));

        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/downvote/post/{postId}")
    public ResponseEntity downvote(@PathVariable String postId, Principal principal) {
        this.postService.downvote(postId, userService.getUserByUsername(principal.getName()));

        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/s/{name}/posts")
    public ModelAndView getPostsFromSubseenit(ModelAndView modelAndView, @PathVariable String name, Principal principal, @PageableDefault(size = 10) Pageable pageable) {
        User user;
        Page<Post> posts = null;

        if (name.equals("all")) {
            posts = this.postService.listAllByPage(pageable);
        } else {
            Subseenit subseenit = this.subseenitService.findOneSubseenitByName(name);

            if (subseenit != null) {
                posts = this.postService.listAllBySubseenitAndPage(subseenit, pageable);
            }
        }

        if (principal != null && posts != null) {
            user = this.userService.getUserByUsername(principal.getName());
            this.postService.populateUpvotedDownvotedFields(posts, user);
        }

        modelAndView.addObject("posts", posts);
        modelAndView.setViewName("post/list-posts");

        return modelAndView;
    }

    @GetMapping("/u/{username}/posts")
    public ModelAndView getPostsSubmittedByUser(ModelAndView modelAndView, @PathVariable String username, Principal principal, @PageableDefault(size = 10) Pageable pageable) {
        Page<Post> posts;

        User user = this.userService.getUserByUsername(username);

        if (user == null) {
            throw new UserNotFoundException();
        }

        posts = this.postService.listAllByCreator(user, pageable);

        if (principal != null && posts != null) {
            User loggedUser = this.userService.getUserByUsername(principal.getName());
            this.postService.populateUpvotedDownvotedFields(posts, loggedUser);
        }

        modelAndView.addObject("posts", posts);
        modelAndView.setViewName("post/list-posts");

        return modelAndView;
    }

    @GetMapping("/u/{username}/posts/upvoted")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView getPostsUpvotedByUser(ModelAndView modelAndView, @PathVariable String username, Principal principal, @PageableDefault(size = 10) Pageable pageable) {
        if (!principal.getName().equals(username)) {
            throw new IllegalArgumentException();
        }

        Page<Post> posts;

        User user = this.userService.getUserByUsername(username);

        if (user == null) {
            throw new UserNotFoundException();
        }

        posts = this.postService.listAllUpvotedByUser(user, pageable);
        this.postService.populateUpvotedDownvotedFields(posts, user);

        modelAndView.addObject("posts", posts);
        modelAndView.setViewName("post/list-posts");

        return modelAndView;
    }

    @GetMapping("/u/{username}/posts/downvoted")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView getPostsDownvotedByUser(ModelAndView modelAndView, @PathVariable String username, Principal principal, @PageableDefault(size = 10) Pageable pageable) {
        if (!principal.getName().equals(username)) {
            throw new IllegalArgumentException();
        }

        Page<Post> posts;

        User user = this.userService.getUserByUsername(username);

        if (user == null) {
            throw new UserNotFoundException();
        }

        posts = this.postService.listAllDownvotedByUser(user, pageable);
        this.postService.populateUpvotedDownvotedFields(posts, user);

        modelAndView.addObject("posts", posts);
        modelAndView.setViewName("post/list-posts");

        return modelAndView;
    }

    @GetMapping("/s/{subseenitName}/comments/{postId}")
    public ModelAndView postPage(@PathVariable String subseenitName, @PathVariable String postId, ModelAndView modelAndView, Principal principal, @PageableDefault(size = 10) Pageable pageable) {
        Subseenit subseenit = this.subseenitService.findOneSubseenitByName(subseenitName);

        if (subseenit == null) {
            throw new SubseenitNotFoundException();
        }

        Post post = this.postService.findById(postId);

        if (post == null) {
            throw new PostNotFoundException();
        }

        if (principal != null) {
            User user = this.userService.getUserByUsername(principal.getName());

            this.postService.populateUpvotedDownvotedFields(post, user);
            modelAndView.addObject("isSubscribed", user.isSubscribedTo(subseenit.getName()));
        }

        modelAndView.addObject("totalPages", this.postService.getCommentsPagesCount(post, pageable.getPageSize()));
        modelAndView.addObject("post", post);
        modelAndView.addObject("view", "post/post-page :: post-page");
        modelAndView.addObject("subseenitName", subseenit.getName());
        modelAndView.setViewName("base-layout");

        return modelAndView;
    }
}