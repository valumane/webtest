package fr.kb.tp.tp1.config;

import com.serli.security.model.AuthToken;
import com.serli.security.model.User;
import com.serli.security.service.UserService;
import fr.kb.tp.tp1.model.AuthToken;
import fr.kb.tp.tp1.repository.AuthTokenRepository;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletResponse;
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

    private final UserService userDetailsService;
    private final AuthTokenRepository authTokenRepository;
    private final JwtConfig jwtConfig;

    public SecurityConfig(UserService userDetailsService, JwtConfig jwtConfig) {
        this.userDetailsService = userDetailsService;
        this.jwtConfig = jwtConfig;
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
            final User user = (User) authentication.getPrincipal();


            UUID uuid = UUID.randomUUID();

            LocalDateTime expirationDate = LocalDateTime.now().plusMinutes(30)

                    //-----------------------
            //creation du token de session + insertion
            AuthToken authToken = new AuthToken()
                    .setId(uuid.toString()).setUser(user).setExpirationDate(expirationDate);

            authTokenRepository.save(authToken);


            //creation d'un cookie portant l'id de session
            Cookie authTokenCookie = new Cookie(AUTHORIZATION,uuid.toString());


            //depose du cookie dans la reponse
            response.addCookie(authTokenCookie);

            //--------------------

            String token = Jwts.builder()
                    .subject(user.getUsername())
                    .issuedAt(new Date())
                    .expiration(new Date(System.currentTimeMillis() + jwtConfig.getExpiration()))
                    .signWith(jwtConfig.signingKey())
                    .compact();

            response.setContentType("application/json;charset=UTF-8");
            mapper.writeValue(response.getWriter(), new AuthToken(token));
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
                        new JwtTokenAuthenticationFilter(userDetailsService, authTokenRepository),
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
        registry.addViewController("/livredor").setViewName("forward:livredor.html");
    }
}
