package com.thexaxo.seenit.models;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

public class RegisterUserBindingModel {
    @NotNull
    @Length(min = 5)
    private String username;

    @NotNull
    @Length(min = 5)
    private String password;

    @NotNull
    @Length(min = 5)
    private String confirmPassword;

    @NotNull
    @Length(min = 5)
    private String email;

    public RegisterUserBindingModel() {

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

    public String getConfirmPassword() {
        return this.confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
