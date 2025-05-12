package cn.jxufe.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record LandTypeResponse(
        @Schema(description = "传参所用名称")
        String code,
        @Schema(description = "土地等级中文名称")
        String chineseName) {
}
