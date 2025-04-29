package cn.jxufe.model.dto;

import cn.jxufe.model.enums.LandType;
import cn.jxufe.model.enums.SeedType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "种子数据传输对象")
public class SeedDTO {

    @Schema(description = "种子 ID", example = "1")
    private Long seedId;

    @NotBlank(message = "种子名称不能为空")
    @Schema(description = "种子名称", example = "胡萝卜")
    private String seedName;

    @NotNull(message = "生长季节数不能为空")
    @Min(value = 1, message = "生长季节数必须大于0")
    @Schema(description = "生长季节数", example = "3")
    private Integer growthSeasonCount;

    @NotNull(message = "种子等级不能为空")
    @Min(value = 1, message = "种子等级必须大于0")
    @Schema(description = "种子等级", example = "2")
    private Integer seedLevel;

    @NotNull(message = "种子类型不能为空")
    @Schema(description = "种子类型", example = "COMMON")
    private SeedType seedType;

    @NotNull(message = "经验值不能为空")
    @Min(value = 0, message = "经验值不能为负")
    @Schema(description = "成熟后收获经验值", example = "100")
    private Integer experience;

    @NotNull(message = "积分不能为空")
    @Min(value = 0, message = "积分不能为负")
    @Schema(description = "成熟后收获积分", example = "50")
    private Integer points;

    @NotNull(message = "收获产量不能为空")
    @Min(value = 1, message = "收获产量必须大于0")
    @Schema(description = "成熟后收获产量", example = "5")
    private Integer harvestYield;

    @NotNull(message = "每季生长时间不能为空")
    @Min(value = 1, message = "每季生长时间必须大于0")
    @Schema(description = "每季生长时间，单位为秒", example = "3600")
    private Integer growthTimePerSeason;

    @NotNull(message = "种子购买价格不能为空")
    @Min(value = 0, message = "种子购买价格不能为负")
    @Schema(description = "种子购买价格", example = "25.0")
    private Double seedPurchasePrice;

    @NotNull(message = "水果单价不能为空")
    @Min(value = 0, message = "水果单价不能为负")
    @Schema(description = "每单位水果出售价格", example = "10.0")
    private Double fruitPricePerUnit;

    @NotNull(message = "种植土地需求不能为空")
    @Schema(description = "种植土地需求", example = "YELLOW")
    private LandType landRequirement;

    @Schema(description = "种植提示", example = "保持土壤湿润，定期除草")
    private String plantingTip;
}