package com.thexaxo.seenit.models;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class SubmitLinkBindingModel {
    @NotNull
    @Length(min = 3, max = 150)
    private String title;

    @NotNull
    @Pattern(regexp = "^(https?:\\/\\/)?([\\da-z\\.-]+)\\.([a-z\\.]{2,6})([\\/\\w \\.-]*)*\\/?$")
    private String link;

    private String thumbnailUrl;

    @NotNull
    private String subseenit;

    public SubmitLinkBindingModel() {

    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return this.link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getThumbnailUrl() {
        return this.thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getSubseenit() {
        return this.subseenit;
    }

    public void setSubseenit(String subseenit) {
        this.subseenit = subseenit;
    }
}