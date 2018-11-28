package com.thexaxo.seenit.repositories;

import com.thexaxo.seenit.entities.Thread;
import com.thexaxo.seenit.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ThreadRepository extends JpaRepository<Thread, String> {
    Thread findThreadByParticipantsContainingAndParticipantsContaining(User creator, User recipient);

    List<Thread> getAllByParticipantsContaining(User user);

    Thread findThreadById(String threadId);

    int countAllByParticipantsContaining(User user);
}