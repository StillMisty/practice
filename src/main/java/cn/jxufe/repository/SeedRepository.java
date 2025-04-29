package cn.jxufe.repository;

import cn.jxufe.model.entity.Seed;
import cn.jxufe.model.enums.LandType;
import cn.jxufe.model.enums.SeedType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeedRepository extends JpaRepository<Seed, Long> {

    // 根据名称查找
    List<Seed> findBySeedNameContaining(String seedName);

    // 根据种子类型查找
    List<Seed> findBySeedType(SeedType seedType);

    // 根据种子等级查找
    List<Seed> findBySeedLevel(int seedLevel);

    // 根据土地需求类型查找
    List<Seed> findByLandRequirement(LandType landType);

    // 查找特定价格区间内的种子
    List<Seed> findBySeedPurchasePriceBetween(double minPrice, double maxPrice);

    // 根据生长季节数查找
    List<Seed> findByGrowthSeasonCount(int seasonCount);

    // 查找按照价格排序的种子
    @Query("SELECT s FROM Seed s ORDER BY s.seedPurchasePrice ASC")
    List<Seed> findAllOrderBySeedPurchasePriceAsc();

    // 查找特定玩家拥有的种子
    @Query("SELECT s FROM Seed s JOIN s.players p WHERE p.id = :playerId")
    List<Seed> findSeedsByPlayerId(@Param("playerId") Long playerId);
}