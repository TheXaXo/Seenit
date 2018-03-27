package com.thexaxo.seenit.services;

import com.thexaxo.seenit.entities.Subseenit;
import com.thexaxo.seenit.entities.User;
import com.thexaxo.seenit.models.CreateSubseenitBindingModel;
import com.thexaxo.seenit.repositories.SubseenitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDate;
import java.util.ArrayList;

@Service
public class SubseenitServiceImpl implements SubseenitService {
    private SubseenitRepository repository;

    @Autowired
    public SubseenitServiceImpl(SubseenitRepository repository) {
        this.repository = repository;
    }

    @Override
    public Subseenit findOneSubseenitByName(String name) {
        return this.repository.findSubseenitByName(name);
    }

    @Override
    public boolean create(CreateSubseenitBindingModel bindingModel, User loggedUser) {
        Subseenit subseenit = new Subseenit();

        subseenit.setName(bindingModel.getName());
        subseenit.setCreator(loggedUser);
        subseenit.setModerators(new ArrayList<>());
        subseenit.setCreationDate(LocalDate.now());

        try {
            this.repository.saveAndFlush(subseenit);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}