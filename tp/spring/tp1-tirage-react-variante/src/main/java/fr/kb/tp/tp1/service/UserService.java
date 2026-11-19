package fr.kb.tp.tp1.service;

import fr.kb.tp.tp1.model.Utilisateur;
import fr.kb.tp.tp1.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Objects.requireNonNull(username);
        Utilisateur user = userRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return user;
    }

}
