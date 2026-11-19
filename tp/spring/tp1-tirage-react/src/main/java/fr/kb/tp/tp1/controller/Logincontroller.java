package fr.kb.tp.tp1.controller;

import fr.kb.tp.tp1.model.Utilisateur;
import fr.kb.tp.tp1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class Logincontroller {


    @Autowired
    private UserRepository userRepository;

    @PostMapping("/api/login")
    public Utilisateur login(@RequestParam String login, @RequestParam("password") String pwd) {
        Optional<Utilisateur> byId = userRepository.findById(login);
        Utilisateur isConnected = byId
                .filter((user) -> user.getPwd().equals(pwd))
                .orElseThrow(()->new IllegalArgumentException());
        return isConnected;
    }
}
