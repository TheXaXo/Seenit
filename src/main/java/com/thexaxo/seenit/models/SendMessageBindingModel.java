package com.thexaxo.seenit.models;

import org.hibernate.validator.constraints.Length;

public class SendMessageBindingModel {
    @Length(min = 1, max = 255)
    private String content;

    public SendMessageBindingModel() {

    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}