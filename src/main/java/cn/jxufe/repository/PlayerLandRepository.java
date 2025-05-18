package cn.jxufe.repository;

import cn.jxufe.model.entity.PlayerLand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerLandRepository extends JpaRepository<PlayerLand,Long> {
}
