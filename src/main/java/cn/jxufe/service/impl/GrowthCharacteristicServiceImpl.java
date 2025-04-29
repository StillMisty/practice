package cn.jxufe.service.impl;

import cn.jxufe.exception.ResourceNotFoundException;
import cn.jxufe.model.dto.GrowthCharacteristicDTO;
import cn.jxufe.model.entity.GrowthCharacteristic;
import cn.jxufe.model.entity.Seed;
import cn.jxufe.model.enums.CropStatus;
import cn.jxufe.repository.GrowthCharacteristicRepository;
import cn.jxufe.repository.SeedRepository;
import cn.jxufe.service.GrowthCharacteristicService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class GrowthCharacteristicServiceImpl implements GrowthCharacteristicService {

    private final GrowthCharacteristicRepository growthCharacteristicRepository;
    private final SeedRepository seedRepository;

    @Override
    public GrowthCharacteristicDTO createGrowthCharacteristic(GrowthCharacteristicDTO dto) {
        Seed seed = seedRepository.findById(dto.getSeedId())
                .orElseThrow(() -> new ResourceNotFoundException("种子不存在，ID: " + dto.getSeedId()));
        
        GrowthCharacteristic growthCharacteristic = convertToEntity(dto, seed);
        GrowthCharacteristic savedCharacteristic = growthCharacteristicRepository.save(growthCharacteristic);
        
        return convertToDTO(savedCharacteristic);
    }

    @Override
    public GrowthCharacteristicDTO updateGrowthCharacteristic(Long id, GrowthCharacteristicDTO dto) {
        GrowthCharacteristic existingCharacteristic = growthCharacteristicRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("生长特性不存在，ID: " + id));
        
        // 如果更新时改变了种子
        if (!existingCharacteristic.getSeed().getSeedId().equals(dto.getSeedId())) {
            Seed newSeed = seedRepository.findById(dto.getSeedId())
                    .orElseThrow(() -> new ResourceNotFoundException("种子不存在，ID: " + dto.getSeedId()));
            existingCharacteristic.setSeed(newSeed);
        }
        
        updateCharacteristicFromDTO(existingCharacteristic, dto);
        GrowthCharacteristic updatedCharacteristic = growthCharacteristicRepository.save(existingCharacteristic);
        
        return convertToDTO(updatedCharacteristic);
    }

    @Override
    public void deleteGrowthCharacteristic(Long id) {
        if (!growthCharacteristicRepository.existsById(id)) {
            throw new ResourceNotFoundException("生长特性不存在，ID: " + id);
        }
        growthCharacteristicRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public GrowthCharacteristicDTO getGrowthCharacteristicById(Long id) {
        GrowthCharacteristic characteristic = growthCharacteristicRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("生长特性不存在，ID: " + id));
        
        return convertToDTO(characteristic);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GrowthCharacteristicDTO> getAllGrowthCharacteristics() {
        return growthCharacteristicRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<GrowthCharacteristicDTO> getGrowthCharacteristicsBySeedId(Long seedId) {
        return growthCharacteristicRepository.findBySeedSeedId(seedId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<GrowthCharacteristicDTO> getGrowthCharacteristicsByStage(int growthStage) {
        return growthCharacteristicRepository.findByGrowthStage(growthStage).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<GrowthCharacteristicDTO> getGrowthCharacteristicsByCropStatus(CropStatus cropStatus) {
        return growthCharacteristicRepository.findByCropStatus(cropStatus).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public GrowthCharacteristicDTO getGrowthCharacteristicBySeedIdAndStage(Long seedId, int growthStage) {
        GrowthCharacteristic characteristic = growthCharacteristicRepository
                .findBySeedSeedIdAndGrowthStage(seedId, growthStage);
        
        if (characteristic == null) {
            throw new ResourceNotFoundException(
                    "未找到种子ID为 " + seedId + " 且生长阶段为 " + growthStage + " 的生长特性");
        }
        
        return convertToDTO(characteristic);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GrowthCharacteristicDTO> getGrowthCharacteristicsByPestProbability(double probability) {
        return growthCharacteristicRepository.findByPestInfestationProbabilityGreaterThan(probability).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<GrowthCharacteristicDTO> searchGrowthCharacteristicsByTitle(String title) {
        return growthCharacteristicRepository.findByGrowthStageTitleContaining(title).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<GrowthCharacteristicDTO> getGrowthCharacteristicsBySeedIdSorted(Long seedId) {
        return growthCharacteristicRepository.findBySeedIdOrderByGrowthStageAsc(seedId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // 辅助方法：将 DTO 转换为实体
    private GrowthCharacteristic convertToEntity(GrowthCharacteristicDTO dto, Seed seed) {
        GrowthCharacteristic characteristic = new GrowthCharacteristic();
        characteristic.setSeed(seed);
        updateCharacteristicFromDTO(characteristic, dto);
        return characteristic;
    }

    // 辅助方法：将实体转换为 DTO
    private GrowthCharacteristicDTO convertToDTO(GrowthCharacteristic characteristic) {
        GrowthCharacteristicDTO dto = new GrowthCharacteristicDTO();
        dto.setId(characteristic.getId());
        dto.setGrowthStage(characteristic.getGrowthStage());
        dto.setGrowthStageTitle(characteristic.getGrowthStageTitle());
        dto.setStageGrowthTime(characteristic.getStageGrowthTime());
        dto.setPestInfestationProbability(characteristic.getPestInfestationProbability());
        dto.setImageWidth(characteristic.getImageWidth());
        dto.setImageHeight(characteristic.getImageHeight());
        dto.setImageOffsetX(characteristic.getImageOffsetX());
        dto.setImageOffsetY(characteristic.getImageOffsetY());
        dto.setCropStatus(characteristic.getCropStatus());
        dto.setSeedId(characteristic.getSeed().getSeedId());
        return dto;
    }

    // 辅助方法：从 DTO 更新实体
    private void updateCharacteristicFromDTO(GrowthCharacteristic characteristic, GrowthCharacteristicDTO dto) {
        characteristic.setGrowthStage(dto.getGrowthStage());
        characteristic.setGrowthStageTitle(dto.getGrowthStageTitle());
        characteristic.setStageGrowthTime(dto.getStageGrowthTime());
        characteristic.setPestInfestationProbability(dto.getPestInfestationProbability());
        characteristic.setImageWidth(dto.getImageWidth());
        characteristic.setImageHeight(dto.getImageHeight());
        characteristic.setImageOffsetX(dto.getImageOffsetX());
        characteristic.setImageOffsetY(dto.getImageOffsetY());
        characteristic.setCropStatus(dto.getCropStatus());
    }
}