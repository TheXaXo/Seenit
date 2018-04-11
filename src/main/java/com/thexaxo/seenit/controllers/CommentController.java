package com.thexaxo.seenit.controllers;

import com.thexaxo.seenit.entities.Comment;
import com.thexaxo.seenit.entities.Post;
import com.thexaxo.seenit.entities.Subseenit;
import com.thexaxo.seenit.entities.User;
import com.thexaxo.seenit.exceptions.InvalidCommentContentException;
import com.thexaxo.seenit.exceptions.PostNotFoundException;
import com.thexaxo.seenit.exceptions.SubseenitNotFoundException;
import com.thexaxo.seenit.exceptions.UserNotFoundException;
import com.thexaxo.seenit.models.AddCommentBindingModel;
import com.thexaxo.seenit.services.CommentService;
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

@Controller
public class CommentController {
    private CommentService commentService;
    private PostService postService;
    private UserService userService;
    private SubseenitService subseenitService;

    @Autowired
    public CommentController(CommentService commentService, PostService postService, UserService userService, SubseenitService subseenitService) {
        this.commentService = commentService;
        this.postService = postService;
        this.userService = userService;
        this.subseenitService = subseenitService;
    }

    @PostMapping("/{postId}/comments/add")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView addCommentConfirm(ModelAndView modelAndView, @PathVariable String postId, @Valid @ModelAttribute("comment") AddCommentBindingModel bindingModel, BindingResult bindingResult, Principal principal) {
        modelAndView.setViewName("comment/comment");

        if (bindingResult.hasErrors()) {
            throw new InvalidCommentContentException();
        }

        Post post = this.postService.findById(postId);

        if (post == null) {
            throw new PostNotFoundException();
        }

        Comment comment = this.commentService.addComment(bindingModel, this.userService.getUserByUsername(principal.getName()), post);

        modelAndView.addObject("comment", comment);
        return modelAndView;
    }

    @GetMapping("/s/{subseenitName}/comments/{postId}/all")
    public ModelAndView getCommentsFromPost(@PathVariable String subseenitName, @PathVariable String postId, ModelAndView modelAndView, Principal principal, @PageableDefault(size = 10) Pageable pageable) {
        Subseenit subseenit = this.subseenitService.findOneSubseenitByName(subseenitName);

        if (subseenit == null) {
            throw new SubseenitNotFoundException();
        }

        Post post = this.postService.findById(postId);

        if (post == null) {
            throw new PostNotFoundException();
        }

        Page<Comment> comments = this.commentService.listAllByPostAndPage(post, pageable);

        if (principal != null && comments != null) {
            User user = this.userService.getUserByUsername(principal.getName());

            this.commentService.populateUpvotedDownvotedFields(comments, user);
        }

        modelAndView.addObject("comments", comments);
        modelAndView.setViewName("comment/comments :: comments");

        return modelAndView;
    }

    @GetMapping("/u/{username}/comments")
    public ModelAndView getCommentsSubmittedByUser(ModelAndView modelAndView, @PathVariable String username, Principal principal, @PageableDefault(size = 10) Pageable pageable) {
        Page<Comment> comments;

        User user = this.userService.getUserByUsername(username);

        if (user == null) {
            throw new UserNotFoundException();
        }

        comments = this.commentService.listAllByCreator(user, pageable);

        if (principal != null && comments != null) {
            User loggedUser = this.userService.getUserByUsername(principal.getName());
            this.commentService.populateUpvotedDownvotedFields(comments, loggedUser);
        }

        modelAndView.addObject("comments", comments);
        modelAndView.setViewName("comment/comments :: comments");

        return modelAndView;
    }

    @GetMapping("/u/{username}/comments/upvoted")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView getCommentsUpvotedByUser(ModelAndView modelAndView, @PathVariable String username, Principal principal, @PageableDefault(size = 10) Pageable pageable) {
        if (!principal.getName().equals(username)) {
            throw new IllegalArgumentException();
        }

        Page<Comment> comments;

        User user = this.userService.getUserByUsername(username);

        if (user == null) {
            throw new UserNotFoundException();
        }

        comments = this.commentService.listAllUpvotedByUser(user, pageable);
        this.commentService.populateUpvotedDownvotedFields(comments, user);

        modelAndView.addObject("comments", comments);
        modelAndView.setViewName("comment/comments :: comments");

        return modelAndView;
    }

    @GetMapping("/u/{username}/comments/downvoted")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView getCommentsDownvotedByUser(ModelAndView modelAndView, @PathVariable String username, Principal principal, @PageableDefault(size = 10) Pageable pageable) {
        if (!principal.getName().equals(username)) {
            throw new IllegalArgumentException();
        }

        Page<Comment> comments;

        User user = this.userService.getUserByUsername(username);

        if (user == null) {
            throw new UserNotFoundException();
        }

        comments = this.commentService.listAllDownvotedByUser(user, pageable);
        this.commentService.populateUpvotedDownvotedFields(comments, user);

        modelAndView.addObject("comments", comments);
        modelAndView.setViewName("comment/comments");

        return modelAndView;
    }

    @GetMapping("/upvote/comment/{commentId}")
    public ResponseEntity upvote(@PathVariable String commentId, Principal principal) {
        this.commentService.upvote(commentId, userService.getUserByUsername(principal.getName()));

        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/downvote/comment/{commentId}")
    public ResponseEntity downvote(@PathVariable String commentId, Principal principal) {
        this.commentService.downvote(commentId, userService.getUserByUsername(principal.getName()));

        return new ResponseEntity(HttpStatus.OK);
    }
}