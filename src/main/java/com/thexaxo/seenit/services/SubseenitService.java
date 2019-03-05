package com.thexaxo.seenit.services;

import com.thexaxo.seenit.entities.Subseenit;
import com.thexaxo.seenit.entities.User;
import com.thexaxo.seenit.models.CreateSubseenitBindingModel;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;

public interface SubseenitService {
    Subseenit findOneSubseenitByName(String name);

    boolean create(CreateSubseenitBindingModel bindingModel, User loggedUser);

    long getPostsPagesCount(Subseenit subseenit, int size);

    Page<Subseenit> getSubseenitsSubscribedByUser(User user, Pageable pageable);
}