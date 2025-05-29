package cn.jxufe.service.impl;

import cn.jxufe.exception.ResourceNotFoundException;
import cn.jxufe.model.dto.SeedDTO;
import cn.jxufe.model.dto.SeedTypeResponse;
import cn.jxufe.model.entity.Seed;
import cn.jxufe.model.enums.LandType;
import cn.jxufe.model.enums.SeedType;
import cn.jxufe.repository.PlayerRepository;
import cn.jxufe.repository.SeedRepository;
import cn.jxufe.service.AuthService;
import cn.jxufe.service.FileStorageService;
import cn.jxufe.service.SeedService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Transactional
public class SeedServiceImpl implements SeedService {

    private final FileStorageService fileStorageService;
    private final SeedRepository seedRepository;
    private final PlayerRepository playerRepository;
    private final AuthService authService;

    @Override
    public SeedDTO createSeed(SeedDTO seedDTO) {
        Seed seed = convertToEntity(seedDTO);
        Seed savedSeed = seedRepository.save(seed);
        return savedSeed.toDTO();
    }

    @Override
    public SeedDTO updateSeed(
            Long seedId, SeedDTO seedDTO) {
        Seed existingSeed = seedRepository.findById(seedId)
                .orElseThrow(() -> new ResourceNotFoundException("种子不存在，ID: " + seedId));

        updateSeedFromDTO(existingSeed, seedDTO);
        Seed updatedSeed = seedRepository.save(existingSeed);
        return updatedSeed.toDTO();
    }

    @Override
    public SeedDTO updateSeedImage(
            Long seedId,
            MultipartFile file
    ) throws IOException {
        Seed existingSeed = seedRepository.findById(seedId)
                .orElseThrow(() -> new ResourceNotFoundException("种子不存在，ID: " + seedId));

        // 删除旧图片
        fileStorageService.deleteFile(existingSeed.getImagePath());

        // 上传新图片
        String imagePath = fileStorageService.storeFile(file, "seeds");
        existingSeed.setImagePath(imagePath);
        Seed updatedSeed = seedRepository.save(existingSeed);
        return updatedSeed.toDTO();
    }

    @Override
    public Path getSeedImagePath(Long seedId) {
        Seed seed = seedRepository.findById(seedId)
                .orElseThrow(() -> new ResourceNotFoundException("种子不存在，ID: " + seedId));
        return seed.getImagePath() == null ? null : fileStorageService.getFilePath(seed.getImagePath());
    }

    @Override
    public boolean deleteSeedImage(Long seedId) {
        Seed existingSeed = seedRepository.findById(seedId)
                .orElseThrow(() -> new ResourceNotFoundException("种子不存在，ID: " + seedId));

        String imagePath = existingSeed.getImagePath();
        boolean deleted = fileStorageService.deleteFile(imagePath);
        if (deleted) {
            existingSeed.setImagePath(null);
            seedRepository.save(existingSeed);
        }
        return deleted;
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
        return seed.toDTO();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SeedDTO> getAllSeeds(Pageable pageable) {
        return seedRepository.findAll(pageable)
                .map(Seed::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SeedDTO> searchSeedsByName(String seedName, Pageable pageable) {
        return seedRepository.findBySeedNameContaining(seedName, pageable)
                .map(Seed::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SeedDTO> findSeedsByType(SeedType seedType) {
        return seedRepository.findBySeedType(seedType).stream()
                .map(Seed::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SeedDTO> findSeedsByLevel(int seedLevel) {
        return seedRepository.findBySeedLevel(seedLevel).stream()
                .map(Seed::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SeedDTO> findSeedsByLandRequirement(LandType landType) {
        return seedRepository.findByLandRequirement(landType).stream()
                .map(Seed::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SeedDTO> findSeedsByPriceRange(double minPrice, double maxPrice) {
        return seedRepository.findBySeedPurchasePriceBetween(minPrice, maxPrice).stream()
                .map(Seed::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SeedDTO> getAllSeedsOrderByPrice() {
        return seedRepository.findByOrderBySeedPurchasePriceAsc().stream()
                .map(Seed::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SeedTypeResponse> getAllSeedTypes() {
        return Stream.of(SeedType.values())
                .map(type -> new SeedTypeResponse(type.name(), type.getChineseName(), type.getGrade()))
                .collect(Collectors.toList());
    }

    // 辅助方法：将 DTO 转换为实体
    private Seed convertToEntity(SeedDTO seedDTO) {
        Seed seed = new Seed();
        updateSeedFromDTO(seed, seedDTO);
        return seed;
    }

    // 辅助方法：从 DTO 更新实体
    private void updateSeedFromDTO(Seed seed, SeedDTO seedDTO) {
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