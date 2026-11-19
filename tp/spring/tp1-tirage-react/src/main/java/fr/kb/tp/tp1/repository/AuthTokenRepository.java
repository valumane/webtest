package fr.kb.tp.tp1.repository;

import fr.kb.tp.tp1.model.AuthToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthTokenRepository extends JpaRepository<AuthToken, String> {
}
