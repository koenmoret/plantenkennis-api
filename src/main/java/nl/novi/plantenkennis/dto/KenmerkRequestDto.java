package nl.novi.plantenkennis.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KenmerkRequestDto {

    private String type;
    private String waarde;
}
