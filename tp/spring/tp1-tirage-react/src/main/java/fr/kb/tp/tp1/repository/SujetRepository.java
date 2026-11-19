package fr.kb.tp.tp1.repository;

import fr.kb.tp.tp1.model.Groupe;
import fr.kb.tp.tp1.model.Sujet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SujetRepository extends JpaRepository<Sujet, Integer> {

    @Query("from Sujet where id not in (select id.sujet.id from GroupeSujet)")
    public List<Sujet> findSujetLibre();








}
