package nl.novi.plantenkennis.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "gebruikers")
public class Gebruiker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "keycloak_subject", nullable = false, unique = true, length = 100)
    private String keycloakSubject;

    @Column(nullable = false, length = 120)
    private String naam;

    @Column(nullable = false, unique = true, length = 190)
    private String email;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = true, length = 255)
    private String wachtwoordHash;

    @OneToMany(mappedBy = "gebruiker",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Spelsessie> spelsessies = new ArrayList<>();

    // ===== Constructors =====

    public Gebruiker() {}

    public Gebruiker(String keycloakSubject, String naam, String email) {
        this.keycloakSubject = keycloakSubject;
        this.naam = naam;
        this.email = email;
    }

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    // ===== Getters / Setters =====

    public Long getId() {
        return id;
    }

    public String getKeycloakSubject() {
        return keycloakSubject;
    }

    public void setKeycloakSubject(String keycloakSubject) {
        this.keycloakSubject = keycloakSubject;
    }

    public String getNaam() {
        return naam;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getWachtwoordHash() {
        return wachtwoordHash;
    }

    public void setWachtwoordHash(String wachtwoordHash) {
        this.wachtwoordHash = wachtwoordHash;
    }

    public List<Spelsessie> getSpelsessies() {
        return spelsessies;
    }

    // ===== equals/hashCode =====

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Gebruiker)) return false;
        Gebruiker that = (Gebruiker) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
