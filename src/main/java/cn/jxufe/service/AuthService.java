package cn.jxufe.service;

import cn.jxufe.model.dto.PlayerDTO;
import jakarta.servlet.http.HttpSession;

public interface AuthService {
    /**
     * 设置玩家信息到会话中
     *
     * @param session
     * @param playerId 玩家ID
     */
    PlayerDTO setPlayerInfo(HttpSession session, Long playerId);

    /**
     * 获取会话中的玩家信息
     *
     * @param session
     * @return 玩家信息
     */
    PlayerDTO getPlayerInfo(HttpSession session);
}

