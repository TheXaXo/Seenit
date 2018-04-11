package com.thexaxo.seenit.services;

import com.thexaxo.seenit.entities.Comment;
import com.thexaxo.seenit.entities.Post;
import com.thexaxo.seenit.entities.User;
import com.thexaxo.seenit.exceptions.CommentNotFoundException;
import com.thexaxo.seenit.models.AddCommentBindingModel;
import com.thexaxo.seenit.repositories.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
public class CommentServiceImpl implements CommentService {
    private CommentRepository repository;

    @Autowired
    public CommentServiceImpl(CommentRepository repository) {
        this.repository = repository;
    }

    @Override
    public Comment addComment(AddCommentBindingModel bindingModel, User creator, Post post) {
        Comment comment = new Comment();

        comment.setContent(bindingModel.getContent());
        comment.setPost(post);
        comment.setCreator(creator);
        comment.setCreationDate(LocalDateTime.now());
        comment.setUsersUpvoted(new ArrayList<>());
        comment.setUsersDownvoted(new ArrayList<>());

        return this.repository.save(comment);
    }

    @Override
    public Page<Comment> listAllByPostAndPage(Post post, Pageable pageable) {
        return this.repository.findAllByPost(post, pageable);
    }

    @Override
    public Page<Comment> listAllByCreator(User creator, Pageable pageable) {
        return this.repository.findAllByCreator(creator, pageable);
    }

    @Override
    public Page<Comment> listAllUpvotedByUser(User user, Pageable pageable) {
        return this.repository.findAllByUsersUpvoted(user, pageable);
    }

    @Override
    public Page<Comment> listAllDownvotedByUser(User user, Pageable pageable) {
        return this.repository.findAllByUsersDownvoted(user, pageable);
    }

    @Override
    public void upvote(String commentId, User user) {
        Comment comment = this.repository.findCommentById(commentId);

        if (comment == null) {
            throw new CommentNotFoundException();
        }

        if (user.getUpvotedComments().contains(comment)) {
            user.getUpvotedComments().remove(comment);
        } else {
            user.getDownvotedComments().remove(comment);
            user.getUpvotedComments().add(comment);
        }

        this.repository.save(comment);
    }

    @Override
    public void downvote(String commentId, User user) {
        Comment comment = this.repository.findCommentById(commentId);

        if (comment == null) {
            throw new CommentNotFoundException();
        }

        if (user.getDownvotedComments().contains(comment)) {
            user.getDownvotedComments().remove(comment);
        } else {
            user.getUpvotedComments().remove(comment);
            user.getDownvotedComments().add(comment);
        }

        this.repository.save(comment);
    }

    @Override
    public void populateUpvotedDownvotedFields(Page<Comment> comments, User user) {
        for (Comment comment : comments) {
            if (user.getUpvotedComments().contains(comment)) {
                comment.setUpvoted(true);
                comment.setDownvoted(false);
            } else if (user.getDownvotedComments().contains(comment)) {
                comment.setUpvoted(false);
                comment.setDownvoted(true);
            }
        }
    }
}