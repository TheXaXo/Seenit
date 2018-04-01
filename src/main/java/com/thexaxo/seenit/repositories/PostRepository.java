package com.thexaxo.seenit.repositories;

import com.thexaxo.seenit.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, String> {
    List<Post> findAllByOrderByCreationDateDesc();

    Post findPostById(String id);
}
