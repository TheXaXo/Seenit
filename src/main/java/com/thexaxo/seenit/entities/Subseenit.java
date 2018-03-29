package com.thexaxo.seenit.entities;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "subseenits")
public class Subseenit {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private String id;

    @Column(nullable = false, unique = true)
    private String name;

    @ManyToOne
    private User creator;

    @OneToMany(mappedBy = "subseenit")
    private List<Post> posts;

    @ManyToMany(mappedBy = "moderatedSubseenits")
    private List<User> moderators;

    @ManyToMany(mappedBy = "subscribedSubseenits")
    private List<User> subscribers;

    @Column(nullable = false)
    private LocalDate creationDate;

    public Subseenit() {

    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getCreator() {
        return this.creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public List<Post> getPosts() {
        return this.posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public List<User> getModerators() {
        return this.moderators;
    }

    public void setModerators(List<User> moderators) {
        this.moderators = moderators;
    }

    public LocalDate getCreationDate() {
        return this.creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public List<User> getSubscribers() {
        return this.subscribers;
    }

    public void setSubscribers(List<User> subscribers) {
        this.subscribers = subscribers;
    }
}