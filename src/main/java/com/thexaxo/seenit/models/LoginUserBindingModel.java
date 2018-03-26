package com.thexaxo.seenit.models;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

public class LoginUserBindingModel {
    @NotNull
    @Length(min = 5)
    private String username;

    @NotNull
    @Length(min = 5)
    private String password;

    public LoginUserBindingModel() {

    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}