package com.taskmanager.server.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class ReminderNotification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String message;

    private LocalDateTime triggeredAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Getters
    public Long getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getTriggeredAt() {
        return triggeredAt;
    }

    public User getUser() {
        return user;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setTriggeredAt(LocalDateTime triggeredAt) {
        this.triggeredAt = triggeredAt;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
