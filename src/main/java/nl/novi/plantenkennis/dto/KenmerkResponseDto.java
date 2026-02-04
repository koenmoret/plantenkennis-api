package nl.novi.plantenkennis.dto;

import lombok.*;
import jakarta.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KenmerkResponseDto {

    private Long id;

    @NotBlank(message = "Type is verplicht")
    private String type;

    @NotBlank(message = "Waarde is verplicht")
    private String waarde;
}
