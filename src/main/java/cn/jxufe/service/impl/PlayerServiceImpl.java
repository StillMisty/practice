package cn.jxufe.service.impl;

import cn.jxufe.exception.ResourceNotFoundException;
import cn.jxufe.model.dto.PlayerDTO;
import cn.jxufe.model.entity.Player;
import cn.jxufe.model.entity.Seed;
import cn.jxufe.repository.PlayerRepository;
import cn.jxufe.repository.SeedRepository;
import cn.jxufe.service.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;
    private final SeedRepository seedRepository;

    @Override
    public PlayerDTO createPlayer(PlayerDTO playerDTO) {
        if (playerRepository.existsByUsername(playerDTO.getUsername())) {
            throw new IllegalArgumentException("用户名已存在：" + playerDTO.getUsername());
        }
        
        Player player = convertToEntity(playerDTO);
        Player savedPlayer = playerRepository.save(player);
        return convertToDTO(savedPlayer);
    }

    @Override
    public PlayerDTO updatePlayer(Long id, PlayerDTO playerDTO) {
        Player existingPlayer = playerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("玩家不存在，ID: " + id));
        
        // 如果更改了用户名，需要检查新用户名是否已存在
        if (!existingPlayer.getUsername().equals(playerDTO.getUsername()) &&
                playerRepository.existsByUsername(playerDTO.getUsername())) {
            throw new IllegalArgumentException("用户名已存在：" + playerDTO.getUsername());
        }
        
        updatePlayerFromDTO(existingPlayer, playerDTO);
        
        // 更新玩家拥有的种子集合
        if (playerDTO.getOwnedSeedIds() != null) {
            updatePlayerSeeds(existingPlayer, playerDTO.getOwnedSeedIds());
        }
        
        Player updatedPlayer = playerRepository.save(existingPlayer);
        return convertToDTO(updatedPlayer);
    }

    @Override
    public void deletePlayer(Long id) {
        if (!playerRepository.existsById(id)) {
            throw new ResourceNotFoundException("玩家不存在，ID: " + id);
        }
        playerRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public PlayerDTO getPlayerById(Long id) {
        Player player = playerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("玩家不存在，ID: " + id));
        return convertToDTO(player);
    }

    @Override
    @Transactional(readOnly = true)
    public PlayerDTO getPlayerByUsername(String username) {
        Player player = playerRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("玩家不存在，用户名: " + username));
        return convertToDTO(player);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlayerDTO> getAllPlayers() {
        return playerRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlayerDTO> searchPlayersByDisplayName(String displayName) {
        return playerRepository.findByDisplayNameContaining(displayName).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlayerDTO> getPlayersByExperienceRanking() {
        return playerRepository.findAllOrderByExperiencePointsDesc().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlayerDTO> getPlayersByGoldCoinsRanking() {
        return playerRepository.findAllOrderByGoldCoinsDesc().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlayerDTO> getPlayersByPointsRanking() {
        return playerRepository.findAllOrderByTotalPointsDesc().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlayerDTO> getPlayersWithSeed(Long seedId) {
        return playerRepository.findPlayersWithSeed(seedId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PlayerDTO addSeedToPlayer(Long playerId, Long seedId) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new ResourceNotFoundException("玩家不存在，ID: " + playerId));
        
        Seed seed = seedRepository.findById(seedId)
                .orElseThrow(() -> new ResourceNotFoundException("种子不存在，ID: " + seedId));
        
        if (player.getOwnedSeeds() == null) {
            player.setOwnedSeeds(new HashSet<>());
        }
        
        player.getOwnedSeeds().add(seed);
        Player updatedPlayer = playerRepository.save(player);
        
        return convertToDTO(updatedPlayer);
    }

    @Override
    public PlayerDTO removeSeedFromPlayer(Long playerId, Long seedId) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new ResourceNotFoundException("玩家不存在，ID: " + playerId));
        
        Seed seed = seedRepository.findById(seedId)
                .orElseThrow(() -> new ResourceNotFoundException("种子不存在，ID: " + seedId));
        
        if (player.getOwnedSeeds() != null) {
            player.getOwnedSeeds().remove(seed);
            playerRepository.save(player);
        }
        
        return convertToDTO(player);
    }

    @Override
    @Transactional(readOnly = true)
    public Set<Long> getPlayerOwnedSeedIds(Long playerId) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new ResourceNotFoundException("玩家不存在，ID: " + playerId));
        
        if (player.getOwnedSeeds() == null) {
            return new HashSet<>();
        }
        
        return player.getOwnedSeeds().stream()
                .map(Seed::getSeedId)
                .collect(Collectors.toSet());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isUsernameExists(String username) {
        return playerRepository.existsByUsername(username);
    }

    // 辅助方法：将 DTO 转换为实体
    private Player convertToEntity(PlayerDTO dto) {
        Player player = new Player();
        updatePlayerFromDTO(player, dto);
        return player;
    }

    // 辅助方法：将实体转换为 DTO
    private PlayerDTO convertToDTO(Player player) {
        PlayerDTO dto = new PlayerDTO();
        dto.setId(player.getId());
        dto.setUsername(player.getUsername());
        dto.setDisplayName(player.getDisplayName());
        dto.setExperiencePoints(player.getExperiencePoints());
        dto.setTotalPoints(player.getTotalPoints());
        dto.setGoldCoins(player.getGoldCoins());
        
        // 收集玩家拥有的种子ID
        if (player.getOwnedSeeds() != null) {
            dto.setOwnedSeedIds(player.getOwnedSeeds().stream()
                    .map(Seed::getSeedId)
                    .collect(Collectors.toSet()));
        } else {
            dto.setOwnedSeedIds(new HashSet<>());
        }
        
        return dto;
    }

    // 辅助方法：从 DTO 更新实体
    private void updatePlayerFromDTO(Player player, PlayerDTO dto) {
        player.setUsername(dto.getUsername());
        player.setDisplayName(dto.getDisplayName());
        player.setExperiencePoints(dto.getExperiencePoints());
        player.setTotalPoints(dto.getTotalPoints());
        player.setGoldCoins(dto.getGoldCoins());
    }
    
    // 辅助方法：更新玩家拥有的种子集合
    private void updatePlayerSeeds(Player player, Set<Long> seedIds) {
        // 创建一个新的种子集合
        Set<Seed> seeds = new HashSet<>();
        
        // 为每个种子ID查找对应的种子实体并添加到集合中
        for (Long seedId : seedIds) {
            seedRepository.findById(seedId).ifPresent(seeds::add);
        }
        
        // 更新玩家的种子集合
        player.setOwnedSeeds(seeds);
    }
}