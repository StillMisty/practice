package cn.jxufe.service;

import cn.jxufe.model.dto.SeedDTO;
import cn.jxufe.model.enums.LandType;
import cn.jxufe.model.enums.SeedType;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface SeedService {
    
    // 创建新种子
    SeedDTO createSeed(SeedDTO seedDTO);

    // 更新种子信息
    SeedDTO updateSeed(
            Long seedId, SeedDTO seedDTO
    ) ;

    // 更新种子图片
    SeedDTO updateSeedImage(
            Long seedId,
            MultipartFile file
    ) throws IOException;

    // 获取种子图片
    Path getSeedImagePath(Long seedId) throws IOException;

    // 删除种子图片
    boolean deleteSeedImage(Long seedId) throws IOException;

    // 删除种子
    void deleteSeed(Long seedId);
    
    // 获取单个种子信息
    SeedDTO getSeedById(Long seedId);
    
    // 获取所有种子
    List<SeedDTO> getAllSeeds();
    
    // 根据名称搜索种子
    List<SeedDTO> searchSeedsByName(String seedName);
    
    // 根据种子类型查找
    List<SeedDTO> findSeedsByType(SeedType seedType);
    
    // 根据种子等级查找
    List<SeedDTO> findSeedsByLevel(int seedLevel);
    
    // 根据土地需求查找
    List<SeedDTO> findSeedsByLandRequirement(LandType landType);
    
    // 根据价格区间查找
    List<SeedDTO> findSeedsByPriceRange(double minPrice, double maxPrice);
    
    // 按价格排序获取所有种子
    List<SeedDTO> getAllSeedsOrderByPrice();
    
    // 获取玩家拥有的所有种子
    List<SeedDTO> getSeedsByPlayerId(Long playerId);
}