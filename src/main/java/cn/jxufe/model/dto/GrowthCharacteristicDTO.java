package cn.jxufe.model.dto;

import cn.jxufe.model.enums.CropStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "生长特性数据传输对象")
public class GrowthCharacteristicDTO {

    @Schema(description = "生长特性 ID", example = "1")
    private Long id;

    @Schema(description = "生长阶段图片路径", example = "growth-characteristics/carrot_stage1.jpg")
    private String imagePath;

    @NotNull(message = "生长阶段不能为空")
    @Min(value = 1, message = "生长阶段必须大于0")
    @Schema(description = "生长阶段", example = "1")
    private Integer growthStage;

    @Schema(description = "生长阶段标题", example = "幼苗期")
    private String growthStageTitle;

    @NotNull(message = "阶段生长时间不能为空")
    @Min(value = 1, message = "阶段生长时间必须大于0")
    @Schema(description = "阶段生长时间（秒）", example = "1200")
    private Integer stageGrowthTime;

    @NotNull(message = "虫害发生概率不能为空")
    @Min(value = 0, message = "虫害发生概率不能小于0")
    @Max(value = 1, message = "虫害发生概率不能大于1")
    @Schema(description = "虫害发生概率", example = "0.15")
    private Double pestInfestationProbability;

    @Schema(description = "图片宽度", example = "120")
    private Integer imageWidth;

    @Schema(description = "图片高度", example = "150")
    private Integer imageHeight;

    @Schema(description = "图片偏移量 X", example = "10")
    private Integer imageOffsetX;

    @Schema(description = "图片偏移量 Y", example = "5")
    private Integer imageOffsetY;

    @NotNull(message = "作物状态不能为空")
    @Schema(description = "作物状态", example = "GROWING")
    private CropStatus cropStatus;

    @NotNull(message = "种子ID不能为空")
    @Schema(description = "种子ID", example = "1")
    private Long seedId;
}