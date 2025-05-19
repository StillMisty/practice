package cn.jxufe.service.impl;

import cn.jxufe.model.dto.PlayerDTO;
import cn.jxufe.model.dto.PlayerLandDTO;
import cn.jxufe.model.entity.PlayerLand;
import cn.jxufe.repository.PlayerRepository;
import cn.jxufe.service.AuthService;
import cn.jxufe.service.PlayerLandService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlayerLandServiceImpl implements PlayerLandService {

    private final PlayerRepository playerRepository;
    private final AuthService authService;

    @Transactional(readOnly = true)
    @Override
    public List<PlayerLandDTO> getPlayerLands(HttpSession session) {
        PlayerDTO player = authService.getPlayerInfo(session);
        return playerRepository.findById(player.getId())
                .orElseThrow(() -> new IllegalArgumentException("玩家不存在"))
                .getLands()
                .stream()
                .sorted(Comparator.comparing(PlayerLand::getId)) // 按照 ID 升序排序
                .map(playerLand -> new PlayerLandDTO(
                        playerLand.getId(),
                        playerLand.getLandType(),
                        playerLand.getSeed() != null ? playerLand.getSeed().toDTO() : null,
                        playerLand.getGrowthCharacteristic() != null ? playerLand.getGrowthCharacteristic().toDTO() : null,
                        playerLand.isPestInfestation(),
                        playerLand.getPlantingTime(),
                        playerLand.getHarvestableQuantity(),
                        playerLand.getGrowthSeason()
                ))
                .collect(Collectors.toList());
    }
}
