package cn.jxufe.service;

import cn.jxufe.model.dto.CropStatusesResponse;
import cn.jxufe.model.dto.GrowthCharacteristicDTO;
import cn.jxufe.model.enums.CropStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface GrowthCharacteristicService {

    // 创建新的生长特性
    GrowthCharacteristicDTO createGrowthCharacteristic(GrowthCharacteristicDTO dto);

    // 更新生长特性
    GrowthCharacteristicDTO updateGrowthCharacteristic(Long id, GrowthCharacteristicDTO dto);

    // 更新生长特性图片
    GrowthCharacteristicDTO updateGrowthCharacteristicImage(Long id, MultipartFile file) throws IOException;

    // 获取生长特性图片
    Path getGrowthCharacteristicImagePath(Long id);

    // 删除生长特性图片
    boolean deleteGrowthCharacteristicImage(Long id);

    // 删除生长特性
    void deleteGrowthCharacteristic(Long id);

    // 获取单个生长特性
    GrowthCharacteristicDTO getGrowthCharacteristicById(Long id);

    // 获取所有生长特性
    Page<GrowthCharacteristicDTO> getAllGrowthCharacteristics(Pageable pageable);

    // 获取特定种子的所有生长特性
    List<GrowthCharacteristicDTO> getGrowthCharacteristicsBySeedId(Long seedId, Sort sort);

    // 根据生长阶段查询生长特性
    List<GrowthCharacteristicDTO> getGrowthCharacteristicsByStage(int growthStage);

    // 根据作物状态查询生长特性
    List<GrowthCharacteristicDTO> getGrowthCharacteristicsByCropStatus(CropStatus cropStatus);

    // 获取特定种子特定生长阶段的生长特性
    GrowthCharacteristicDTO getGrowthCharacteristicBySeedIdAndStage(Long seedId, int growthStage);

    // 获取虫害概率大于特定值的生长特性
    List<GrowthCharacteristicDTO> getGrowthCharacteristicsByPestProbability(double probability);

    // 根据生长阶段标题模糊查询
    List<GrowthCharacteristicDTO> searchGrowthCharacteristicsByTitle(String title);

    // 获取特定种子的所有生长阶段，按阶段排序
    List<GrowthCharacteristicDTO> getGrowthCharacteristicsBySeedIdSorted(Long seedId);

    List<CropStatusesResponse> getAllCropStatuses();
}