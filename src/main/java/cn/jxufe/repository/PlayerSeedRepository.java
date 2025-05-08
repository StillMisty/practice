package cn.jxufe.repository;

import cn.jxufe.model.entity.PlayerSeed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlayerSeedRepository extends JpaRepository<PlayerSeed, Long> {
    Optional<PlayerSeed> findByPlayerIdAndSeedId(Long playerId, Long seedId);
}