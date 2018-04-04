package com.thexaxo.seenit.services;

import com.thexaxo.seenit.entities.Subseenit;
import com.thexaxo.seenit.entities.User;
import com.thexaxo.seenit.models.CreateSubseenitBindingModel;

public interface SubseenitService {
    Subseenit findOneSubseenitByName(String name);

    boolean create(CreateSubseenitBindingModel bindingModel, User loggedUser);

    long getPostsPagesCount(Subseenit subseenit, int size);
}