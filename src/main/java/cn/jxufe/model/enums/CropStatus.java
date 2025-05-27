package cn.jxufe.model.enums;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Schema(description = "作物状态")
@Getter
@AllArgsConstructor
public enum CropStatus {
    SEED("种子阶段"),
    GROWING("生长阶段"),
    READY_TO_HARVEST("成熟阶段"),
    HARVESTED("枯草阶段");

    private final String chineseName;
}
