package com.thexaxo.seenit.services;

import com.thexaxo.seenit.entities.Post;
import com.thexaxo.seenit.entities.Subseenit;
import com.thexaxo.seenit.entities.User;
import com.thexaxo.seenit.models.SubmitLinkBindingModel;
import com.thexaxo.seenit.models.SubmitTextPostBindingModel;

import java.util.List;

public interface PostService {
    void createTextPost(SubmitTextPostBindingModel bindingModel, User creator, Subseenit subseenit);

    void createLinkPost(SubmitLinkBindingModel bindingModel, User creator, Subseenit subseenit);

    List<Post> getAllPosts();
}