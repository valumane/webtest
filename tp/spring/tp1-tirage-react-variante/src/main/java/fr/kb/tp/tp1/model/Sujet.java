package fr.kb.tp.tp1.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Sujet {

    @Id
    private Integer id;
    private String label;

    public Sujet() {
    }

    public Sujet(Integer id, String label) {
        this.id = id;
        this.label = label;
    }

    public Integer getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
