package cn.jxufe.service.impl;

import cn.jxufe.exception.ResourceNotFoundException;
import cn.jxufe.model.entity.*;
import cn.jxufe.model.enums.CropStatus;
import cn.jxufe.repository.*;
import cn.jxufe.service.FarmService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FarmServiceImpl implements FarmService {
    private final PlayerLandRepository playerLandRepository;
    private final PlayerRepository playerRepository;
    private final PlayerSeedRepository playerSeedRepository;
    private final GrowthCharacteristicRepository growthCharacteristicRepository;
    private final SeedRepository seedRepository;

    @Override
    @Transactional
    public String actionPlant(Long landId, Long seedId, Long playerId) {
        PlayerLand land = validatePlayerAndLand(landId, playerId);

        if (land.getSeed() != null) {
            return "土地上已经有作物了";
        }

        // 验证种子
        Seed seed = seedRepository.findById(seedId)
                .orElse(null);
        if (seed == null) {
            return "种子不存在";
        }

        // 验证土地类型
        if (land.getLandType() != seed.getLandRequirement()) {
            return "土地类型不符合种子要求";
        }

        // 验证种子数量
        PlayerSeed playerSeed = playerSeedRepository.findByPlayerIdAndSeedId(playerId, seedId)
                .orElse(null);

        if (playerSeed == null) {
            return "玩家没有该种子";
        }

        if (playerSeed.getQuantity() < 1) {
            return "种子数量不足";
        }

        // 获取种子阶段
        GrowthCharacteristic growthCharacteristic = growthCharacteristicRepository.findBySeed_IdAndCropStatus(seedId, CropStatus.SEED)
                .orElse(null);
        if (growthCharacteristic == null) {
            return "该作物不存在种子阶段";
        }

        // 播种操作
        playerSeed.setQuantity(playerSeed.getQuantity() - 1);
        land.setSeed(seed);
        land.setGrowthCharacteristic(growthCharacteristic);
        land.setPlantingTime(System.currentTimeMillis());
        land.setHarvestableQuantity(seed.getHarvestYield());
        land.setGrowthSeason(0); // 初始为0季

        playerSeedRepository.save(playerSeed);
        playerLandRepository.save(land);

        return "种植成功";
    }

    @Override
    @Transactional
    public String actionKillWorm(Long landId, Long playerId) {
        PlayerLand land = validatePlayerAndLand(landId, playerId);

        // 验证虫害状态
        if (!land.isPestInfestation()) {
            return "该土地没有虫害";
        }

        // 除虫操作
        land.setPestInfestation(false);
        Player player = land.getPlayer();
        int experiencePoints = 2;
        int goldCoins = 1;
        int totalPoints = 2;
        player.setExperiencePoints(player.getExperiencePoints() + experiencePoints);
        player.setGoldCoins(player.getGoldCoins() + goldCoins);
        player.setTotalPoints(player.getTotalPoints() + totalPoints);

        playerLandRepository.save(land);

        return String.format(
                "除虫成功<br>经验值：%d<br>金币：%d<br>总积分：%d",
                experiencePoints, goldCoins, totalPoints
        );
    }

    @Override
    public String actionHarvest(Long landId, Long playerId) {
        PlayerLand land = validatePlayerAndLand(landId, playerId);

        // 验证土地状态
        if (land.getSeed() == null) {
            return "土地上没有作物";
        }

        // 验证作物状态
        if (land.getGrowthCharacteristic().getCropStatus() != CropStatus.READY_TO_HARVEST) {
            return "作物未成熟或已收获";
        }

        // 收获操作
        Seed seed = land.getSeed();
        Player player = land.getPlayer();
        int experiencePoints = seed.getExperience();
        long goldCoins = seed.getFruitPricePerUnit() * land.getHarvestableQuantity();
        int totalPoints = seed.getExperience();
        player.setExperiencePoints(player.getExperiencePoints() + experiencePoints);
        player.setGoldCoins(player.getGoldCoins() + goldCoins);
        player.setTotalPoints(player.getTotalPoints() + totalPoints);

        // 判断为几季节作物
        if (land.getGrowthSeason() < seed.getGrowthSeasonCount()) {
            // 可以继续生长
            land.setGrowthSeason(land.getGrowthSeason() + 1);
        } else {
            // 作物完全收割，进入枯草阶段
            land.setGrowthCharacteristic(
                    growthCharacteristicRepository
                            .findBySeed_IdAndCropStatus(seed.getId(), CropStatus.HARVESTED)
                            .orElseThrow(() -> new ResourceNotFoundException("作物无枯草阶段")));
        }

        playerLandRepository.save(land);

        return String.format(
                "收获成功<br>经验值：%d<br>金币：%d<br>总积分：%d",
                experiencePoints, goldCoins, totalPoints
        );
    }

    @Override
    public String actionCleanLand(Long landId, Long playerId) {
        PlayerLand land = validatePlayerAndLand(landId, playerId);

        if (land.getGrowthCharacteristic().getCropStatus() != CropStatus.HARVESTED) {
            return "土地上没有枯草";
        }

        // 清理土地操作
        land.setSeed(null);
        land.setGrowthCharacteristic(null);
        land.setHarvestableQuantity(0);
        land.setPlantingTime(0L);
        land.setGrowthSeason(0);

        Player player = land.getPlayer();
        int experiencePoints = 5;
        int totalPoints = 5;
        player.setExperiencePoints(player.getExperiencePoints() + experiencePoints);
        player.setTotalPoints(player.getTotalPoints() + totalPoints);

        playerLandRepository.save(land);

        return String.format(
                "清理成功<br>经验值：%d<br>总积分：%d",
                experiencePoints, totalPoints
        );
    }


    private PlayerLand validatePlayerAndLand(Long landId, Long playerId) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new ResourceNotFoundException("玩家不存在"));

        PlayerLand land = playerLandRepository.findById(landId)
                .orElseThrow(() -> new ResourceNotFoundException("土地不存在"));

        if (!player.getLands().contains(land)) {
            throw new RuntimeException("这不是你的土地");
        }

        return land;
    }
}