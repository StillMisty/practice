package cn.jxufe.repository;

import cn.jxufe.model.entity.Seed;
import cn.jxufe.model.enums.LandType;
import cn.jxufe.model.enums.SeedType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeedRepository extends JpaRepository<Seed, Long> {

    // 根据名称查找
    Page<Seed> findBySeedNameContaining(String seedName, Pageable pageable);

    // 根据种子类型查找
    List<Seed> findBySeedType(SeedType seedType);

    // 根据种子等级查找
    List<Seed> findBySeedLevel(int seedLevel);

    // 根据土地需求类型查找
    List<Seed> findByLandRequirement(LandType landType);

    // 查找特定价格区间内的种子
    List<Seed> findBySeedPurchasePriceBetween(double minPrice, double maxPrice);

    // 查找按照价格排序的种子
    List<Seed> findByOrderBySeedPurchasePriceAsc();
}