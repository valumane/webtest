package fr.kb.tp.tp1.repository;

import fr.kb.tp.tp1.model.Groupe;
import fr.kb.tp.tp1.model.GroupeSujet;
import fr.kb.tp.tp1.model.GroupeSujetId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupeSujetRepository extends JpaRepository<GroupeSujet, GroupeSujetId> {
}
