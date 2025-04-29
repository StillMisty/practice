package cn.jxufe.model.enums;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Schema(description = "土地类型")
@Getter
@AllArgsConstructor
public enum LandType {
    RED("红土地"),
    YELLOW("黄土地"),
    BLACK("黑土地");

    private final String chineseName;
}
