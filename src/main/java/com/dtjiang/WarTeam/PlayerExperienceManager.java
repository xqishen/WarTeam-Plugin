package com.dtjiang.WarTeam;

import org.bukkit.entity.Player;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerExperienceManager {
    private Map<UUID, Integer> playerExperience = new HashMap<>();
    private Map<UUID, Integer> playerLevel = new HashMap<>();

    // 添加经验并更新等级
    public void addExperience(Player player, int experience) {
        UUID playerId = player.getUniqueId();
        int currentExp = playerExperience.getOrDefault(playerId, 0);
        currentExp += experience;
        playerExperience.put(playerId, currentExp);

        updateLevel(player, currentExp);
    }

    // 移除经验并更新等级
    public void removeExperience(Player player, int experience) {
        UUID playerId = player.getUniqueId();
        int currentExp = Math.max(playerExperience.getOrDefault(playerId, 0) - experience, 0);
        playerExperience.put(playerId, currentExp);

        updateLevel(player, currentExp);
    }

    // 根据经验计算等级
    private int calculateLevel(int experience) {
        int level = 1;
        int expForNextLevel = 500;

        while (experience >= expForNextLevel) {
            level++;
            expForNextLevel += 50 * level;
        }

        return level;
    }

    // 更新玩家等级
    private void updateLevel(Player player, int currentExp) {
        int newLevel = calculateLevel(currentExp);
        UUID playerId = player.getUniqueId();
        int currentLevel = playerLevel.getOrDefault(playerId, 0);

        if (newLevel > currentLevel) {
            playerLevel.put(playerId, newLevel);
            player.sendMessage("恭喜" + player.getName() + "升级，目前等级为" + newLevel + "。");
        }
    }

    // 获取玩家当前等级
    public int getLevel(Player player) {
        return playerLevel.getOrDefault(player.getUniqueId(), 0);
    }
}
