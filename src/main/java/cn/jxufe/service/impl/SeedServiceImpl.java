package cn.jxufe.service.impl;

import cn.jxufe.exception.ResourceNotFoundException;
import cn.jxufe.model.dto.SeedDTO;
import cn.jxufe.model.entity.Seed;
import cn.jxufe.model.enums.LandType;
import cn.jxufe.model.enums.SeedType;
import cn.jxufe.repository.SeedRepository;
import cn.jxufe.service.SeedService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class SeedServiceImpl implements SeedService {

    private final SeedRepository seedRepository;

    @Override
    public SeedDTO createSeed(SeedDTO seedDTO) {
        Seed seed = convertToEntity(seedDTO);
        Seed savedSeed = seedRepository.save(seed);
        return convertToDTO(savedSeed);
    }

    @Override
    public SeedDTO updateSeed(Long seedId, SeedDTO seedDTO) {
        Seed existingSeed = seedRepository.findById(seedId)
                .orElseThrow(() -> new ResourceNotFoundException("种子不存在，ID: " + seedId));

        updateSeedFromDTO(existingSeed, seedDTO);
        Seed updatedSeed = seedRepository.save(existingSeed);
        return convertToDTO(updatedSeed);
    }

    @Override
    public void deleteSeed(Long seedId) {
        if (!seedRepository.existsById(seedId)) {
            throw new ResourceNotFoundException("种子不存在，ID: " + seedId);
        }
        seedRepository.deleteById(seedId);
    }

    @Override
    @Transactional(readOnly = true)
    public SeedDTO getSeedById(Long seedId) {
        Seed seed = seedRepository.findById(seedId)
                .orElseThrow(() -> new ResourceNotFoundException("种子不存在，ID: " + seedId));
        return convertToDTO(seed);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SeedDTO> getAllSeeds() {
        return seedRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SeedDTO> searchSeedsByName(String seedName) {
        return seedRepository.findBySeedNameContaining(seedName).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SeedDTO> findSeedsByType(SeedType seedType) {
        return seedRepository.findBySeedType(seedType).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SeedDTO> findSeedsByLevel(int seedLevel) {
        return seedRepository.findBySeedLevel(seedLevel).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SeedDTO> findSeedsByLandRequirement(LandType landType) {
        return seedRepository.findByLandRequirement(landType).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SeedDTO> findSeedsByPriceRange(double minPrice, double maxPrice) {
        return seedRepository.findBySeedPurchasePriceBetween(minPrice, maxPrice).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SeedDTO> getAllSeedsOrderByPrice() {
        return seedRepository.findAllOrderBySeedPurchasePriceAsc().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SeedDTO> getSeedsByPlayerId(Long playerId) {
        return seedRepository.findSeedsByPlayerId(playerId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // 辅助方法：将 DTO 转换为实体
    private Seed convertToEntity(SeedDTO seedDTO) {
        Seed seed = new Seed();
        updateSeedFromDTO(seed, seedDTO);
        return seed;
    }

    // 辅助方法：将实体转换为 DTO
    private SeedDTO convertToDTO(Seed seed) {
        SeedDTO dto = new SeedDTO();
        dto.setSeedId(seed.getSeedId());
        dto.setImagePath(seed.getImagePath());
        dto.setSeedName(seed.getSeedName());
        dto.setGrowthSeasonCount(seed.getGrowthSeasonCount());
        dto.setSeedLevel(seed.getSeedLevel());
        dto.setSeedType(seed.getSeedType());
        dto.setExperience(seed.getExperience());
        dto.setPoints(seed.getPoints());
        dto.setHarvestYield(seed.getHarvestYield());
        dto.setGrowthTimePerSeason(seed.getGrowthTimePerSeason());
        dto.setSeedPurchasePrice(seed.getSeedPurchasePrice());
        dto.setFruitPricePerUnit(seed.getFruitPricePerUnit());
        dto.setLandRequirement(seed.getLandRequirement());
        dto.setPlantingTip(seed.getPlantingTip());
        return dto;
    }

    // 辅助方法：从 DTO 更新实体
    private void updateSeedFromDTO(Seed seed, SeedDTO seedDTO) {
        seed.setImagePath(seedDTO.getImagePath());
        seed.setSeedName(seedDTO.getSeedName());
        seed.setGrowthSeasonCount(seedDTO.getGrowthSeasonCount());
        seed.setSeedLevel(seedDTO.getSeedLevel());
        seed.setSeedType(seedDTO.getSeedType());
        seed.setExperience(seedDTO.getExperience());
        seed.setPoints(seedDTO.getPoints());
        seed.setHarvestYield(seedDTO.getHarvestYield());
        seed.setGrowthTimePerSeason(seedDTO.getGrowthTimePerSeason());
        seed.setSeedPurchasePrice(seedDTO.getSeedPurchasePrice());
        seed.setFruitPricePerUnit(seedDTO.getFruitPricePerUnit());
        seed.setLandRequirement(seedDTO.getLandRequirement());
        seed.setPlantingTip(seedDTO.getPlantingTip());
    }
}