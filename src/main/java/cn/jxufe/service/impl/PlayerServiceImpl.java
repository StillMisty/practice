package cn.jxufe.service.impl;

import cn.jxufe.exception.ResourceNotFoundException;
import cn.jxufe.model.dto.PlayerDTO;
import cn.jxufe.model.entity.Player;
import cn.jxufe.model.entity.PlayerLand;
import cn.jxufe.model.enums.LandType;
import cn.jxufe.repository.PlayerLandRepository;
import cn.jxufe.repository.PlayerRepository;
import cn.jxufe.service.FileStorageService;
import cn.jxufe.service.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;
    private final FileStorageService fileStorageService;
    private final PlayerLandRepository playerLandRepository;

    @Override
    public PlayerDTO createPlayer(PlayerDTO playerDTO) {
        if (playerRepository.existsByUsername(playerDTO.getUsername())) {
            throw new IllegalArgumentException("用户名已存在：" + playerDTO.getUsername());
        }

        Player player = convertToEntity(playerDTO);
        Player savedPlayer = playerRepository.save(player);

        // 生成地块
        // 每个玩家初始每种土地类型各六个地块
        int landCount = 6;
        List<PlayerLand> playerLands = new ArrayList<>();
        Arrays.stream(LandType.values()).forEach(landType -> {
            for (int i = 0; i < landCount; i++) {
                PlayerLand playerLand = new PlayerLand();
                playerLand.setPlayer(player);
                playerLand.setLandType(landType);
                playerLands.add(playerLand);
            }
        });
        playerLandRepository.saveAll(playerLands);

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

        Player updatedPlayer = playerRepository.save(existingPlayer);
        return convertToDTO(updatedPlayer);
    }

    @Override
    public PlayerDTO updatePlayerAvatar(Long id, MultipartFile file) throws IOException {
        Player existingPlayer = playerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("玩家不存在，ID: " + id));

        // 删除旧头像
        fileStorageService.deleteFile(existingPlayer.getAvatarPath());

        // 上传新头像
        String avatarPath = fileStorageService.storeFile(file, "avatars");

        existingPlayer.setAvatarPath(avatarPath);

        Player updatedPlayer = playerRepository.save(existingPlayer);
        return convertToDTO(updatedPlayer);
    }

    @Override
    @Transactional(readOnly = true)
    public Path getPlayerAvatar(Long id) {
        Player player = playerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("玩家不存在，ID: " + id));
        return player.getAvatarPath() == null ? null : fileStorageService.getFilePath(player.getAvatarPath());
    }

    @Override
    public boolean deletePlayerAvatar(Long id) {
        Player existingPlayer = playerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("玩家不存在，ID: " + id));

        // 删除旧头像
        return fileStorageService.deleteFile(existingPlayer.getAvatarPath());
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
    public Page<PlayerDTO> getAllPlayers(Pageable pageable) {
        return playerRepository.findAll(pageable)
                .map(this::convertToDTO);
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
        return playerRepository.findAllByOrderByExperiencePointsDesc().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlayerDTO> getPlayersByGoldCoinsRanking() {
        return playerRepository.findAllByOrderByGoldCoinsDesc().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlayerDTO> getPlayersByPointsRanking() {
        return playerRepository.findAllByOrderByTotalPointsDesc().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
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
}