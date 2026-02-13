package nl.novi.plantenkennis.entity;

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

    @Column(nullable = false, length = 80)
    private String modus;

    // schema.sql: level INTEGER
    private Integer level;

    private Integer score;

    @Column(name = "duur_sec")
    private Integer duurSec;

    @Column(name = "aantal_correct")
    private Integer aantalCorrect;

    @Column(name = "aantal_pogingen")
    private Integer aantalPogingen;

    @Column(name = "gespeeld_op", nullable = false)
    private LocalDateTime gespeeldOp;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "gebruiker_id", nullable = false)
    @JsonIgnore
    private Gebruiker gebruiker;

    // === Constructors ===
    public Spelsessie(String modus, Integer level, Integer score, Integer duurSec, Integer aantalCorrect, Integer aantalPogingen) {
        this.modus = modus;
        this.level = level;
        this.score = score;
        this.duurSec = duurSec;
        this.aantalCorrect = aantalCorrect;
        this.aantalPogingen = aantalPogingen;
        this.gespeeldOp = LocalDateTime.now();
    }

    public Spelsessie() {}

    @PrePersist
    protected void onCreate() {
        if (gespeeldOp == null) {
            gespeeldOp = LocalDateTime.now();
        }
    }

    // === Getters/Setters ===
    public Long getId() { return id; }

    public String getModus() { return modus; }
    public void setModus(String modus) { this.modus = modus; }

    public Integer getLevel() { return level; }
    public void setLevel(Integer level) { this.level = level; }

    public Integer getScore() { return score; }
    public void setScore(Integer score) { this.score = score; }

    public Integer getDuurSec() { return duurSec; }
    public void setDuurSec(Integer duurSec) { this.duurSec = duurSec; }

    public Integer getAantalCorrect() { return aantalCorrect; }
    public void setAantalCorrect(Integer aantalCorrect) { this.aantalCorrect = aantalCorrect; }

    public Integer getAantalPogingen() { return aantalPogingen; }
    public void setAantalPogingen(Integer aantalPogingen) { this.aantalPogingen = aantalPogingen; }

    public LocalDateTime getGespeeldOp() { return gespeeldOp; }
    public void setGespeeldOp(LocalDateTime gespeeldOp) { this.gespeeldOp = gespeeldOp; }

    public Gebruiker getGebruiker() { return gebruiker; }
    public void setGebruiker(Gebruiker gebruiker) { this.gebruiker = gebruiker; }

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
