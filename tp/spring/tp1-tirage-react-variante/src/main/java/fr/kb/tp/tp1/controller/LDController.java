package fr.kb.tp.tp1.controller;

import fr.kb.tp.tp1.model.*;
import fr.kb.tp.tp1.repository.GroupeRepository;
import fr.kb.tp.tp1.repository.GroupeSujetRepository;
import fr.kb.tp.tp1.repository.SujetRepository;
import fr.kb.tp.tp1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@RestController
public class LDController {


    private final GroupeRepository groupeRepository;

    private final SujetRepository sujetRepository;

    private final GroupeSujetRepository groupeSujetRepository;
    private final UserRepository userRepository;

    public LDController(GroupeRepository groupeRepository, SujetRepository sujetRepository, GroupeSujetRepository groupeSujetRepository, UserRepository userRepository) {
        this.groupeRepository = groupeRepository;
        this.sujetRepository = sujetRepository;
        this.groupeSujetRepository = groupeSujetRepository;
        this.userRepository = userRepository;
    }


    @GetMapping("/api/message")
    public String message() {
        return "ma chaine de caractere";
    }


    //end point de restitution de cette structure

    @GetMapping("/api/tirages")
    public List<GroupeSujet> tirages() {
        List<GroupeSujet> tirages = groupeSujetRepository.findAll();
        return tirages;
    }

    //endpoint de maj
    @PostMapping("/api/tirages")
    public void addTirage() {
        List<GroupeSujet> tirages = groupeSujetRepository.findAll();

        List<Sujet> disponibles = sujetRepository.findSujetLibre();

        int idGroupe = tirages.size() + 1 - disponibles.size();
        Optional<Groupe> byId = groupeRepository.findById(idGroupe);


        byId.ifPresent((groupe)->{
        Sujet sujet = disponibles.get(new Random().nextInt(disponibles.size()));
            GroupeSujet gS = new GroupeSujet();
            GroupeSujetId id = new GroupeSujetId();
            id.setGroupe(groupe);
            id.setSujet(sujet);

            gS.setId(id);
            groupeSujetRepository.save(gS);
        });

    }
}
