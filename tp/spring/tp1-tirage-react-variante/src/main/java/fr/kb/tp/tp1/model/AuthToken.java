package fr.kb.tp.tp1.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class AuthToken {
    @Id
    private String id;

    @ManyToOne
    private Utilisateur user;

    private Date expirationDate;

    public AuthToken(String id, Utilisateur user, Date expirationDate) {
        this.id = id;
        this.user = user;
        this.expirationDate = expirationDate;
    }



    public String getId() {
        return id;
    }

    public Utilisateur getUser() {
        return user;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUser(Utilisateur user) {
        this.user = user;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }
}
