package cn.jxufe.model.enums;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Schema(description = "土地类型")
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public enum LandType {
    RED("红土地"),
    YELLOW("黄土地"),
    BLACK("黑土地");

    private final String chineseName;
}
