package cn.jxufe.service;

import cn.jxufe.model.dto.Message;
import cn.jxufe.model.dto.PlayerLandDTO;

import java.util.List;

public interface FarmService {

    /**
     * 获取玩家的所有土地
     *
     * @param playerId 玩家ID
     * @return 玩家土地列表
     */
    List<PlayerLandDTO> getPlayerLands(Long playerId);

    /**
     * 播种
     *
     * @param landId   土地ID
     * @param seedId   种子ID
     * @param playerId 玩家ID
     * @return 操作结果消息
     */
    Message actionPlant(Long landId, Long seedId, Long playerId);

    /**
     * 清理虫害
     *
     * @param landId   土地ID
     * @param playerId 玩家ID
     * @return 操作结果消息
     */
    Message actionKillWorm(Long landId, Long playerId);

    /**
     * 收获作物
     *
     * @param landId   土地ID
     * @param playerId 玩家ID
     * @return 操作结果消息
     */
    Message actionHarvest(Long landId, Long playerId);

    /**
     * 清理土地
     *
     * @param landId   土地ID
     * @param playerId 玩家ID
     * @return 操作结果消息
     */
    Message actionCleanLand(Long landId, Long playerId);
}