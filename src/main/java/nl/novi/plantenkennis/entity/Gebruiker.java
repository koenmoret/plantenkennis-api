package nl.novi.plantenkennis.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "gebruikers")
public class Gebruiker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String naam;

    @Column(nullable = false, unique = true, length = 190)
    private String email;

    /**
     * Let op: security komt pas in fase 3.
     */
    @Column(nullable = true, length = 255)
    private String wachtwoordHash;

    @OneToMany(mappedBy = "gebruiker", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Spelsessie> spelsessies = new ArrayList<>();

    // === Constructors ===
    public Gebruiker() {}

    public Gebruiker(String naam, String email) {
        this.naam = naam;
        this.email = email;
    }

    // === Helper methods (belangrijk voor consistente relaties) ===
    public void addSpelsessie(Spelsessie spelsessie) {
        spelsessies.add(spelsessie);
        spelsessie.setGebruiker(this);
    }

    public void removeSpelsessie(Spelsessie spelsessie) {
        spelsessies.remove(spelsessie);
        spelsessie.setGebruiker(null);
    }

    // === Getters/Setters ===
    public Long getId() {
        return id;
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

    public String getWachtwoordHash() {
        return wachtwoordHash;
    }

    public void setWachtwoordHash(String wachtwoordHash) {
        this.wachtwoordHash = wachtwoordHash;
    }

    public List<Spelsessie> getSpelsessies() {
        return spelsessies;
    }

    // === equals/hashCode op id (JPA-safe) ===
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
