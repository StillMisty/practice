package cn.jxufe.repository;

import cn.jxufe.model.entity.GrowthCharacteristic;
import cn.jxufe.model.enums.CropStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GrowthCharacteristicRepository extends JpaRepository<GrowthCharacteristic, Long> {

    // 根据种子ID查找生长特性
    List<GrowthCharacteristic> findBySeedSeedId(Long seedId);

    // 根据生长阶段查找
    List<GrowthCharacteristic> findByGrowthStage(int growthStage);

    // 根据作物状态查找
    List<GrowthCharacteristic> findByCropStatus(CropStatus cropStatus);

    // 查找特定种子的特定生长阶段
    GrowthCharacteristic findBySeedSeedIdAndGrowthStage(Long seedId, int growthStage);

    // 查找虫害概率高于某阈值的生长特性
    List<GrowthCharacteristic> findByPestInfestationProbabilityGreaterThan(double probability);

    // 根据生长阶段标题模糊查询
    List<GrowthCharacteristic> findByGrowthStageTitleContaining(String title);

    // 查询特定种子的所有生长阶段，按阶段排序
    @Query("SELECT gc FROM GrowthCharacteristic gc WHERE gc.seed.seedId = :seedId ORDER BY gc.growthStage ASC")
    List<GrowthCharacteristic> findBySeedIdOrderByGrowthStageAsc(@Param("seedId") Long seedId);
}