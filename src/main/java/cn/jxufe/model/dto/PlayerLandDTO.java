package cn.jxufe.model.dto;

import cn.jxufe.model.enums.LandType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PlayerLandDTO {
    @Schema(description = "土地 ID")
    private Long id;

    @Schema(description = "土地种类")
    private LandType landType;

    @Schema(description = "所种植的种子")
    private SeedDTO seed;

    @Schema(description = "当前生长阶段")
    private GrowthCharacteristicDTO growthCharacteristic;

    @Schema(description = "是否生虫")
    @Column(name = "pest_infestation")
    private boolean pestInfestation;

    @Schema(description = "作物种植时间，单位：毫秒，0表示未种植")
    private Long plantingTime;

    @Schema(description = "可收获的果实数量")
    private int harvestableQuantity;

    @Schema(description = "作物处于第几季")
    private int growthSeason;
}
