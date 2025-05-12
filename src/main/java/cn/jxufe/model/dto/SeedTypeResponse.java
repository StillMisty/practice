package cn.jxufe.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record SeedTypeResponse(
        @Schema(description = "传参所用名称")
        String code,
        @Schema(description = "等级中文名称")
        String chineseName,
        @Schema(description = "等级排序，越大越好")
        int grade
) {
}
