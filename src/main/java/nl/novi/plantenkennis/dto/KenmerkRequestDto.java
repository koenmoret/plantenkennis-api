package nl.novi.plantenkennis.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KenmerkRequestDto {

    @NotBlank(message = "Type is verplicht")
    private String type;

    @NotBlank(message = "Waarde is verplicht")
    private String waarde;
}
