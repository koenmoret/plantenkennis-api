package nl.novi.plantenkennis.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FavorietRequestDto {
    private Long gebruikerId;
    private Long plantSoortId;
}
