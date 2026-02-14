package nl.novi.plantenkennis.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class SecurityController {

    @GetMapping("/public")
    public Map<String, Object> publicEndpoint() {
        return Map.of(
                "ok", true,
                "message", "Public endpoint: geen token nodig."
        );
    }

    @GetMapping("/me")
    public Map<String, Object> me(Authentication authentication,
                                  @AuthenticationPrincipal Jwt jwt) {

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("authenticated", authentication != null && authentication.isAuthenticated());
        result.put("name", authentication != null ? authentication.getName() : null);

        // Handige JWT velden
        if (jwt != null) {
            result.put("subject", jwt.getSubject());
            result.put("issuer", jwt.getIssuer() != null ? jwt.getIssuer().toString() : null);
            result.put("preferred_username", jwt.getClaimAsString("preferred_username"));
            result.put("email", jwt.getClaimAsString("email"));
            result.put("claims", jwt.getClaims()); // voor debug, later evt. weghalen
        }

        // Authorities die Spring ziet
        List<String> authorities = authentication == null ? List.of()
                : authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .sorted()
                .toList();

        result.put("authorities", authorities);

        return result;
    }

    @GetMapping("/roles")
    public Map<String, Object> roles(Authentication authentication) {
        List<String> authorities = authentication == null ? List.of()
                : authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .sorted()
                .toList();

        return Map.of(
                "authorities", authorities
        );
    }

    @GetMapping("/admin")
    public Map<String, Object> adminOnly() {
        // Deze endpoint moet je in SecurityConfig afschermen met hasAuthority(...)
        return Map.of(
                "ok", true,
                "message", "Admin endpoint: je hebt de juiste rol."
        );
    }
}
