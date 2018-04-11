package com.thexaxo.seenit.entities;

import org.hibernate.annotations.GenericGenerator;
import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private String id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String email;

    @Column
    private LocalDateTime registrationDate;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    @OneToMany(mappedBy = "creator")
    private List<Post> posts;

    @ManyToMany
    @JoinTable(name = "users_upvotedPosts",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "post_id"))
    private List<Post> upvotedPosts;

    @ManyToMany
    @JoinTable(name = "users_downvotedPosts",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "post_id"))
    private List<Post> downvotedPosts;

    @ManyToMany
    @JoinTable(name = "users_savedPosts",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "post_id"))
    private List<Post> savedPosts;

    @OneToMany(mappedBy = "creator")
    private List<Comment> comments;

    @ManyToMany
    @JoinTable(name = "users_upvotedComments",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "comment_id"))
    private List<Comment> upvotedComments;

    @ManyToMany
    @JoinTable(name = "users_downvotedComments",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "comment_id"))
    private List<Comment> downvotedComments;

    @OneToMany(mappedBy = "creator")
    private List<Subseenit> createdSubseenits;

    @ManyToMany
    @JoinTable(name = "users_moderatedSubseenits",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "subseenit_id"))
    private List<Subseenit> moderatedSubseenits;

    @ManyToMany
    @JoinTable(name = "users_subscribedSubseenits",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "subseenit_id"))
    private List<Subseenit> subscribedSubseenits;

    @Transient
    private int postKarma;

    @Transient
    private int commentKarma;

    private boolean isAccountNonExpired;

    private boolean isAccountNonLocked;

    private boolean isCredentialsNonExpired;

    private boolean isEnabled;

    public User() {

    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getRegistrationDate() {
        return this.registrationDate;
    }

    public void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles;
    }

    public void setAuthorities(Set<Role> authorities) {
        this.roles = authorities;
    }

    public List<Post> getPosts() {
        return this.posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public List<Post> getUpvotedPosts() {
        return this.upvotedPosts;
    }

    public void setUpvotedPosts(List<Post> upvotedPosts) {
        this.upvotedPosts = upvotedPosts;
    }

    public List<Post> getDownvotedPosts() {
        return this.downvotedPosts;
    }

    public void setDownvotedPosts(List<Post> downvotedPosts) {
        this.downvotedPosts = downvotedPosts;
    }

    public List<Post> getSavedPosts() {
        return this.savedPosts;
    }

    public void setSavedPosts(List<Post> savedPosts) {
        this.savedPosts = savedPosts;
    }

    public List<Comment> getComments() {
        return this.comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<Comment> getUpvotedComments() {
        return this.upvotedComments;
    }

    public void setUpvotedComments(List<Comment> upvotedComments) {
        this.upvotedComments = upvotedComments;
    }

    public List<Comment> getDownvotedComments() {
        return this.downvotedComments;
    }

    public void setDownvotedComments(List<Comment> downvotedComments) {
        this.downvotedComments = downvotedComments;
    }

    public List<Subseenit> getCreatedSubseenits() {
        return this.createdSubseenits;
    }

    public void setCreatedSubseenits(List<Subseenit> createdSubseenits) {
        this.createdSubseenits = createdSubseenits;
    }

    public List<Subseenit> getModeratedSubseenits() {
        return this.moderatedSubseenits;
    }

    public void setModeratedSubseenits(List<Subseenit> moderatedSubseenits) {
        this.moderatedSubseenits = moderatedSubseenits;
    }

    public List<Subseenit> getSubscribedSubseenits() {
        return this.subscribedSubseenits;
    }

    public void setSubscribedSubseenits(List<Subseenit> subscribedSubseenits) {
        this.subscribedSubseenits = subscribedSubseenits;
    }

    public int getPostKarma() {
        return this.posts.stream()
                .mapToInt(Post::getScore)
                .sum();
    }

    public int getCommentKarma() {
        return this.comments.stream()
                .mapToInt(Comment::getScore)
                .sum();
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.isAccountNonExpired;
    }

    public void setAccountNonExpired(boolean accountNonExpired) {
        isAccountNonExpired = accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.isAccountNonLocked;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        isAccountNonLocked = accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.isCredentialsNonExpired;
    }

    public void setCredentialsNonExpired(boolean credentialsNonExpired) {
        isCredentialsNonExpired = credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public boolean isSubscribedTo(String subseenitName) {
        return this.getSubscribedSubseenits().stream()
                .anyMatch(s -> s.getName().equals(subseenitName));
    }

    public String getRegisteredTimeAgo() {
        PrettyTime p = new PrettyTime();

        Instant instant = this.getRegistrationDate().atZone(ZoneId.systemDefault()).toInstant();
        Date date = Date.from(instant);

        return p.format(date);
    }
}