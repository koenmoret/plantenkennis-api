package nl.novi.plantenkennis.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FavorietRequestDto {
    private Long gebruikerId;
    private Long plantSoortId;
}
