package pharmacymarketplace.cruduser.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Desabilita CSRF
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // APIs REST não guardam sessão
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/users/**").permitAll() // <-- Libera tudo sob /api/user
                        .requestMatchers("/h2-console/**").permitAll() // <-- Libera o console H2
                        .anyRequest().authenticated() // Pede login para qualquer OUTRA coisa (ex: /admin)
                )
                // Para o H2 console funcionar (ele usa frames)
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()));

        return http.build();
    }
}