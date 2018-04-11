package com.thexaxo.seenit.repositories;

import com.thexaxo.seenit.entities.Post;
import com.thexaxo.seenit.entities.Subseenit;
import com.thexaxo.seenit.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends PagingAndSortingRepository<Post, String> {
    Post findPostById(String id);

    Page<Post> findAllBySubseenit(@Param("subseenit") Subseenit subseenit, Pageable pageable);

    Page<Post> findAllByCreator(@Param("creator") User creator, Pageable pageable);

    Page<Post> findAllByUsersUpvoted(@Param("user") User user, Pageable pageable);

    Page<Post> findAllByUsersDownvoted(@Param("user") User user, Pageable pageable);
}