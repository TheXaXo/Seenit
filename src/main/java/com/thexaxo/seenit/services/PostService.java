package com.thexaxo.seenit.services;

import com.thexaxo.seenit.entities.Post;
import com.thexaxo.seenit.entities.Subseenit;
import com.thexaxo.seenit.entities.User;
import com.thexaxo.seenit.models.SubmitLinkBindingModel;
import com.thexaxo.seenit.models.SubmitTextPostBindingModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostService {
    Post createTextPost(SubmitTextPostBindingModel bindingModel, User creator, Subseenit subseenit);

    Post createLinkPost(SubmitLinkBindingModel bindingModel, User creator, Subseenit subseenit);

    void upvote(String postId, User user);

    void downvote(String postId, User user);

    Page<Post> listAllByPage(Pageable pageable);

    Page<Post> listAllBySubseenitAndPage(Subseenit subseenit, Pageable pageable);

    Page<Post> listAllByCreator(User creator, Pageable pageable);

    Page<Post> listAllUpvotedByUser(User user, Pageable pageable);

    Page<Post> listAllDownvotedByUser(User user, Pageable pageable);

    long getAllPostsPagesCount(int size);

    long getCommentsPagesCount(Post post, int size);

    Post findById(String postId);

    void populateUpvotedDownvotedFields(Page<Post> posts, User user);

    void populateUpvotedDownvotedFields(Post post, User user);
}