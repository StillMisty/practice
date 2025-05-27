package cn.jxufe.repository;

import cn.jxufe.model.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {

    // 根据用户名查找玩家
    Optional<Player> findByUsername(String username);

    // 根据显示名称模糊查询
    List<Player> findByDisplayNameContaining(String displayName);

    // 查找经验值大于指定值的玩家
    List<Player> findByExperiencePointsGreaterThan(long experiencePoints);

    // 查找金币数量大于指定值的玩家
    List<Player> findByGoldCoinsGreaterThan(long goldCoins);

    // 查找按照经验值排序的所有玩家（排行榜功能）, 限制20返回数量
    List<Player> findTop20AllByOrderByExperiencePointsDesc();

    // 查找按照金币排序的所有玩家
    List<Player> findTop20AllByOrderByGoldCoinsDesc();

    // 查找按照总积分排序的所有玩家
    List<Player> findTop20AllByOrderByTotalPointsDesc();

    // 检查用户名是否存在
    boolean existsByUsername(String username);
}