package nl.novi.plantenkennis.config;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.util.*;
import java.util.stream.Collectors;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Value("${client-id}")
    private String clientId;

    @Value("${issuer-uri}")
    private String issuerUri;

    @Value("${audience:}")
    private String audience;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth

                        // ===== PUBLIC =====
                        .requestMatchers("/auth/public").permitAll()

                        .requestMatchers(HttpMethod.GET, "/plantsoorten/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/kenmerken/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/foto/**").permitAll()

                        // ===== AUTHENTICATED (ingelogd) =====
                        .requestMatchers("/auth/**").authenticated()

                        // ===== DEELNEMER =====
                        .requestMatchers(HttpMethod.GET, "/spelsessies/**").hasAuthority("ROLE_client_deelnemer")
                        .requestMatchers(HttpMethod.GET, "/gebruikers/**").hasAuthority("ROLE_client_deelnemer")

                        // ===== ADMIN =====
                        .requestMatchers(HttpMethod.POST, "/plantsoorten/**").hasAuthority("ROLE_client_admin")
                        .requestMatchers(HttpMethod.PUT, "/plantsoorten/**").hasAuthority("ROLE_client_admin")
                        .requestMatchers(HttpMethod.DELETE, "/plantsoorten/**").hasAuthority("ROLE_client_admin")

                        .requestMatchers(HttpMethod.POST, "/kenmerken/**").hasAuthority("ROLE_client_admin")
                        .requestMatchers(HttpMethod.DELETE, "/kenmerken/**").hasAuthority("ROLE_client_admin")

                        // alles wat je niet expliciet toestaat: alleen ingelogd
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex
                        .accessDeniedHandler(accessDeniedHandler())
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .decoder(jwtDecoder())
                                .jwtAuthenticationConverter(jwtAuthConverter())
                        )
                )
                .build();
    }

    /**
     * Converter: haalt client-rollen uit:
     * resource_access.<clientId>.
     */
    @Bean
    public JwtAuthenticationConverter jwtAuthConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(this::extractClientRoles);
        return converter;
    }

    private Collection<GrantedAuthority> extractClientRoles(Jwt jwt) {
        Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
        if (resourceAccess == null) return List.of();

        Object clientObj = resourceAccess.get(clientId);
        if (!(clientObj instanceof Map<?, ?> clientMap)) return List.of();

        Object rolesObj = clientMap.get("roles");
        if (!(rolesObj instanceof Collection<?> roles)) return List.of();

        return roles.stream()
                //ROLE_ wordt meegegeven in uit keycloak
                .map(String::valueOf)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        NimbusJwtDecoder decoder = JwtDecoders.fromIssuerLocation(issuerUri);

        OAuth2TokenValidator<Jwt> withIssuer = JwtValidators.createDefaultWithIssuer(issuerUri);

        if (audience != null && !audience.isBlank()) {
            OAuth2TokenValidator<Jwt> withAudience = new AudienceValidator(audience);
            decoder.setJwtValidator(new DelegatingOAuth2TokenValidator<>(withIssuer, withAudience));
        } else {
            decoder.setJwtValidator(withIssuer);
        }

        return decoder;
    }

    record AudienceValidator(String audience)
            implements OAuth2TokenValidator<Jwt> {

        @Override
        public OAuth2TokenValidatorResult validate(Jwt jwt) {
            Object audClaim = jwt.getClaims().get("aud");

            boolean ok = false;

            if (audClaim instanceof String audString) {
                ok = audience.equals(audString);
            } else if (audClaim instanceof Collection<?> audList) {
                ok = audList.stream()
                        .anyMatch(a -> audience.equals(String.valueOf(a)));
            }

            return ok
                    ? OAuth2TokenValidatorResult.success()
                    : OAuth2TokenValidatorResult.failure(
                    new OAuth2Error(
                            "invalid_token",
                            "Token audience ontbreekt of is ongeldig (verwacht: " + audience + ")",
                            null
                    )
            );
        }
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json");
            response.getWriter().write("""
            {
              "error": "forbidden",
              "message": "Je hebt geen toegang tot deze resource."
            }
        """);
        };
    }
}
