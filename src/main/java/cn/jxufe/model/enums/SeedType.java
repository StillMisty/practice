package cn.jxufe.model.enums;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Schema(description = "种子等级")
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public enum SeedType {
    COMMON("普通",1),
    UNCOMMON("非凡",2),
    RARE("稀有",3),
    EPIC("史诗",4),
    LEGENDARY("传说",5);

    private final String chineseName;
    private final int grade;
}