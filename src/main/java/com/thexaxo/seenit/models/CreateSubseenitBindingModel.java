package com.thexaxo.seenit.models;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

public class CreateSubseenitBindingModel {
    @NotNull
    @Length(min = 3, max = 30)
    private String name;

    public CreateSubseenitBindingModel() {

    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}