package fr.kb.tp.tp1.config;

import fr.kb.tp.tp1.model.AuthToken;
import fr.kb.tp.tp1.model.Utilisateur;
import fr.kb.tp.tp1.repository.AuthTokenRepository;
import fr.kb.tp.tp1.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.hibernate.id.UUIDLogger_$logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Configuration
@EnableWebSecurity
public class SecurityConfig implements WebMvcConfigurer {

    public static final String AUTHORIZATION = "AUTHORIZATION";
    private final UserService userDetailsService;
    private final AuthTokenRepository authTokenRepository;

    public SecurityConfig(UserService userDetailsService, AuthTokenRepository authTokenRepository) {
        this.userDetailsService = userDetailsService;
        this.authTokenRepository = authTokenRepository;
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider getAuthProvider() {
        return new DaoAuthenticationProvider(userDetailsService) {
            @Override
            public Authentication authenticate(Authentication authentication) throws AuthenticationException {

                UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) authentication;

                String name = auth.getName();
                String password = auth.getCredentials().toString();

                UserDetails user = this.getUserDetailsService().loadUserByUsername(name);

                if (user == null || !bCryptPasswordEncoder().matches(password, user.getPassword())) {
                    throw new BadCredentialsException("Username/Password does not match for " + auth.getPrincipal());
                }

                return new UsernamePasswordAuthenticationToken(user, password, user.getAuthorities());
            }
        };
    }

    @Bean
    public AuthenticationSuccessHandler getAuthSuccessHandler() {
        ObjectMapper mapper = new ObjectMapper();
        return (request, response, authentication) -> {
            SecurityContextHolder.getContext().setAuthentication(authentication);
            final Utilisateur user = (Utilisateur) authentication.getPrincipal();

            UUID uuid = UUID.randomUUID();

            LocalDateTime expirationDate = LocalDateTime.now().plusMinutes(30);

            // CREATION DU TOKEN DE SESSION + INSERTION
            AuthToken authToken = new AuthToken()
                    .setId(uuid.toString())
                    .setUser(user)
                    .setExpirationDate(expirationDate);
            authTokenRepository.save(authToken);

            // CREATION D'UN COOKIE PORTANT L'ID DE SESSION
            Cookie authTokenCookie = new Cookie(AUTHORIZATION, uuid.toString());

            // DEPOSE DU COOKIE DANS LA REPONSE
            response.addCookie(authTokenCookie);

        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(ex -> ex.authenticationEntryPoint(new Http403ForbiddenEntryPoint()))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.DELETE, "/api/comments").hasAuthority("ROLE_ADMIN")
                        .requestMatchers("/api/**").authenticated()
                        .anyRequest().permitAll()
                )
                .addFilterBefore(
                        new TokenAuthenticationFilter(userDetailsService, authTokenRepository),
                        UsernamePasswordAuthenticationFilter.class
                )
                .authenticationProvider(getAuthProvider())
                .formLogin(form -> form
                        .loginPage("/login.html")
                        .loginProcessingUrl("/login")
                        .successHandler(getAuthSuccessHandler())
                        // Renvoie un 401 plutôt qu'une redirection vers login.html?error : un appel axios
                        // suivrait la redirection et croirait la connexion réussie.
                        .failureHandler((request, response, exception) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED))
                );

        return http.build();
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("forward:login.html");
        registry.addViewController("/tirage").setViewName("forward:tirages.html");
        registry.addViewController("/backOffice").setViewName("forward:admin.html");
    }
}
