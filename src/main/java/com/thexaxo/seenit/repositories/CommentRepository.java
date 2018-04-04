package com.thexaxo.seenit.repositories;

import com.thexaxo.seenit.entities.Comment;
import com.thexaxo.seenit.entities.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends PagingAndSortingRepository<Comment, Long> {
    Comment findCommentById(String id);

    Page<Comment> findAllByPost(@Param("post") Post post, Pageable pageable);
}