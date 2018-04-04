package com.thexaxo.seenit.models;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

public class AddCommentBindingModel {
    @NotNull
    @Length(min = 3, max = 65535)
    private String content;

    public AddCommentBindingModel() {

    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}