package com.thexaxo.seenit.services;

import com.thexaxo.seenit.entities.Comment;
import com.thexaxo.seenit.entities.Post;
import com.thexaxo.seenit.entities.User;
import com.thexaxo.seenit.models.AddCommentBindingModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentService {
    Comment addComment(AddCommentBindingModel bindingModel, User creator, Post post);

    Page<Comment> listAllByPostAndPage(Post post, Pageable pageable);

    Page<Comment> listAllByCreator(User creator, Pageable pageable);

    Page<Comment> listAllUpvotedByUser(User user, Pageable pageable);

    Page<Comment> listAllDownvotedByUser(User user, Pageable pageable);

    void upvote(String commentId, User user);

    void downvote(String commentId, User user);

    void populateUpvotedDownvotedFields(Page<Comment> comments, User user);
}