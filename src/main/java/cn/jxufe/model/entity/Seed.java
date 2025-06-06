package cn.jxufe.model.entity;

import cn.jxufe.model.dto.SeedDTO;
import cn.jxufe.model.enums.LandType;
import cn.jxufe.model.enums.SeedType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.proxy.HibernateProxy;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "seed")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Schema(description = "种子")
public class Seed {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "种子 ID")
    private Long id;

    @Column(name = "image_path")
    @Schema(description = "种子图片保存路径")
    private String imagePath;

    @Column(name = "seed_name")
    @Schema(description = "种子名称")
    private String seedName;

    @Column(name = "growth_season_count")
    @Schema(description = "生长季节数")
    private int growthSeasonCount;

    @Column(name = "seed_level")
    @Schema(description = "种子等级")
    private int seedLevel;

    @Column(name = "seed_type")
    @Schema(description = "种子类型")
    private SeedType seedType;

    @Column(name = "experience")
    @Schema(description = "成熟后收获经验值")
    private int experience;

    @Column(name = "points")
    @Schema(description = "成熟后收获积分")
    private int points;

    @Column(name = "harvest_yield")
    @Schema(description = "成熟后收获产量")
    private int harvestYield;

    @Column(name = "growth_time_per_season")
    @Schema(description = "每季生长时间，单位为秒")
    private int growthTimePerSeason;

    @Column(name = "seed_purchase_price")
    @Schema(description = "种子购买价格")
    private Long seedPurchasePrice;

    @Column(name = "fruit_price_per_unit")
    @Schema(description = "每单位水果出售价格")
    private Long fruitPricePerUnit;

    @Column(name = "land_requirement")
    @Schema(description = "种植土地需求")
    private LandType landRequirement;

    @Column(name = "planting_tip", length = 500)
    @Schema(description = "种植提示")
    private String plantingTip;

    @OneToMany(mappedBy = "seed", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<GrowthCharacteristic> growthCharacteristics;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Seed that = (Seed) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }

    public SeedDTO toDTO() {
        SeedDTO seedDTO = new SeedDTO();
        seedDTO.setId(this.id);
        seedDTO.setSeedName(this.seedName);
        seedDTO.setGrowthSeasonCount(this.growthSeasonCount);
        seedDTO.setSeedLevel(this.seedLevel);
        seedDTO.setSeedType(this.seedType);
        seedDTO.setExperience(this.experience);
        seedDTO.setPoints(this.points);
        seedDTO.setHarvestYield(this.harvestYield);
        seedDTO.setGrowthTimePerSeason(this.growthTimePerSeason);
        seedDTO.setSeedPurchasePrice(this.seedPurchasePrice);
        seedDTO.setFruitPricePerUnit(this.fruitPricePerUnit);
        seedDTO.setLandRequirement(this.landRequirement);
        seedDTO.setPlantingTip(this.plantingTip);

        return seedDTO;
    }
}
