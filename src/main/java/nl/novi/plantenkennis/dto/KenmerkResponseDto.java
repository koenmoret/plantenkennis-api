package nl.novi.plantenkennis.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KenmerkResponseDto {

    private Long id;
    private String type;
    private String waarde;
}
