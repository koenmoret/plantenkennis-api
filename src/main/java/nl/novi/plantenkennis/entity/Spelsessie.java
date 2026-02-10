package nl.novi.plantenkennis.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "spelsessies")
public class Spelsessie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String modus;

    @Column(nullable = false)
    private String level;

    @Column(nullable = false)
    private int score;

    @Column(nullable = false)
    private int duurSec;

    @Column(nullable = false)
    private int aantalCorrect;

    @Column(nullable = false)
    private int aantalPogingen;

    @Column(nullable = false)
    private LocalDateTime gespeeldOp;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "gebruiker_id", nullable = false)
    @JsonIgnore
    private Gebruiker gebruiker;

    // === Constructors ===
    public Spelsessie() {}

    public Spelsessie(String modus, String level, int score, int duurSec, int aantalCorrect, int aantalPogingen) {
        this.modus = modus;
        this.level = level;
        this.score = score;
        this.duurSec = duurSec;
        this.aantalCorrect = aantalCorrect;
        this.aantalPogingen = aantalPogingen;
        this.gespeeldOp = LocalDateTime.now();
    }

    @PrePersist
    protected void onCreate() {
        if (gespeeldOp == null) {
            gespeeldOp = LocalDateTime.now();
        }
    }

    // === Getters/Setters ===
    public Long getId() {
        return id;
    }

    public String getModus() {
        return modus;
    }

    public void setModus(String modus) {
        this.modus = modus;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getDuurSec() {
        return duurSec;
    }

    public void setDuurSec(int duurSec) {
        this.duurSec = duurSec;
    }

    public int getAantalCorrect() {
        return aantalCorrect;
    }

    public void setAantalCorrect(int aantalCorrect) {
        this.aantalCorrect = aantalCorrect;
    }

    public int getAantalPogingen() {
        return aantalPogingen;
    }

    public void setAantalPogingen(int aantalPogingen) {
        this.aantalPogingen = aantalPogingen;
    }

    public LocalDateTime getGespeeldOp() {
        return gespeeldOp;
    }

    public void setGespeeldOp(LocalDateTime gespeeldOp) {
        this.gespeeldOp = gespeeldOp;
    }

    public Gebruiker getGebruiker() {
        return gebruiker;
    }

    public void setGebruiker(Gebruiker gebruiker) {
        this.gebruiker = gebruiker;
    }

    // === equals/hashCode op id ===
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Spelsessie)) return false;
        Spelsessie that = (Spelsessie) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
