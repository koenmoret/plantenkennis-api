package nl.novi.plantenkennis.dto;

import java.time.LocalDateTime;

public class SpelsessieDto {

    private Long id;
    private String modus;
    private Integer level;
    private Integer score;
    private Integer duurSec;
    private Integer aantalCorrect;
    private Integer aantalPogingen;
    private LocalDateTime gespeeldOp;
    private Long gebruikerId;

    public SpelsessieDto(Long id,
                         String modus,
                         Integer level,
                         Integer score,
                         Integer duurSec,
                         Integer aantalCorrect,
                         Integer aantalPogingen,
                         LocalDateTime gespeeldOp,
                         Long gebruikerId) {
        this.id = id;
        this.modus = modus;
        this.level = level;
        this.score = score;
        this.duurSec = duurSec;
        this.aantalCorrect = aantalCorrect;
        this.aantalPogingen = aantalPogingen;
        this.gespeeldOp = gespeeldOp;
        this.gebruikerId = gebruikerId;
    }

    public Long getId() { return id; }
    public String getModus() { return modus; }
    public Integer getLevel() { return level; }
    public Integer getScore() { return score; }
    public Integer getDuurSec() { return duurSec; }
    public Integer getAantalCorrect() { return aantalCorrect; }
    public Integer getAantalPogingen() { return aantalPogingen; }
    public LocalDateTime getGespeeldOp() { return gespeeldOp; }
    public Long getGebruikerId() { return gebruikerId; }

    public void setId(Long id) { this.id = id; }
    public void setModus(String modus) { this.modus = modus; }
    public void setLevel(Integer level) { this.level = level; }
    public void setScore(Integer score) { this.score = score; }
    public void setDuurSec(Integer duurSec) { this.duurSec = duurSec; }
    public void setAantalCorrect(Integer aantalCorrect) { this.aantalCorrect = aantalCorrect; }
    public void setAantalPogingen(Integer aantalPogingen) { this.aantalPogingen = aantalPogingen; }
    public void setGespeeldOp(LocalDateTime gespeeldOp) { this.gespeeldOp = gespeeldOp; }
    public void setGebruikerId(Long gebruikerId) { this.gebruikerId = gebruikerId; }
}
