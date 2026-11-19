package fr.kb.tp.tp1.controller;

import fr.kb.tp.tp1.modele.Content;
import fr.kb.tp.tp1.modele.AppUser;
import fr.kb.tp.tp1.repository.AppUserRepository;
import java.security.Principal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PageController {
    private final Content content;
    private final AppUserRepository users;

    public PageController(Content content, AppUserRepository users) {
        this.content = content;
        this.users = users;
    }

    @GetMapping("/")
    public String login(Principal principal) {
        return principal == null ? "login" : "redirect:/tirage";
    }

    @GetMapping("/tirage")
    public String pageTirage(Principal principal, Model model) {
        AppUser user = users.findByUsername(principal.getName()).orElseThrow();
        model.addAttribute("username", user.getUsername());
        model.addAttribute("groupe", user.getGroupe());
        model.addAttribute("tirage", content.getTirage(user.getGroupe()).orElse(null));
        model.addAttribute("restants", content.getRestants());
        return "tirage";
    }

    @PostMapping("/tirage")
    public String faireTirage(Principal principal) {
        AppUser user = users.findByUsername(principal.getName()).orElseThrow();
        content.tirerPourGroupe(user.getGroupe());
        return "redirect:/tirage";
    }
}
