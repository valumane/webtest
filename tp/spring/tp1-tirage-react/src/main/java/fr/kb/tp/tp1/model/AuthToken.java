package fr.kb.tp.tp1.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Date;


@Entity
public class AuthToken {

    @Id
    private String id;

    @ManyToOne
    private Utilisateur user;

    private LocalDateTime expirationDate;

    public String getId() {
        return id;
    }

    public AuthToken setId(String id) {
        this.id = id;
        return this;
    }

    public Utilisateur getUser() {
        return user;
    }

    public AuthToken setUser(Utilisateur user) {
        this.user = user;
        return this;
    }

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }

    public AuthToken setExpirationDate(LocalDateTime expirationDate) {
        this.expirationDate = expirationDate;
        return this;
    }


}
