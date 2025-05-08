package cn.jxufe.model.enums;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Schema(description = "种子等级")
@Getter
@AllArgsConstructor
public enum SeedType {
    COMMON("普通", 1),
    UNCOMMON("非凡", 2),
    RARE("稀有", 3),
    EPIC("史诗", 4),
    LEGENDARY("传说", 5);

    private final String chineseName;
    private final int grade;
}