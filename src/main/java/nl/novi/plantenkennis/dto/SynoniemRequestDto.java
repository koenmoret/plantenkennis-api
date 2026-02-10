package nl.novi.plantenkennis.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SynoniemRequestDto {
    @NotBlank
    private String naam;
}
