package cn.jxufe.controller;

import cn.jxufe.model.dto.PlayerLandDTO;
import cn.jxufe.service.PlayerLandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("/api/play-lands")
@RequiredArgsConstructor
@Tag(name = "玩家土地管理", description = "玩家土地相关接口")
public class PlayerLandController {

    private final PlayerLandService playerLandService;

    @GetMapping
    @Operation(summary = "获取玩家所有土地", description = "获取登陆用户的所有土地")
    public List<PlayerLandDTO> getPlayLands(HttpSession session) {
        return playerLandService.getPlayerLands(session);
    }
}
