package com.thexaxo.seenit.entities;

import org.hibernate.annotations.GenericGenerator;
import org.ocpsoft.prettytime.PrettyTime;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Entity
@Table(name = "messages")
public class Message {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private String id;

    @ManyToOne
    private User creator;

    @ManyToOne
    private Thread thread;

    @Column
    private String content;

    @Column
    private LocalDateTime creationDate;

    @Transient
    private boolean isSentByLoggedUser;

    public Message() {

    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getCreator() {
        return this.creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public Thread getThread() {
        return this.thread;
    }

    public void setThread(Thread thread) {
        this.thread = thread;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreationDate() {
        return this.creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public String getSentTimeAgo() {
        PrettyTime p = new PrettyTime();

        Instant instant = this.getCreationDate().atZone(ZoneId.systemDefault()).toInstant();
        Date date = Date.from(instant);

        return p.format(date);
    }

    public boolean isSentByLoggedUser() {
        return this.isSentByLoggedUser;
    }

    public void setSentByLoggedUser(boolean sentByLoggedUser) {
        isSentByLoggedUser = sentByLoggedUser;
    }
}