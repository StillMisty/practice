package cn.jxufe.service;

import cn.jxufe.model.dto.PlayerDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;

public interface PlayerService {

    // 创建新玩家
    PlayerDTO createPlayer(PlayerDTO playerDTO);
    
    // 更新玩家信息
    PlayerDTO updatePlayer(Long id, PlayerDTO playerDTO);

    // 更新玩家头像
    PlayerDTO updatePlayerAvatar(Long id, MultipartFile file) throws IOException;

    // 获取玩家头像
    Path getPlayerAvatar(Long id);

    // 删除玩家头像
    boolean deletePlayerAvatar(Long id);
    
    // 删除玩家
    void deletePlayer(Long id);
    
    // 根据ID获取玩家信息
    PlayerDTO getPlayerById(Long id);
    
    // 根据用户名获取玩家信息
    PlayerDTO getPlayerByUsername(String username);
    
    // 获取所有玩家
    Page<PlayerDTO> getAllPlayers(Pageable pageable);
    
    // 根据显示名称搜索玩家
    List<PlayerDTO> searchPlayersByDisplayName(String displayName);
    
    // 查找经验值排行榜
    List<PlayerDTO> getPlayersByExperienceRanking();
    
    // 查找金币排行榜
    List<PlayerDTO> getPlayersByGoldCoinsRanking();
    
    // 查找积分排行榜
    List<PlayerDTO> getPlayersByPointsRanking();
    
    // 检查用户名是否已存在
    boolean isUsernameExists(String username);
}