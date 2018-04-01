package com.thexaxo.seenit.services;

import com.thexaxo.seenit.entities.Post;
import com.thexaxo.seenit.entities.Subseenit;
import com.thexaxo.seenit.entities.User;
import com.thexaxo.seenit.models.SubmitLinkBindingModel;
import com.thexaxo.seenit.models.SubmitTextPostBindingModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostService {
    void createTextPost(SubmitTextPostBindingModel bindingModel, User creator, Subseenit subseenit);

    void createLinkPost(SubmitLinkBindingModel bindingModel, User creator, Subseenit subseenit);

    void upvote(String postId, User user);

    void downvote(String postId, User user);

    Page<Post> listAllByPage(Pageable pageable);

    Page<Post> listAllBySubsenitAndPage(Subseenit subseenit, Pageable pageable);

    long getTotalPagesCount(int size);
}