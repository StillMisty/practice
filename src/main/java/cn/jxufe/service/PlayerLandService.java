package cn.jxufe.service;

import cn.jxufe.model.dto.PlayerLandDTO;
import jakarta.servlet.http.HttpSession;

import java.util.List;

public interface PlayerLandService {
    /**
     * 获取玩家的所有土地
     *
     * @param session
     * @return 玩家土地列表
     */
    List<PlayerLandDTO> getPlayerLands(HttpSession session);
}
