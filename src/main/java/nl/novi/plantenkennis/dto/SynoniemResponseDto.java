package nl.novi.plantenkennis.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SynoniemResponseDto {
    private Long id;
    private String naam;
}
