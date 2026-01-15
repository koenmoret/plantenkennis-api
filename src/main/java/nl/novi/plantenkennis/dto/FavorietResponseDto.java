package nl.novi.plantenkennis.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FavorietResponseDto {
    private Long id;
    private Long gebruikerId;
    private Long plantSoortId;
}
