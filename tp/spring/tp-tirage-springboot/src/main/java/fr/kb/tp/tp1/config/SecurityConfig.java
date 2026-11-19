package fr.kb.tp.tp1.config;

import fr.kb.tp.tp1.repository.AppUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.security.autoconfigure.web.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import fr.kb.tp.tp1.modele.AppUser;

@Configuration
public class SecurityConfig {
    /** Configuration distincte pour la console H2 (développement local uniquement). */
    @Bean
    @Order(1)
    SecurityFilterChain h2ConsoleSecurityFilterChain(HttpSecurity http) throws Exception {
        http.securityMatcher(PathRequest.toH2Console())
            .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
            .csrf(AbstractHttpConfigurer::disable)
            .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()));
        return http.build();
    }

    /** Toutes les pages et API de tirage demandent une session authentifiée. */
    @Bean
    @Order(2)
    SecurityFilterChain appSecurityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/login", "/css/**", "/favicon.ico").permitAll()
                .anyRequest().authenticated())
            .formLogin(form -> form
                .loginPage("/")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/tirage", true)
                .failureUrl("/?error")
                .permitAll())
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/?logout")
                .permitAll());
        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }

    @Bean
    UserDetailsService userDetailsService(AppUserRepository repository) {
        return username -> repository.findByUsername(username)
            .map(user -> User.withUsername(user.getUsername())
                .password(user.getPasswordHash())
                .roles("USER")
                .build())
            .orElseThrow(() -> new UsernameNotFoundException("Utilisateur inconnu"));
    }

    /** Comptes de démonstration. À désactiver avant toute mise en production. */
    @Bean
    CommandLineRunner initAccounts(AppUserRepository repository, PasswordEncoder encoder) {
        return args -> {
            for (int numero = 1; numero <= 12; numero++) {
                String username = "groupe" + numero;
                if (repository.findByUsername(username).isEmpty()) {
                    repository.save(new AppUser(username, encoder.encode("demo123"), numero));
                }
            }
        };
    }
}
