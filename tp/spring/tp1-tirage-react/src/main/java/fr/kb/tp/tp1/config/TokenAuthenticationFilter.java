package fr.kb.tp.tp1.config;

import com.serli.security.model.User;
import fr.kb.tp.tp1.model.AuthToken;
import fr.kb.tp.tp1.model.Utilisateur;
import fr.kb.tp.tp1.repository.AuthTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final UserDetailsService userService;
    private final AuthTokenRepository authTokenRepository;

    private final Logger log = LoggerFactory.getLogger(TokenAuthenticationFilter.class);

    public TokenAuthenticationFilter(UserDetailsService userService, AuthTokenRepository authTokenRepository) {
        this.userService = userService;
        this.authTokenRepository = authTokenRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

       // RECUPERATION DU COOKIE DEPUIS LA REQUETE
        Cookie cookie = Arrays.stream(request.getCookies())
                .filter(c -> c.getName().equals(SecurityConfig.AUTHORIZATION))
                .findFirst()
                //EXCEPTION FORBIDDEN
                .orElseThrow(() -> new IllegalStateException());


        // TEST DE VALIDITE DU COOKIE -> VERIFICATION DE L'EXISTANCE ENBASE
        String value = cookie.getValue();
        AuthToken authToken = authTokenRepository.findById(value)
                //EXCEPTION FORBIDDEN
                .orElseThrow(() -> new IllegalStateException());



        try {

            // RECUPERATION DE L4UTILISATEUR SI SESSION VALIDE
            Utilisateur userDetails = authToken.getUser();
            if (userDetails != null && SecurityContextHolder.getContext().getAuthentication() == null) {

//                Utilisateur userDetails = (Utilisateur) userService.loadUserByUsername(username);

                if (userDetails != null) {
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    log.info("{} -> authenticated user {} (expiration {})", request.getRequestURI(), userDetails.getUsername(), authToken.getExpirationDate());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (Exception e) {
            SecurityContextHolder.clearContext();
        }

        chain.doFilter(request, response);
    }

}
