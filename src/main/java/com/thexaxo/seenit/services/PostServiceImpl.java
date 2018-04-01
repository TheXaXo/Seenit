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
import java.util.List;

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
        linkPost.setThumbnailUrl(bindingModel.getThumbnailUrl());
        linkPost.setSubseenit(subseenit);
        linkPost.setCreator(creator);
        linkPost.setCreationDate(LocalDateTime.now());
        linkPost.setType("link");

        this.repository.saveAndFlush(linkPost);
    }

    @Override
    public List<Post> getAllPosts() {
        return this.repository.findAllByOrderByCreationDateDesc();
    }

    @Override
    public void upvote(String postId, User user) {
        Post post = this.repository.findPostById(postId);

        if (post == null) {
            return;
        }

        if (user.getUpvotedPosts().contains(post)) {
            user.getUpvotedPosts().remove(post);
        } else {
            user.getDownvotedPosts().remove(post);
            user.getUpvotedPosts().add(post);
        }

        this.repository.saveAndFlush(post);
    }

    @Override
    public void downvote(String postId, User user) {
        Post post = this.repository.findPostById(postId);

        if (post == null) {
            return;
        }

        if (user.getDownvotedPosts().contains(post)) {
            user.getDownvotedPosts().remove(post);
        } else {
            user.getUpvotedPosts().remove(post);
            user.getDownvotedPosts().add(post);
        }

        this.repository.saveAndFlush(post);
    }
}