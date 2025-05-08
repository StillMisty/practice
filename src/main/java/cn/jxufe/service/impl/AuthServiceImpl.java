package cn.jxufe.service.impl;

import cn.jxufe.exception.ResourceNotFoundException;
import cn.jxufe.model.dto.PlayerDTO;
import cn.jxufe.service.AuthService;
import cn.jxufe.service.PlayerService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final PlayerService PlayerService;

    @Override
    public PlayerDTO setPlayerInfo(HttpSession session, Long playerId) {
        PlayerDTO playerDTO = PlayerService.getPlayerById(playerId);
        session.setAttribute("player", playerDTO);
        return playerDTO;
    }

    @Override
    public PlayerDTO getPlayerInfo(HttpSession session) {
        Object player = session.getAttribute("player");
        if (player == null) {
            throw new ResourceNotFoundException("玩家未登录");
        }
        return (PlayerDTO) player;
    }
}

