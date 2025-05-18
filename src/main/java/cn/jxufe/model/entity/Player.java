package cn.jxufe.model.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "player")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Schema(description = "玩家")
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Schema(description = "玩家 ID")
    private Long id;

    @Column(name = "username", unique = true, nullable = false, length = 50)
    @Schema(description = "用户名")
    private String username;

    @Column(name= "avatar_path")
    @Schema(description = "头像保存路径")
    private String avatarPath;

    @Column(name = "display_name", length = 100)
    @Schema(description = "显示名称")
    private String displayName;

    @Column(name = "experience_points")
    @Schema(description = "经验值")
    private long experiencePoints;

    @Column(name = "total_points")
    @Schema(description = "总积分")
    private int totalPoints;

    @Column(name = "gold_coins")
    @Schema(description = "金币")
    private long goldCoins;
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "player_growth_characteristic",
            joinColumns = @JoinColumn(name = "player_id"),
            inverseJoinColumns = @JoinColumn(name = "growth_characteristic_id")
    )
    @ToString.Exclude
    @Schema(description = "拥有的生长特性")
    private Set<GrowthCharacteristic> growthCharacteristics;
    
    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Player player = (Player) o;
        return getId() != null && Objects.equals(getId(), player.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
