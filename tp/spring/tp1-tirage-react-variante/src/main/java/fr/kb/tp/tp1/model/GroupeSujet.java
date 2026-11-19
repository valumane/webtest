package fr.kb.tp.tp1.model;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

import java.util.Date;

@Entity
public class GroupeSujet {

    @Id
    @EmbeddedId
    private GroupeSujetId id;


    public GroupeSujetId getId() {
        return id;
    }

    public void setId(GroupeSujetId id) {
        this.id = id;
    }
}
