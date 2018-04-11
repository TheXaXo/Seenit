package com.thexaxo.seenit.models;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

public class ChangePasswordBindingModel {
    @NotNull
    @Length(min = 5)
    private String currentPassword;

    @NotNull
    @Length(min = 5)
    private String newPassword;

    @NotNull
    @Length(min = 5)
    private String confirmPassword;

    public ChangePasswordBindingModel() {

    }

    public String getCurrentPassword() {
        return this.currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return this.newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmPassword() {
        return this.confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}