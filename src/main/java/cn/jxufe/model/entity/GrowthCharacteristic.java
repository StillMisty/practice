package cn.jxufe.model.entity;

import cn.jxufe.model.enums.CropStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "growth_characteristic")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Schema(description = "种子的各个阶段生长特性")
public class GrowthCharacteristic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Schema(description = "生长特性 ID")
    private Long id;

    @Column(name = "image_path")
    @Schema(description = "生长阶段图片保存路径")
    private String imagePath;

    @Column(name = "growth_stage")
    @Schema(description = "生长阶段")
    private int growthStage;

    @Column(name = "growth_stage_title", length = 100)
    @Schema(description = "生长阶段标题")
    private String growthStageTitle;

    @Column(name = "stage_growth_time")
    @Schema(description = "阶段生长时间")
    private int stageGrowthTime;

    @Column(name = "pest_infestation_probability")
    @Schema(description = "虫害发生概率")
    private double pestInfestationProbability;

    @Column(name = "image_width")
    @Schema(description = "图片宽度")
    private int imageWidth;

    @Column(name = "image_height")
    @Schema(description = "图片高度")
    private int imageHeight;

    @Column(name = "image_offset_x")
    @Schema(description = "图片偏移量 X")
    private int imageOffsetX;

    @Column(name = "image_offset_y")
    @Schema(description = "图片偏移量 Y")
    private int imageOffsetY;

    @Column(name = "crop_status", length = 50)
    @Schema(description = "作物状态")
    private CropStatus cropStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seed_id", nullable = false, referencedColumnName = "id")
    @ToString.Exclude
    @Schema(description = "种子")
    private Seed seed;

    @ManyToMany(mappedBy = "growthCharacteristics", fetch = FetchType.LAZY)
    @ToString.Exclude
    @Schema(description = "拥有的玩家")
    private Set<Player> players;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        GrowthCharacteristic that = (GrowthCharacteristic) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
