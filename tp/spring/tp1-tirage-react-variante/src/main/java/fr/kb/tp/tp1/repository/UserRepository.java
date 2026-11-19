package fr.kb.tp.tp1.repository;

import fr.kb.tp.tp1.model.Groupe;
import fr.kb.tp.tp1.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Utilisateur, String> {
}
