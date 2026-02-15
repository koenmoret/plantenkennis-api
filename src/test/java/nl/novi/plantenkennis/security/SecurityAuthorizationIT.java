package nl.novi.plantenkennis.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityAuthorizationIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Admin endpoint vereist ROLE_client_admin authority")
    void authAdmin_requiresClientAdminRole() throws Exception {

        // Geen token → 401 Unauthorized
        mockMvc.perform(get("/auth/admin"))
                .andExpect(status().isUnauthorized());

        // Wel token maar GEEN juiste authority → 403 Forbidden
        mockMvc.perform(get("/auth/admin")
                        .with(jwt()))
                .andExpect(status().isForbidden());

        // Wel token + ROLE_client_admin → 200 OK
        mockMvc.perform(get("/auth/admin")
                        .with(jwt().authorities(
                                new SimpleGrantedAuthority("ROLE_client_admin")
                        )))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andExpect(jsonPath("$.ok").value(true));
    }
}
