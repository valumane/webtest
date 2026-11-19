package fr.kb.tp.tp1;

import fr.kb.tp.tp1.model.*;
import fr.kb.tp.tp1.repository.GroupeRepository;
import fr.kb.tp.tp1.repository.GroupeSujetRepository;
import fr.kb.tp.tp1.repository.SujetRepository;
import fr.kb.tp.tp1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

public class Initializer {


    private final GroupeRepository groupeRepository;
    private final SujetRepository sujetRepository;
    private final GroupeSujetRepository groupeSujetRepository;
    private final UserRepository userRepository;
    //Propriete de stockage

    {

    }

    public Initializer(GroupeRepository groupeRepository, SujetRepository sujetRepository, GroupeSujetRepository groupeSujetRepository, UserRepository userRepository) {

        this.groupeRepository = groupeRepository;
        this.sujetRepository = sujetRepository;
        this.groupeSujetRepository = groupeSujetRepository;
        this.userRepository = userRepository;


        Groupe groupe1 = groupeRepository.save(new Groupe(1, "emery-adam-pigeaud-chesse-loup-fabre-enzo"));
        Groupe groupe2 = groupeRepository.save(new Groupe(2, "lucas-vadé-yvan-hamel"));
        Groupe groupe3 = groupeRepository.save(new Groupe(3, "tom-proust-sven-carreras-esterlingot-ibraguim-karsamov"));
        Groupe groupe4 = groupeRepository.save(new Groupe(4, "evan-ribouleau-stéphane-castan-melodie-fleury-antoine-giret"));
        Groupe groupe5 = groupeRepository.save(new Groupe(5, "charpentier-tom-krizmanic-mathis-mirghane-mohamed-kamardine"));
        Groupe groupe6 = groupeRepository.save(new Groupe(6, "zang-jean-ley-thibault-damien-neupont-thomas"));
        Groupe groupe7 = groupeRepository.save(new Groupe(7, "zoé-arthapignet-nicolas-itey-mathis-laronde-owen-abril"));
        Groupe groupe8 = groupeRepository.save(new Groupe(8, "sasso-etien-wapae-emile-clio-kendrick"));
        Groupe groupe9 = groupeRepository.save(new Groupe(9, "matteo-raffin-chloé-lemaire-paul-ourliac"));
        Groupe groupe10 = groupeRepository.save(new Groupe(10, "honcharenko-dmytro-rybak-vladyslav-stremsky-dmytro"));
        Groupe groupe11 = groupeRepository.save(new Groupe(11, "melissa-amari-zaynab-benchfeg-azemour-wiame-bouziane"));
        Groupe groupe12 = groupeRepository.save(new Groupe(12, "charbel-abriny-pierre-abou-faliniaina-ranaivoson"));
        Groupe groupe13 = groupeRepository.save(new Groupe(13, "koné-moussa-giordhy-gantsie-alae-eddine-el-basbassi"));
        Groupe groupe14 = groupeRepository.save(new Groupe(14, "marc-michoulier-tristan-hartmann-baudry"));
        Groupe groupe15 = groupeRepository.save(new Groupe(15, "amine-aroug-et-elouan-moal"));
        Groupe groupe16 = groupeRepository.save(new Groupe(16, "wama-brayan-djoudi-khaled-manil-larbi"));


        Sujet sujet1 = sujetRepository.save(new Sujet(1, "Covoiturage de la promo"));
        Sujet sujet2 = sujetRepository.save(new Sujet(2, "Réservation de salles de projet"));
        Sujet sujet3 = sujetRepository.save(new Sujet(3, "Permanences du BDE"));
        Sujet sujet4 = sujetRepository.save(new Sujet(4, "Tournoi e-sport inter-groupes"));
        Sujet sujet5 = sujetRepository.save(new Sujet(5, "Ludothèque de la promo"));
        Sujet sujet6 = sujetRepository.save(new Sujet(6, "Suivi des candidatures de stage"));
        Sujet sujet7 = sujetRepository.save(new Sujet(7, "Annuaire de compétences"));
        Sujet sujet8 = sujetRepository.save(new Sujet(8, "Défi sportif de la promo"));
        Sujet sujet9 = sujetRepository.save(new Sujet(9, "Commandes groupées de repas"));
        Sujet sujet10 = sujetRepository.save(new Sujet(10, "Tutorat entre étudiants"));
        Sujet sujet11 = sujetRepository.save(new Sujet(11, "Bons plans de la ville"));
        Sujet sujet12 = sujetRepository.save(new Sujet(12, "Soirées ciné de la promo"));
        Sujet sujet13 = sujetRepository.save(new Sujet(13, "Suivi des rendus et deadlines"));
        Sujet sujet14 = sujetRepository.save(new Sujet(14, "Organisation de sorties"));
        Sujet sujet15 = sujetRepository.save(new Sujet(15, "Troc et vente entre étudiants"));
        Sujet sujet16 = sujetRepository.save(new Sujet(16, "Support d'aide de la promo"));
        Sujet sujet17 = sujetRepository.save(new Sujet(17, "Cagnotte du voyage de promo"));
        Sujet sujet18 = sujetRepository.save(new Sujet(18, "Défi de code quotidien"));
        Sujet sujet19 = sujetRepository.save(new Sujet(19, "Playlist collaborative"));
        Sujet sujet20 = sujetRepository.save(new Sujet(20, "Émargement et absences"));


        //INITIALISATION DE LA STRUCTURE
        GroupeSujetId gS1 = new GroupeSujetId(groupe8, sujet1);
        GroupeSujet gr = new GroupeSujet();
        gr.setId(gS1);
        groupeSujetRepository.save(gr);

        GroupeSujetId gS2 = new GroupeSujetId(groupe13, sujet2);
        GroupeSujet gr2 = new GroupeSujet();
        gr2.setId(gS2);
        groupeSujetRepository.save(gr2);

        GroupeSujetId gS3 = new GroupeSujetId(groupe5, sujet3);
        GroupeSujet gr3 = new GroupeSujet();
        gr3.setId(gS3);
        groupeSujetRepository.save(gr3);

        GroupeSujetId gS5 = new GroupeSujetId(groupe6, sujet5);
        GroupeSujet gr5 = new GroupeSujet();
        gr5.setId(gS5);
        groupeSujetRepository.save(gr5);

        GroupeSujetId gS7 = new GroupeSujetId(groupe10, sujet7);
        GroupeSujet gr7 = new GroupeSujet();
        gr7.setId(gS7);
        groupeSujetRepository.save(gr7);

        GroupeSujetId gS8 = new GroupeSujetId(groupe11, sujet8);
        GroupeSujet gr8 = new GroupeSujet();
        gr8.setId(gS8);
        groupeSujetRepository.save(gr8);

        GroupeSujetId gS9 = new GroupeSujetId(groupe9, sujet9);
        GroupeSujet gr9 = new GroupeSujet();
        gr9.setId(gS9);
        groupeSujetRepository.save(gr9);

        GroupeSujetId gS10 = new GroupeSujetId(groupe16, sujet10);
        GroupeSujet gr10 = new GroupeSujet();
        gr10.setId(gS10);
        groupeSujetRepository.save(gr10);

        GroupeSujetId gS11 = new GroupeSujetId(groupe12, sujet11);
        GroupeSujet gr11 = new GroupeSujet();
        gr11.setId(gS11);
        groupeSujetRepository.save(gr11);

        GroupeSujetId gS13 = new GroupeSujetId(groupe15, sujet13);
        GroupeSujet gr13 = new GroupeSujet();
        gr13.setId(gS13);
        groupeSujetRepository.save(gr13);

        GroupeSujetId gS14 = new GroupeSujetId(groupe1, sujet14);
        GroupeSujet gr14 = new GroupeSujet();
        gr14.setId(gS14);
        groupeSujetRepository.save(gr14);

        GroupeSujetId gS15 = new GroupeSujetId(groupe3, sujet15);
        GroupeSujet gr15 = new GroupeSujet();
        gr15.setId(gS15);
        groupeSujetRepository.save(gr15);


        GroupeSujetId gS17 = new GroupeSujetId(groupe14, sujet17);
        GroupeSujet gr17 = new GroupeSujet();
        gr17.setId(gS17);
        groupeSujetRepository.save(gr17);

        GroupeSujetId gS18 = new GroupeSujetId(groupe7, sujet18);
        GroupeSujet gr18 = new GroupeSujet();
        gr18.setId(gS18);
        groupeSujetRepository.save(gr18);

        GroupeSujetId gS19 = new GroupeSujetId(groupe4, sujet19);
        GroupeSujet gr19 = new GroupeSujet();
        gr19.setId(gS19);
        groupeSujetRepository.save(gr19);

        GroupeSujetId gS20 = new GroupeSujetId(groupe2, sujet20);
        GroupeSujet gr20 = new GroupeSujet();
        gr20.setId(gS20);
        groupeSujetRepository.save(gr20);


        for(int i = 0; i<4; i++){
            Utilisateur user = new Utilisateur();
            user.setLogin("test"+i);
            user.setPwd("test"+i);
            userRepository.save(user);

        }

    }
}
