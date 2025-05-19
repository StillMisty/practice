package cn.jxufe.controller;

import cn.jxufe.model.dto.Message;
import cn.jxufe.model.dto.PlayerLandDTO;
import cn.jxufe.service.AuthService;
import cn.jxufe.service.FarmService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("/api/farm")
@RequiredArgsConstructor
@Tag(name = "玩家农场管理", description = "玩家农场相关接口")
public class FarmController {

    private final AuthService authService;
    private final FarmService farmService;


    @GetMapping("/lands")
    @Operation(summary = "获取玩家所有土地", description = "获取登陆用户的所有土地")
    public List<PlayerLandDTO> getPlayLands(HttpSession session) {
        Long playerId = authService.getPlayerInfo(session).getId();
        return farmService.getPlayerLands(playerId);
    }

    @PostMapping("/plant")
    @Operation(summary = "播种", description = "玩家在指定土地上播种")
    public Message plant(
            @RequestParam Long landId,
            @RequestParam Long seedId,
            HttpSession session
    ) {
        Long playerId = authService.getPlayerInfo(session).getId();
        return farmService.actionPlant(landId, seedId, playerId);
    }

    @PostMapping("/killWorm")
    @Operation(summary = "除虫", description = "玩家在指定土地上除虫")
    public Message killWorm(
            @RequestParam Long landId,
            HttpSession session
    ) {
        Long playerId = authService.getPlayerInfo(session).getId();
        return farmService.actionKillWorm(landId, playerId);
    }

    @PostMapping("/harvest")
    @Operation(summary = "收获", description = "玩家在指定土地上收获")
    public Message harvest(
            @RequestParam Long landId,
            HttpSession session
    ) {
        Long playerId = authService.getPlayerInfo(session).getId();
        return farmService.actionHarvest(landId, playerId);
    }

    @PostMapping("/cleanLand")
    @Operation(summary = "除枯草", description = "玩家在指定土地上除枯草")
    public Message cleanLand(
            @RequestParam Long landId,
            HttpSession session
    ) {
        Long playerId = authService.getPlayerInfo(session).getId();
        return farmService.actionCleanLand(landId, playerId);
    }
}
