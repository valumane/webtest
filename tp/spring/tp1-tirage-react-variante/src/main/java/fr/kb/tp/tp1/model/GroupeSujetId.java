package fr.kb.tp.tp1.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.OneToOne;

@Embeddable
public class GroupeSujetId {

    @OneToOne
    private Groupe groupe;

    @OneToOne
    private Sujet sujet;

    public GroupeSujetId(Groupe groupe, Sujet sujet) {
        this.groupe = groupe;
        this.sujet = sujet;
    }

    public GroupeSujetId() {

    }

    public Groupe getGroupe() {
        return groupe;
    }

    public void setGroupe(Groupe groupe) {
        this.groupe = groupe;
    }

    public Sujet getSujet() {
        return sujet;
    }

    public void setSujet(Sujet sujet) {
        this.sujet = sujet;
    }
}
