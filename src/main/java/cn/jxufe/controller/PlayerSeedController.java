package cn.jxufe.controller;

import cn.jxufe.model.dto.PlayerDTO;
import cn.jxufe.service.AuthService;
import cn.jxufe.service.PlayerSeedService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/player-seeds")
@RequiredArgsConstructor
@Tag(name = "玩家种子管理", description = "玩家购买和管理种子的接口")
public class PlayerSeedController {

    private final PlayerSeedService playerSeedService;
    private final AuthService authService;

    @GetMapping("/list")
    @Operation(summary = "获取玩家种子列表", description = "获取当前登录玩家拥有的所有种子")
    public ResponseEntity<?> getPlayerSeeds(HttpSession session) {
        PlayerDTO currentPlayer = authService.getPlayerInfo(session);
        return ResponseEntity.ok(playerSeedService.getPlayerSeeds(currentPlayer.getId()));
    }

    @PostMapping("/buy/{seedId}")
    @Operation(summary = "购买种子", description = "玩家购买指定的种子")
    public ResponseEntity<?> buySeed(
            HttpSession session,
            @Parameter(description = "种子ID") @PathVariable Long seedId,
            @Parameter(description = "购买数量") @RequestParam(defaultValue = "1") Integer quantity) {
        PlayerDTO currentPlayer = authService.getPlayerInfo(session);
        return ResponseEntity.ok(playerSeedService.buySeed(currentPlayer.getId(), seedId, quantity));
    }
}
