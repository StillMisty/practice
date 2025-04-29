package cn.jxufe.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "玩家数据传输对象")
public class PlayerDTO {

    @Schema(description = "玩家 ID", example = "1")
    private Long id;

    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 50, message = "用户名长度必须在3-50个字符之间")
    @Schema(description = "用户名", example = "player001")
    private String username;

    @Size(max = 100, message = "显示名称不能超过100个字符")
    @Schema(description = "显示名称", example = "萌萌的小农民")
    private String displayName;

    @Schema(description = "经验值", example = "1500")
    @Min(value = 0, message = "经验值不能为负数")
    private Long experiencePoints;

    @Schema(description = "总积分", example = "750")
    @Min(value = 0, message = "积分不能为负数")
    private Integer totalPoints;

    @Schema(description = "金币", example = "3000")
    @Min(value = 0, message = "金币不能为负数")
    private Long goldCoins;

    @Schema(description = "拥有的种子ID列表")
    private Set<Long> ownedSeedIds;
}