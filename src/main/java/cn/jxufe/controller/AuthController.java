package cn.jxufe.controller;

import cn.jxufe.model.dto.PlayerDTO;
import cn.jxufe.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/set-player")
    @Operation(summary = "设置玩家信息", description = "将玩家ID设置到会话中")
    public PlayerDTO setPlayer(
            HttpSession session,
            @RequestBody Long playerId
    ) {
        return authService.setPlayerInfo(session, playerId);
    }

    @PostMapping("/get-player")
    @Operation(summary = "获取玩家信息", description = "从会话中获取玩家信息")
    public PlayerDTO getPlayer(HttpSession session) {
        return authService.getPlayerInfo(session);
    }
}
