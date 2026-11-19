package fr.aaw.template;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;

import java.time.Instant;
import java.util.UUID;

@Entity
public class Todo {
    public enum Priority { URGENTE, NORMALE, FAIBLE }

    @Id
    private UUID id;

    @Column(nullable = false, length = 255)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Priority priority;

    @Column(nullable = false)
    private Instant createdAt;

    protected Todo() {}

    public Todo(String title, Priority priority) {
        this.id = UUID.randomUUID();
        this.title = title;
        this.priority = priority;
        this.createdAt = Instant.now();
    }

    public UUID getId() { return id; }
    public String getTitle() { return title; }
    public Priority getPriority() { return priority; }
    public Instant getCreatedAt() { return createdAt; }
}
