package com.thexaxo.seenit.entities;

import org.hibernate.annotations.GenericGenerator;
import org.ocpsoft.prettytime.PrettyTime;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private String id;

    @Column
    private String title;

    @Column
    private String text;

    @Column
    private String link;

    @Column
    private String thumbnailUrl;

    @ManyToOne
    private Subseenit subseenit;

    @ManyToOne
    private User creator;

    @Column
    private LocalDateTime creationDate;

    @Column
    private String type;

    @Transient
    private int score;

    @ManyToMany(mappedBy = "upvotedPosts")
    private List<User> usersUpvoted;

    @ManyToMany(mappedBy = "downvotedPosts")
    private List<User> usersDownvoted;

    @ManyToMany(mappedBy = "savedPosts")
    private List<User> usersSaved;

    public Post() {

    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Subseenit getSubseenit() {
        return this.subseenit;
    }

    public void setSubseenit(Subseenit subseenit) {
        this.subseenit = subseenit;
    }

    public User getCreator() {
        return this.creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public LocalDateTime getCreationDate() {
        return this.creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getScore() {
        return this.usersUpvoted.size() - this.usersDownvoted.size();
    }

    public List<User> getUsersUpvoted() {
        return this.usersUpvoted;
    }

    public void setUsersUpvoted(List<User> usersUpvoted) {
        this.usersUpvoted = usersUpvoted;
    }

    public List<User> getUsersDownvoted() {
        return this.usersDownvoted;
    }

    public void setUsersDownvoted(List<User> usersDownvoted) {
        this.usersDownvoted = usersDownvoted;
    }

    public List<User> getUsersSaved() {
        return this.usersSaved;
    }

    public void setUsersSaved(List<User> usersSaved) {
        this.usersSaved = usersSaved;
    }

    public String getSubmittedTimeAgo() {
        PrettyTime p = new PrettyTime();

        Instant instant = this.creationDate.atZone(ZoneId.systemDefault()).toInstant();
        Date date = Date.from(instant);

        return p.format(date);
    }
}