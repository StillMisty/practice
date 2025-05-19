package cn.jxufe.service.task;

import cn.jxufe.model.dto.Message;
import cn.jxufe.model.entity.PlayerLand;
import cn.jxufe.model.enums.CropStatus;
import cn.jxufe.model.enums.FarmAction;
import cn.jxufe.model.enums.SoundType;
import cn.jxufe.repository.GrowthCharacteristicRepository;
import cn.jxufe.repository.PlayerLandRepository;
import cn.jxufe.ws.NativeWebSocketServer;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class FarmManageTask {
    private final PlayerLandRepository playerLandRepository;
    private final GrowthCharacteristicRepository growthCharacteristicRepository;
    private final Random random = new Random();
    private final ObjectMapper objectMapper;

    // 每2秒执行一次
    @Scheduled(fixedRate = 2000)
    public void manageFarm() {
        List<PlayerLand> lands = playerLandRepository.findAll();
        for (PlayerLand land : lands) {
            if (land.getSeed() == null || land.getGrowthCharacteristic() == null)
                continue;
            CropStatus status = land.getGrowthCharacteristic().getCropStatus();
            if (status == CropStatus.HARVESTED || status == CropStatus.READY_TO_HARVEST)
                continue;
            long now = System.currentTimeMillis();
            int growTime = land.getGrowthCharacteristic().getStageGrowthTime(); // 阶段生长时间（秒）
            if (now - land.getPlantingTime() >= growTime * 1000L) {
                String playerId = String.valueOf(land.getPlayer().getId());
                // 进入下一个生长阶段
                CropStatus nextStatus = getNextStatus(status);
                growthCharacteristicRepository.findBySeed_IdAndCropStatus(land.getSeed().getId(), nextStatus)
                        .ifPresent(gc -> {
                            land.setGrowthCharacteristic(gc);

                            // 如果在下一个阶段还是虫害，则减产
                            if (land.isPestInfestation()) {
                                // 随机减产 10% ~ 50%
                                int harvestableQuantity = land.getHarvestableQuantity();
                                int reducedQuantity = (int) (harvestableQuantity * (0.1 + random.nextDouble() * 0.4));
                                land.setHarvestableQuantity(land.getHarvestableQuantity() - reducedQuantity);
                                Message msg = new Message(
                                        -1,
                                        FarmAction.KILL_WORM,
                                        "虫害导致减产，请及时处理！",
                                        SoundType.FAIL,
                                        land.toDTO()
                                );
                                NativeWebSocketServer.pushToPlayer(Long.valueOf(playerId), msg);
                            }

                            // 生虫概率
                            if (!land.isPestInfestation() && gc.getPestInfestationProbability() > 0
                                    && random.nextDouble() < gc.getPestInfestationProbability()) {
                                land.setPestInfestation(true);
                                Message msg = new Message(
                                        -1,
                                        FarmAction.PEST_INFESTATION,
                                        "土地发生虫害，请及时处理！",
                                        SoundType.TAUNT,
                                        land.toDTO()
                                );
                                NativeWebSocketServer.pushToPlayer(Long.valueOf(playerId), msg);
                            }

                            // 推送作物状态更新消息，不显示提示框
                            Message msg = new Message(
                                    1,
                                    FarmAction.CROP_STATUS_UPDATE,
                                    "作物状态更新",
                                    SoundType.SUCCESS,
                                    land.toDTO(),
                                    false
                            );
                            NativeWebSocketServer.pushToPlayer(Long.valueOf(playerId), msg);
                        });
                playerLandRepository.save(land);
            }
        }
    }

    private CropStatus getNextStatus(CropStatus status) {
        if (status == CropStatus.SEED)
            return CropStatus.GROWING;
        if (status == CropStatus.GROWING)
            return CropStatus.READY_TO_HARVEST;
        return CropStatus.HARVESTED;
    }
}
