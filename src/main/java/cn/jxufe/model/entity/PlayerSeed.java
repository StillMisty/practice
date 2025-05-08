package cn.jxufe.model.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "player_seeds")
@Data
public class PlayerSeed {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seed_id", nullable = false)
    private Seed seed;

    @Column(nullable = false)
    private Integer quantity;
}