package cn.jxufe.model.enums;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Schema(description = "作物状态")
@Getter
@AllArgsConstructor
public enum CropStatus {
    SEED("种子"),
    GROWING("生长中"),
    READY_TO_HARVEST("成熟"),
    HARVESTED("已收获");

    private final String chineseName;
}
