package fr.kb.tp.tp1.modele;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "tirages")
public class Tirage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private int groupe;

    @Column(nullable = false, unique = true)
    private int sujet;

    @Column(nullable = false)
    private Instant createdAt;

    protected Tirage() { }

    public Tirage(int groupe, int sujet) {
        this.groupe = groupe;
        this.sujet = sujet;
        this.createdAt = Instant.now();
    }

    public Long getId() { return id; }
    public int getGroupe() { return groupe; }
    public int getSujet() { return sujet; }
    public Instant getCreatedAt() { return createdAt; }
}
