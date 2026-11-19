package fr.kb.tp.tp1.modele;

import fr.kb.tp.tp1.repository.TirageRepository;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

/** Remplace la Map en mémoire par une table H2 persistante. */
@Service
public class Content {
    private static final int NOMBRE_GROUPES = 12;
    private static final int NOMBRE_SUJETS = 12;
    private final TirageRepository repository;
    private final TransactionTemplate transaction;
    private final SecureRandom random = new SecureRandom();
    private final Object verrou = new Object();

    public Content(TirageRepository repository, PlatformTransactionManager manager) {
        this.repository = repository;
        this.transaction = new TransactionTemplate(manager);
    }

    public Optional<Tirage> getTirage(int groupe) {
        return repository.findByGroupe(groupe);
    }

    public long getRestants() {
        return NOMBRE_SUJETS - repository.count();
    }

    /** Un groupe ne peut tirer qu'une fois ; un sujet ne peut être attribué qu'une fois.
     *  Le verrou garde la sélection + le COMMIT atomiques sur une instance de serveur.
     *  Les contraintes UNIQUE de H2 offrent une deuxième protection.
     */
    public Tirage tirerPourGroupe(int groupe) {
        if (groupe < 1 || groupe > NOMBRE_GROUPES) {
            throw new IllegalArgumentException("Groupe invalide : " + groupe);
        }
        synchronized (verrou) {
            return transaction.execute(status -> {
                Optional<Tirage> dejaTire = repository.findByGroupe(groupe);
                if (dejaTire.isPresent()) {
                    return dejaTire.get();
                }

                Set<Integer> utilises = new HashSet<>();
                for (Tirage tirage : repository.findAll()) {
                    utilises.add(tirage.getSujet());
                }
                List<Integer> disponibles = new ArrayList<>();
                for (int sujet = 1; sujet <= NOMBRE_SUJETS; sujet++) {
                    if (!utilises.contains(sujet)) {
                        disponibles.add(sujet);
                    }
                }
                if (disponibles.isEmpty()) {
                    throw new IllegalStateException("Tous les sujets ont été attribués.");
                }
                int sujetChoisi = disponibles.get(random.nextInt(disponibles.size()));
                return repository.saveAndFlush(new Tirage(groupe, sujetChoisi));
            });
        }
    }
}
