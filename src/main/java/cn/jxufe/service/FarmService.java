package cn.jxufe.service;


public interface FarmService {
    /**
     * 播种
     *
     * @param landId   土地ID
     * @param seedId   种子ID
     * @param playerId 玩家ID
     * @return 操作结果消息
     */
    String actionPlant(Long landId, Long seedId, Long playerId);

    /**
     * 收获
     *
     * @param landId   土地ID
     * @param playerId 玩家ID
     * @return 操作结果消息
     */
    String actionKillWorm(Long landId, Long playerId);

    /**
     * 清理土地
     *
     * @param landId   土地ID
     * @param playerId 玩家ID
     * @return 操作结果消息
     */
    String actionHarvest(Long landId, Long playerId);

    /**
     * 清理土地
     *
     * @param landId   土地ID
     * @param playerId 玩家ID
     * @return 操作结果消息
     */
    String actionCleanLand(Long landId, Long playerId);
}