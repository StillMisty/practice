package cn.jxufe.service.impl;

import cn.jxufe.exception.ResourceNotFoundException;
import cn.jxufe.model.dto.PlayerSeedDTO;
import cn.jxufe.model.entity.Player;
import cn.jxufe.model.entity.PlayerSeed;
import cn.jxufe.model.entity.Seed;
import cn.jxufe.repository.PlayerRepository;
import cn.jxufe.repository.PlayerSeedRepository;
import cn.jxufe.repository.SeedRepository;
import cn.jxufe.service.PlayerSeedService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlayerSeedServiceImpl implements PlayerSeedService {

    private final PlayerRepository playerRepository;
    private final SeedRepository seedRepository;
    private final PlayerSeedRepository playerSeedRepository;

    @Override
    @Transactional
    public PlayerSeedDTO buySeed(Long playerId, Long seedId, Integer quantity) {
        Player player = playerRepository.findById(playerId).orElseThrow(() -> new ResourceNotFoundException("找不到玩家"));

        Seed seed = seedRepository.findById(seedId).orElseThrow(() -> new ResourceNotFoundException("找不到种子"));

        // 计算总价
        Long totalCost = seed.getSeedPurchasePrice() * quantity;

        // 检查玩家金币是否足够
        if (player.getGoldCoins() < totalCost) {
            throw new IllegalStateException("金币不足");
        }

        // 扣除金币
        player.setGoldCoins(player.getGoldCoins() - totalCost);
        playerRepository.save(player);

        // 添加或更新玩家的种子数量
        PlayerSeed playerSeed = playerSeedRepository.findByPlayerIdAndSeedId(playerId, seedId).orElse(new PlayerSeed());

        if (playerSeed.getId() == null) {
            playerSeed.setPlayer(player);
            playerSeed.setSeed(seed);
            playerSeed.setQuantity(quantity);
        } else {
            playerSeed.setQuantity(playerSeed.getQuantity() + quantity);
        }

        playerSeed = playerSeedRepository.save(playerSeed);

        // 返回DTO
        return PlayerSeedDTO.builder().id(playerSeed.getId()).playerId(playerId).seedId(seedId).quantity(playerSeed.getQuantity()).build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlayerSeedDTO> getPlayerSeeds(Long playerId) {
        return playerSeedRepository.findByPlayerId(playerId)
                .stream()
                .map(playerSeed ->
                             PlayerSeedDTO.builder()
                                     .id(playerSeed.getId())
                                     .playerId(playerSeed.getPlayer().getId())
                                     .seedId(playerSeed.getSeed().getId())
                                     .quantity(playerSeed.getQuantity()).build())
                .collect(Collectors.toList());
    }
}