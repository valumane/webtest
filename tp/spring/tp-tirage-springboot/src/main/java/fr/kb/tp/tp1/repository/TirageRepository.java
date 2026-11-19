package fr.kb.tp.tp1.repository;

import fr.kb.tp.tp1.modele.Tirage;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TirageRepository extends JpaRepository<Tirage, Long> {
    Optional<Tirage> findByGroupe(int groupe);
}
