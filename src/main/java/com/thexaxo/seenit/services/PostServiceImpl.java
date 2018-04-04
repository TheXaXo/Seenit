package com.thexaxo.seenit.services;

import com.thexaxo.seenit.entities.Post;
import com.thexaxo.seenit.entities.Subseenit;
import com.thexaxo.seenit.entities.User;
import com.thexaxo.seenit.exceptions.PostNotFoundException;
import com.thexaxo.seenit.models.SubmitLinkBindingModel;
import com.thexaxo.seenit.models.SubmitTextPostBindingModel;
import com.thexaxo.seenit.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public Post createTextPost(SubmitTextPostBindingModel bindingModel, User creator, Subseenit subseenit) {
        Post textPost = new Post();

        textPost.setTitle(bindingModel.getTitle());
        textPost.setText(bindingModel.getText());
        textPost.setSubseenit(subseenit);
        textPost.setCreator(creator);
        textPost.setCreationDate(LocalDateTime.now());
        textPost.setType("text");

        return this.repository.save(textPost);
    }

    @Override
    public Post createLinkPost(SubmitLinkBindingModel bindingModel, User creator, Subseenit subseenit) {
        Post linkPost = new Post();

        linkPost.setTitle(bindingModel.getTitle());
        linkPost.setLink(bindingModel.getLink());
        linkPost.setThumbnailUrl(bindingModel.getThumbnailUrl());
        linkPost.setSubseenit(subseenit);
        linkPost.setCreator(creator);
        linkPost.setCreationDate(LocalDateTime.now());
        linkPost.setType("link");

        return this.repository.save(linkPost);
    }

    @Override
    public void upvote(String postId, User user) {
        Post post = this.repository.findPostById(postId);

        if (post == null) {
            throw new PostNotFoundException();
        }

        if (user.getUpvotedPosts().contains(post)) {
            user.getUpvotedPosts().remove(post);
        } else {
            user.getDownvotedPosts().remove(post);
            user.getUpvotedPosts().add(post);
        }

        this.repository.save(post);
    }

    @Override
    public void downvote(String postId, User user) {
        Post post = this.repository.findPostById(postId);

        if (post == null) {
            throw new PostNotFoundException();
        }

        if (user.getDownvotedPosts().contains(post)) {
            user.getDownvotedPosts().remove(post);
        } else {
            user.getUpvotedPosts().remove(post);
            user.getDownvotedPosts().add(post);
        }

        this.repository.save(post);
    }

    @Override
    public Page<Post> listAllByPage(Pageable pageable) {
        return this.repository.findAll(pageable);
    }

    @Override
    public Page<Post> listAllBySubseenitAndPage(Subseenit subseenit, Pageable pageable) {
        return this.repository.findAllBySubseenit(subseenit, pageable);
    }

    @Override
    public long getAllPostsPagesCount(int size) {
        return (long) Math.ceil((double) this.repository.count() / size);
    }

    @Override
    public long getCommentsPagesCount(Post post, int size) {
        return (long) Math.ceil((double) post.getComments().size() / size);
    }

    @Override
    public Post findById(String postId) {
        return this.repository.findPostById(postId);
    }

    @Override
    public void populateUpvotedDownvotedFields(Page<Post> posts, User user) {
        for (Post post : posts) {
            this.populateUpvotedDownvotedFields(post, user);
        }
    }

    @Override
    public void populateUpvotedDownvotedFields(Post post, User user) {
        if (user.getUpvotedPosts().contains(post)) {
            post.setUpvoted(true);
            post.setDownvoted(false);
        } else if (user.getDownvotedPosts().contains(post)) {
            post.setUpvoted(false);
            post.setDownvoted(true);
        }
    }
}