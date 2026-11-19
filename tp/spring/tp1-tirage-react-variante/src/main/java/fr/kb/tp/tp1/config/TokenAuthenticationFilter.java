package fr.kb.tp.tp1.config;

import com.serli.security.model.User;
import fr.kb.tp.tp1.repository.AuthTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
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

public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final JwtConfig jwtConfig;
    private final UserDetailsService userService;
    private final Logger log = LoggerFactory.getLogger(JwtTokenAuthenticationFilter.class);
    private final AuthTokenRepository authTokenRepository;

    public TokenAuthenticationFilter(JwtConfig jwtConfig, UserDetailsService userService) {
        this.jwtConfig = jwtConfig;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {


        // recuperation du cookie depuis la requete
        Arrays.stream(request.getCookies()).filter(c->c.getName().equals(SecurityConfig.AUTHORIZATION)).findFirst().orElseThrow(()->new IllegalStateException());


        //test de validite du cookie -> verification de l'existance enbase

        String value = cookie.getValue();
        AuthToken authToken = authTokenRepository.findById(value).orElseThrow(()->new IllegalStateException());




        try {
            //recuperation de l'utilisateur si session valide


            String username = claims.getSubject();
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                User userDetails = (User) userService.loadUserByUsername(username);

                if (userDetails != null) {
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    log.info("{} -> authenticated user {} (expiration {})", request.getRequestURI(), username, claims.getExpiration());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (Exception e) {
            SecurityContextHolder.clearContext();
        }

        chain.doFilter(request, response);
    }

}
