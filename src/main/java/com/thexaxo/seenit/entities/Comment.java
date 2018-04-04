package com.thexaxo.seenit.entities;

import org.hibernate.annotations.GenericGenerator;
import org.ocpsoft.prettytime.PrettyTime;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private String id;

    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne
    private Post post;

    @ManyToOne
    private User creator;

    @Column
    private LocalDateTime creationDate;

    @Transient
    private int score;

    @Transient
    private boolean isUpvoted;

    @Transient
    private boolean isDownvoted;

    @ManyToMany(mappedBy = "upvotedComments")
    private List<User> usersUpvoted;

    @ManyToMany(mappedBy = "downvotedComments")
    private List<User> usersDownvoted;

    public Comment() {

    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Post getPost() {
        return this.post;
    }

    public void setPost(Post post) {
        this.post = post;
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

    public int getScore() {
        return this.getUsersUpvoted().size() - this.getUsersDownvoted().size();
    }

    public boolean isUpvoted() {
        return this.isUpvoted;
    }

    public void setUpvoted(boolean upvoted) {
        isUpvoted = upvoted;
    }

    public boolean isDownvoted() {
        return this.isDownvoted;
    }

    public void setDownvoted(boolean downvoted) {
        isDownvoted = downvoted;
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

    public String getSubmittedTimeAgo() {
        PrettyTime p = new PrettyTime();

        Instant instant = this.creationDate.atZone(ZoneId.systemDefault()).toInstant();
        Date date = Date.from(instant);

        return p.format(date);
    }
}