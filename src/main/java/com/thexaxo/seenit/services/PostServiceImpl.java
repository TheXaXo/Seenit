package com.thexaxo.seenit.services;

import com.thexaxo.seenit.entities.Post;
import com.thexaxo.seenit.entities.Subseenit;
import com.thexaxo.seenit.entities.User;
import com.thexaxo.seenit.models.SubmitLinkBindingModel;
import com.thexaxo.seenit.models.SubmitTextPostBindingModel;
import com.thexaxo.seenit.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PostServiceImpl implements PostService {
    private PostRepository repository;

    @Autowired
    public PostServiceImpl(PostRepository repository) {
        this.repository = repository;
    }

    @Override
    public void createTextPost(SubmitTextPostBindingModel bindingModel, User creator, Subseenit subseenit) {
        Post textPost = new Post();

        textPost.setTitle(bindingModel.getTitle());
        textPost.setText(bindingModel.getText());
        textPost.setSubseenit(subseenit);
        textPost.setCreator(creator);
        textPost.setCreationDate(LocalDateTime.now());
        textPost.setType("text");

        this.repository.saveAndFlush(textPost);
    }

    @Override
    public void createLinkPost(SubmitLinkBindingModel bindingModel, User creator, Subseenit subseenit) {
        Post linkPost = new Post();

        linkPost.setTitle(bindingModel.getTitle());
        linkPost.setLink(bindingModel.getLink());
        linkPost.setSubseenit(subseenit);
        linkPost.setCreator(creator);
        linkPost.setCreationDate(LocalDateTime.now());
        linkPost.setType("link");

        this.repository.saveAndFlush(linkPost);
    }
}