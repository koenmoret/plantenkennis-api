package nl.novi.plantenkennis.dto;

import lombok.*;
import jakarta.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KenmerkRequestDto {

    @NotBlank
    private String type;

    @NotBlank
    private String waarde;
}
