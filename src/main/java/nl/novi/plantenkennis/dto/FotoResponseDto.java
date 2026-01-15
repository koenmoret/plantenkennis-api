package nl.novi.plantenkennis.dto;

import lombok.*;

@Getter
@Builder
public class FotoResponseDto {
    private Long id;
    private String url;
    private String fotograaf;
    private String licentie;
    private String altTekst;
    private Boolean hoofdfoto;
    private String bron;
}
