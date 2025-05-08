package cn.jxufe.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlayerSeedDTO {
    private Long id;
    private Long playerId;
    private Long seedId;
    private Integer quantity;
}