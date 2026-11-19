package fr.kb.tp.tp1.controller;

import fr.kb.tp.tp1.modele.AppUser;
import fr.kb.tp.tp1.modele.Content;
import fr.kb.tp.tp1.modele.Tirage;
import fr.kb.tp.tp1.repository.AppUserRepository;
import java.security.Principal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LDController {
    private final Content content;
    private final AppUserRepository users;

    public LDController(Content content, AppUserRepository users) {
        this.content = content;
        this.users = users;
    }

    @GetMapping("/api/message")
    public String message() { return "ma chaine de caractere"; }

    /** État du groupe connecté, sans dévoiler les résultats des autres groupes. */
    @GetMapping("/api/tirage")
    public TirageEtat getTirage(Principal principal) {
        AppUser user = getCurrentUser(principal);
        Integer sujet = content.getTirage(user.getGroupe()).map(Tirage::getSujet).orElse(null);
        return new TirageEtat(user.getGroupe(), sujet, content.getRestants());
    }

    /** Le POST JSON nécessite un jeton CSRF. Le formulaire HTML le fournit automatiquement. */
    @PostMapping("/api/tirage")
    public TirageEtat tirer(Principal principal) {
        AppUser user = getCurrentUser(principal);
        Tirage tirage = content.tirerPourGroupe(user.getGroupe());
        return new TirageEtat(user.getGroupe(), tirage.getSujet(), content.getRestants());
    }

    private AppUser getCurrentUser(Principal principal) {
        return users.findByUsername(principal.getName()).orElseThrow();
    }

    public record TirageEtat(int groupe, Integer sujet, long restants) { }
}
