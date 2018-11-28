package com.thexaxo.seenit.services;

import com.thexaxo.seenit.entities.Thread;
import com.thexaxo.seenit.entities.User;

import java.util.List;

public interface ThreadService {
    Thread findThreadByCreatorAndAndRecipient(User creator, User recipient);

    Thread createNewThread(User... participants);

    List<Thread> getAllThreadsOfUser(User user);

    boolean isInboxEmpty(User user);

    Thread findThreadById(String threadId);
}