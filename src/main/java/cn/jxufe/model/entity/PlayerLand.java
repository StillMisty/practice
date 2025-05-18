package cn.jxufe.model.entity;

import cn.jxufe.model.enums.LandType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class PlayerLand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "玩家土地 ID")
    private Long id;

    @Schema(description = "玩家 ID")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    @Schema(description = "土地种类")
    private LandType landType;

    @Schema(description = "所种植的种子")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seed_id")
    private Seed seed;

    @Schema(description = "当前生长阶段")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "growth_characteristic_id")
    private GrowthCharacteristic growthCharacteristic;

    @Schema(description = "是否生虫")
    @Column(name = "pest_infestation")
    private boolean pestInfestation;

    @Schema(description = "作物种植时间")
    @Column(name = "planting_time")
    private long plantingTime;
}
