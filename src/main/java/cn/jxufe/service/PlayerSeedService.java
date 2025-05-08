package cn.jxufe.service;

import cn.jxufe.model.dto.PlayerSeedDTO;

public interface PlayerSeedService {
    /**
     * 玩家购买种子
     * 
     * @param playerId 玩家ID
     * @param seedId   种子ID
     * @param quantity 购买数量
     * @return 购买后的玩家种子信息
     */
    PlayerSeedDTO buySeed(Long playerId, Long seedId, Integer quantity);
}