package cn.jxufe.service.impl;

import cn.jxufe.exception.ResourceNotFoundException;
import cn.jxufe.model.dto.Message;
import cn.jxufe.model.dto.PlayerLandDTO;
import cn.jxufe.model.entity.*;
import cn.jxufe.model.enums.CropStatus;
import cn.jxufe.model.enums.FarmAction;
import cn.jxufe.model.enums.SoundType;
import cn.jxufe.repository.*;
import cn.jxufe.service.FarmService;
import cn.jxufe.ws.NativeWebSocketServer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FarmServiceImpl implements FarmService {
    private final PlayerLandRepository playerLandRepository;
    private final PlayerRepository playerRepository;
    private final PlayerSeedRepository playerSeedRepository;
    private final GrowthCharacteristicRepository growthCharacteristicRepository;
    private final SeedRepository seedRepository;

    @Override
    @Transactional(readOnly = true)
    public List<PlayerLandDTO> getPlayerLands(Long playerId) {
        return playerRepository.findById(playerId)
                .orElseThrow(() -> new IllegalArgumentException("玩家不存在"))
                .getLands()
                .stream()
                .sorted(Comparator.comparing(PlayerLand::getId)) // 按照 ID 升序排序
                .map(playerLand -> new PlayerLandDTO(
                        playerLand.getId(),
                        playerLand.getLandType(),
                        playerLand.getSeed() != null ? playerLand.getSeed().toDTO() : null,
                        playerLand.getGrowthCharacteristic() != null ? playerLand.getGrowthCharacteristic().toDTO() : null,
                        playerLand.isPestInfestation(),
                        playerLand.getPlantingTime(),
                        playerLand.getHarvestableQuantity(),
                        playerLand.getGrowthSeason()
                ))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Message<?> actionPlant(Long landId, Long seedId, Long playerId) {
        PlayerLand land = validatePlayerAndLand(landId, playerId);

        if (land.getSeed() != null) {
            Message<?> msg = Message.of(-1, FarmAction.PLANT, "土地上已经有作物了", SoundType.FAIL);

            NativeWebSocketServer.pushToPlayer(playerId, msg);
            return msg;
        }

        // 验证种子
        Seed seed = seedRepository.findById(seedId)
                .orElse(null);
        if (seed == null) {
            Message<?> msg = Message.of(-1, FarmAction.PLANT, "种子不存在", SoundType.FAIL);

            NativeWebSocketServer.pushToPlayer(playerId, msg);
            return msg;
        }

        // 验证土地类型
        if (land.getLandType() != seed.getLandRequirement()) {
            Message<?> msg = Message.of(-1, FarmAction.PLANT, "土地类型不符合种子要求", SoundType.FAIL);

            NativeWebSocketServer.pushToPlayer(playerId, msg);
            return msg;
        }

        // 验证种子数量
        PlayerSeed playerSeed = playerSeedRepository.findByPlayerIdAndSeedId(playerId, seedId)
                .orElse(null);

        if (playerSeed == null) {
            Message<?> msg = Message.of(-1, FarmAction.PLANT, "玩家没有该种子", SoundType.FAIL);
            NativeWebSocketServer.pushToPlayer(playerId, msg);
            return msg;
        }

        if (playerSeed.getQuantity() < 1) {
            Message<?> msg = Message.of(-1, FarmAction.PLANT, "种子数量不足", SoundType.FAIL);
            NativeWebSocketServer.pushToPlayer(playerId, msg);
            return msg;
        }

        // 获取种子阶段
        GrowthCharacteristic growthCharacteristic = growthCharacteristicRepository
                .findBySeed_IdAndCropStatusOrderByGrowthStageAsc(seedId, CropStatus.SEED).stream().findFirst().orElse(null);
        if (growthCharacteristic == null) {
            Message<?> msg = Message.of(-1, FarmAction.PLANT, "该作物不存在种子阶段", SoundType.FAIL);
            NativeWebSocketServer.pushToPlayer(playerId, msg);
            return msg;
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

        Message<?> msg = Message.of(1, FarmAction.PLANT, "种植成功", SoundType.SUCCESS, land.toDTO());

        NativeWebSocketServer.pushToPlayer(playerId, msg);
        return msg;
    }

    @Override
    @Transactional
    public Message<?> actionKillWorm(Long landId, Long playerId) {
        PlayerLand land = validatePlayerAndLand(landId, playerId);

        // 验证虫害状态
        if (!land.isPestInfestation()) {
            Message<?> msg = Message.of(-1, FarmAction.KILL_WORM, "该土地没有虫害", SoundType.FAIL);

            NativeWebSocketServer.pushToPlayer(playerId, msg);
            return msg;
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

        Message<PlayerLandDTO> msg = Message.of(
                1, FarmAction.KILL_WORM, String.format(
                        "除虫成功<br>经验值：%d<br>金币：%d<br>总积分：%d",
                        experiencePoints, goldCoins, totalPoints
                ), SoundType.SUCCESS, land.toDTO()
        );

        NativeWebSocketServer.pushToPlayer(playerId, msg);
        return msg;
    }

    @Override
    public Message<?> actionHarvest(Long landId, Long playerId) {
        PlayerLand land = validatePlayerAndLand(landId, playerId);

        // 验证土地状态
        if (land.getSeed() == null) {
            Message<?> msg = Message.of(-1, FarmAction.HARVEST, "土地上没有作物", SoundType.FAIL);

            NativeWebSocketServer.pushToPlayer(playerId, msg);
            return msg;
        }

        // 验证作物状态
        if (land.getGrowthCharacteristic().getCropStatus() != CropStatus.READY_TO_HARVEST) {
            Message<?> msg = Message.of(-1, FarmAction.HARVEST, "作物未成熟或已收获", SoundType.FAIL);

            NativeWebSocketServer.pushToPlayer(playerId, msg);
            return msg;
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
            // 可以继续生长，季节+1
            land.setGrowthSeason(land.getGrowthSeason() + 1);
            // 进入种子阶段
            land.setGrowthCharacteristic(
                    growthCharacteristicRepository
                            .findBySeed_IdAndCropStatusOrderByGrowthStageAsc(seed.getId(), CropStatus.SEED)
                            .stream().findFirst().orElseThrow(
                                    () -> new ResourceNotFoundException("作物无种子阶段")
                            ));

            // 重新设置可收获数量
            land.setHarvestableQuantity(seed.getHarvestYield());
        } else {
            // 作物完全收割，进入枯草阶段
            land.setGrowthCharacteristic(
                    growthCharacteristicRepository
                            .findBySeed_IdAndCropStatusOrderByGrowthStageAsc(seed.getId(), CropStatus.HARVESTED)
                            .stream().findFirst().orElseThrow(
                                    () -> new ResourceNotFoundException("作物无枯草阶段")
                            ));
        }

        PlayerLand playerLand = playerLandRepository.save(land);

        Message<PlayerLandDTO> msg = Message.of(
                1, FarmAction.HARVEST, String.format(
                        "收获成功<br>经验值：%d<br>金币：%d<br>总积分：%d",
                        experiencePoints, goldCoins, totalPoints
                ), SoundType.SUCCESS, playerLand.toDTO()
        );

        NativeWebSocketServer.pushToPlayer(playerId, msg);
        return msg;
    }

    @Override
    public Message<?> actionCleanLand(Long landId, Long playerId) {
        PlayerLand land = validatePlayerAndLand(landId, playerId);

        if (land.getGrowthCharacteristic() == null
                || land.getGrowthCharacteristic().getCropStatus() != CropStatus.HARVESTED) {
            Message<?> msg = Message.of(-1, FarmAction.CLEAN, "土地上没有枯草", SoundType.FAIL);

            NativeWebSocketServer.pushToPlayer(playerId, msg);
            return msg;
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

        Message<PlayerLandDTO> msg = Message.of(
                1, FarmAction.CLEAN, String.format(
                        "清理成功<br>经验值：%d<br>总积分：%d",
                        experiencePoints, totalPoints
                ), SoundType.SUCCESS, land.toDTO()
        );

        NativeWebSocketServer.pushToPlayer(playerId, msg);
        return msg;
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