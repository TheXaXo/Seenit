package com.thexaxo.seenit.services;

import com.thexaxo.seenit.entities.Thread;
import com.thexaxo.seenit.entities.User;
import com.thexaxo.seenit.repositories.ThreadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ThreadServiceImpl implements ThreadService {
    private ThreadRepository repository;

    @Autowired
    public ThreadServiceImpl(ThreadRepository repository) {
        this.repository = repository;
    }

    @Override
    public Thread findThreadByCreatorAndAndRecipient(User creator, User recipient) {
        return this.repository.findThreadByParticipantsContainingAndParticipantsContaining(creator, recipient);
    }

    @Override
    public Thread createNewThread(User... participants) {
        Thread thread = new Thread();
        thread.setParticipants(new ArrayList<>(Arrays.asList(participants)));

        return this.repository.saveAndFlush(thread);
    }

    @Override
    public List<Thread> getAllThreadsOfUser(User user) {
        List<Thread> threads = this.repository.getAllByParticipantsContaining(user).stream()
                .sorted((t1, t2) -> {
                    if (t1.getLastMessageSentTime().isBefore(t2.getLastMessageSentTime())) {
                        return 1;
                    } else if (t1.getLastMessageSentTime().isAfter(t2.getLastMessageSentTime())) {
                        return -1;
                    } else {
                        return 0;
                    }
                })
                .collect(Collectors.toCollection(ArrayList::new));

        for (Thread thread : threads) {
            thread.setOtherParticipantUsername(user.getUsername());
        }

        return threads;
    }

    @Override
    public Thread findThreadById(String threadId) {
        return this.repository.findThreadById(threadId);
    }
}