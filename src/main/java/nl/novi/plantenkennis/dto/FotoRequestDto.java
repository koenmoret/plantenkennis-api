package nl.novi.plantenkennis.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FotoRequestDto {
    private String url;
    private String fotograaf;
    private String licentie;
    private String altTekst;
    private Boolean hoofdfoto;
    private String bron;
}
