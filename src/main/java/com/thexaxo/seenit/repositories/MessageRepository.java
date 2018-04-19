package com.thexaxo.seenit.repositories;

import com.thexaxo.seenit.entities.Message;
import com.thexaxo.seenit.entities.Thread;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends PagingAndSortingRepository<Message, String> {
    Page<Message> getAllByThread(Thread thread, Pageable pageable);
}