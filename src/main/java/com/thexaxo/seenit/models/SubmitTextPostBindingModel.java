package com.thexaxo.seenit.models;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

public class SubmitTextPostBindingModel {
    @NotNull
    @Length(min = 3, max = 150)
    private String title;

    @NotNull
    @Length(min = 3, max = 65535)
    private String text;

    @NotNull
    private String subseenit;

    public SubmitTextPostBindingModel() {

    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSubseenit() {
        return this.subseenit;
    }

    public void setSubseenit(String subseenit) {
        this.subseenit = subseenit;
    }
}