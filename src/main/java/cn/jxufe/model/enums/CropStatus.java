package cn.jxufe.model.enums;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Schema(description = "作物状态")
@Getter
@AllArgsConstructor
public enum CropStatus {
    GROWING("生长中"),
    READY_TO_HARVEST("成熟"),
    HARVESTED("已收获"),
    PEST_INFESTED("虫害发生");

    private final String status;
}
