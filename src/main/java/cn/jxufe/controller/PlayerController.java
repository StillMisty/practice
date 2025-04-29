package cn.jxufe.controller;

import cn.jxufe.model.dto.PlayerDTO;
import cn.jxufe.service.PlayerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/players")
@RequiredArgsConstructor
@Tag(name = "玩家管理", description = "玩家的增删改查接口")
public class PlayerController {

    private final PlayerService playerService;

    @PostMapping
    @Operation(summary = "创建新玩家", description = "添加一个新的玩家信息")
    public ResponseEntity<PlayerDTO> createPlayer(@Valid @RequestBody PlayerDTO playerDTO) {
        return new ResponseEntity<>(playerService.createPlayer(playerDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新玩家信息", description = "根据ID更新已有的玩家信息")
    public ResponseEntity<PlayerDTO> updatePlayer(
            @Parameter(description = "玩家ID") @PathVariable Long id,
            @Valid @RequestBody PlayerDTO playerDTO
    ) {
        return ResponseEntity.ok(playerService.updatePlayer(id, playerDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除玩家", description = "根据ID删除玩家")
    public ResponseEntity<Void> deletePlayer(@Parameter(description = "玩家ID") @PathVariable Long id) {
        playerService.deletePlayer(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取单个玩家信息", description = "根据ID获取玩家详细信息")
    public ResponseEntity<PlayerDTO> getPlayerById(@Parameter(description = "玩家ID") @PathVariable Long id) {
        return ResponseEntity.ok(playerService.getPlayerById(id));
    }

    @GetMapping("/username/{username}")
    @Operation(summary = "根据用户名获取玩家信息", description = "根据用户名获取玩家详细信息")
    public ResponseEntity<PlayerDTO> getPlayerByUsername(
            @Parameter(description = "用户名") @PathVariable String username
    ) {
        return ResponseEntity.ok(playerService.getPlayerByUsername(username));
    }

    @GetMapping
    @Operation(summary = "获取所有玩家", description = "获取系统中所有玩家的列表")
    public ResponseEntity<List<PlayerDTO>> getAllPlayers() {
        return ResponseEntity.ok(playerService.getAllPlayers());
    }

    @GetMapping("/search")
    @Operation(summary = "按显示名称搜索玩家", description = "根据显示名称进行模糊搜索")
    public ResponseEntity<List<PlayerDTO>> searchPlayersByDisplayName(
            @Parameter(description = "显示名称关键字") @RequestParam String displayName
    ) {
        return ResponseEntity.ok(playerService.searchPlayersByDisplayName(displayName));
    }

    @GetMapping("/ranking/experience")
    @Operation(summary = "获取经验值排行榜", description = "获取按经验值排序的玩家列表")
    public ResponseEntity<List<PlayerDTO>> getPlayersByExperienceRanking() {
        return ResponseEntity.ok(playerService.getPlayersByExperienceRanking());
    }

    @GetMapping("/ranking/gold")
    @Operation(summary = "获取金币排行榜", description = "获取按金币排序的玩家列表")
    public ResponseEntity<List<PlayerDTO>> getPlayersByGoldCoinsRanking() {
        return ResponseEntity.ok(playerService.getPlayersByGoldCoinsRanking());
    }

    @GetMapping("/ranking/points")
    @Operation(summary = "获取积分排行榜", description = "获取按总积分排序的玩家列表")
    public ResponseEntity<List<PlayerDTO>> getPlayersByPointsRanking() {
        return ResponseEntity.ok(playerService.getPlayersByPointsRanking());
    }

    @GetMapping("/with-seed/{seedId}")
    @Operation(summary = "获取拥有特定种子的玩家", description = "获取拥有指定种子的所有玩家")
    public ResponseEntity<List<PlayerDTO>> getPlayersWithSeed(
            @Parameter(description = "种子ID") @PathVariable Long seedId
    ) {
        return ResponseEntity.ok(playerService.getPlayersWithSeed(seedId));
    }

    @PostMapping("/{playerId}/seeds/{seedId}")
    @Operation(summary = "为玩家添加种子", description = "将指定种子添加到玩家的拥有列表中")
    public ResponseEntity<PlayerDTO> addSeedToPlayer(
            @Parameter(description = "玩家ID") @PathVariable Long playerId,
            @Parameter(description = "种子ID") @PathVariable Long seedId
    ) {
        return ResponseEntity.ok(playerService.addSeedToPlayer(playerId, seedId));
    }

    @DeleteMapping("/{playerId}/seeds/{seedId}")
    @Operation(summary = "从玩家移除种子", description = "从玩家的拥有列表中移除指定种子")
    public ResponseEntity<PlayerDTO> removeSeedFromPlayer(
            @Parameter(description = "玩家ID") @PathVariable Long playerId,
            @Parameter(description = "种子ID") @PathVariable Long seedId
    ) {
        return ResponseEntity.ok(playerService.removeSeedFromPlayer(playerId, seedId));
    }

    @GetMapping("/{playerId}/seeds")
    @Operation(summary = "获取玩家拥有的所有种子ID", description = "获取指定玩家拥有的所有种子ID列表")
    public ResponseEntity<Set<Long>> getPlayerOwnedSeedIds(
            @Parameter(description = "玩家ID") @PathVariable Long playerId
    ) {
        return ResponseEntity.ok(playerService.getPlayerOwnedSeedIds(playerId));
    }

    @GetMapping("/check-username")
    @Operation(summary = "检查用户名是否已存在", description = "检查指定用户名是否已被其他玩家使用")
    public ResponseEntity<Boolean> isUsernameExists(
            @Parameter(description = "用户名") @RequestParam String username
    ) {
        return ResponseEntity.ok(playerService.isUsernameExists(username));
    }
}