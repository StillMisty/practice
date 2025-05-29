package cn.jxufe.repository;

import cn.jxufe.model.entity.GrowthCharacteristic;
import cn.jxufe.model.enums.CropStatus;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GrowthCharacteristicRepository extends JpaRepository<GrowthCharacteristic, Long> {

    // 根据种子ID查找生长特性
    List<GrowthCharacteristic> findBySeedId(Long seedId, Sort sort);

    // 根据生长阶段查找
    List<GrowthCharacteristic> findByGrowthStage(int growthStage);

    // 根据作物状态查找
    List<GrowthCharacteristic> findByCropStatus(CropStatus cropStatus);

    // 查找特定种子的特定生长阶段
    GrowthCharacteristic findBySeedIdAndGrowthStage(Long seedId, int growthStage);

    // 查找虫害概率高于某阈值的生长特性
    List<GrowthCharacteristic> findByPestInfestationProbabilityGreaterThan(double probability);

    // 根据生长阶段标题模糊查询
    List<GrowthCharacteristic> findByGrowthStageTitleContaining(String title);

    // 查询特定种子的所有生长阶段，按阶段排序
    List<GrowthCharacteristic> findBySeedIdOrderByGrowthStageAsc(Long seedId);

    // 查询特定种子的特定作物状态
    Optional<GrowthCharacteristic> findBySeed_IdAndCropStatus(Long id, CropStatus cropStatus);
}