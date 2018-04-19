package com.thexaxo.seenit.services;

import com.thexaxo.seenit.entities.Message;
import com.thexaxo.seenit.entities.Thread;
import com.thexaxo.seenit.entities.User;
import com.thexaxo.seenit.models.SendMessageBindingModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MessageService {
    Message createMessage(SendMessageBindingModel bindingModel, User creator, Thread thread);

    Page<Message> getMessagesByThreadAndPage(Thread thread, User loggedUser, Pageable pageable);
}