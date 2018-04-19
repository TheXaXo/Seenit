package com.thexaxo.seenit.services;

import com.thexaxo.seenit.entities.Message;
import com.thexaxo.seenit.entities.Thread;
import com.thexaxo.seenit.entities.User;
import com.thexaxo.seenit.models.SendMessageBindingModel;
import com.thexaxo.seenit.repositories.MessageRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class MessageServiceImpl implements MessageService {
    private MessageRepository repository;

    public MessageServiceImpl(MessageRepository repository) {
        this.repository = repository;
    }

    @Override
    public Message createMessage(SendMessageBindingModel bindingModel, User creator, Thread thread) {
        Message message = new Message();
        message.setContent(bindingModel.getContent());
        message.setCreator(creator);
        message.setThread(thread);
        message.setCreationDate(LocalDateTime.now());

        return this.repository.save(message);
    }

    @Override
    public Page<Message> getMessagesByThreadAndPage(Thread thread, User loggedUser, Pageable pageable) {
        Page<Message> messages = this.repository.getAllByThread(thread, pageable);

        for (Message message : messages) {
            message.setSentByLoggedUser(message.getCreator() == loggedUser);
        }

        return messages;
    }
}