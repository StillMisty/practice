package cn.jxufe.model.entity;

import cn.jxufe.model.enums.LandType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class PlayerLand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "土地 ID")
    private Long id;

    @Schema(description = "玩家")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", nullable = false)
    @ToString.Exclude
    private Player player;

    @Schema(description = "土地种类")
    private LandType landType;

    @Schema(description = "所种植的种子")
    @ManyToOne
    @JoinColumn(name = "seed_id")
    @ToString.Exclude
    private Seed seed;

    @Schema(description = "当前生长阶段")
    @ManyToOne
    @JoinColumn(name = "growth_characteristic_id")
    @ToString.Exclude
    private GrowthCharacteristic growthCharacteristic;

    @Schema(description = "是否生虫")
    @Column(name = "pest_infestation")
    private boolean pestInfestation;

    @Schema(description = "作物种植时间，单位：毫秒，0表示未种植")
    @Column(name = "planting_time")
    private Long plantingTime;

    @Schema(description = "可收获的果实数量")
    @Column(name = "harvestable_quantity")
    private int harvestableQuantity;

    @Schema(description = "作物处于第几季")
    @Column(name = "growth_season")
    private int growthSeason;
}
