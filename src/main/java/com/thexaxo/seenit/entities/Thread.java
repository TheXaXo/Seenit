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
@Table(name = "threads")
public class Thread {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private String id;

    @ManyToMany
    @JoinTable(name = "threads_participants",
            joinColumns = @JoinColumn(name = "thread_id"),
            inverseJoinColumns = @JoinColumn(name = "participant_id"))
    private List<User> participants;

    @OneToMany(mappedBy = "thread")
    private List<Message> messages;

    @Transient
    private String otherParticipantUsername;

    public Thread() {

    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<User> getParticipants() {
        return this.participants;
    }

    public void setParticipants(List<User> participants) {
        this.participants = participants;
    }

    public List<Message> getMessages() {
        return this.messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public String getOtherParticipantUsername() {
        return this.otherParticipantUsername;
    }

    public void setOtherParticipantUsername(String participantOneUsername) {
        this.otherParticipantUsername = this.participants.stream()
                .map(User::getUsername)
                .filter(p -> !p.equals(participantOneUsername))
                .findFirst()
                .orElse(null);
    }

    public LocalDateTime getLastMessageSentTime() {
        return this.messages.stream()
                .map(Message::getCreationDate)
                .max(LocalDateTime::compareTo)
                .orElse(null);
    }

    public String getLastMessageSentTimeAgo() {
        PrettyTime p = new PrettyTime();

        Instant instant = this.getLastMessageSentTime().atZone(ZoneId.systemDefault()).toInstant();
        Date date = Date.from(instant);

        return p.format(date);
    }

    public long getMessagesPagesCount() {
        return (long) Math.ceil((double) this.getMessages().size() / 5);
    }
}