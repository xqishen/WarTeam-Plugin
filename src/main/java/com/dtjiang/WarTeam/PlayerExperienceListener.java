package com.dtjiang.WarTeam;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerFishEvent;

public class PlayerExperienceListener implements Listener {
    private PlayerExperienceManager experienceManager;

    public PlayerExperienceListener(PlayerExperienceManager manager) {
        this.experienceManager = manager;
    }

    // 处理挖掘矿石事件
    @EventHandler
    public void onMine(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Material blockType = event.getBlock().getType();

        switch (blockType) {
            case COAL_ORE:
            case COPPER_ORE:
                experienceManager.addExperience(player, 5);
                break;
            case IRON_ORE:
                experienceManager.addExperience(player, 6);
                break;
            case GOLD_ORE:
                experienceManager.addExperience(player, 8);
                break;
            case LAPIS_ORE:
            case REDSTONE_ORE:
                experienceManager.addExperience(player, 10);
                break;
            case DIAMOND_ORE:
            case EMERALD_ORE:
                experienceManager.addExperience(player, 16);
                break;
            default:
                break;
        }
    }

    // 处理击杀怪物事件
    @EventHandler
    public void onEntityKill(EntityDeathEvent event) {
        if (event.getEntity().getKiller() instanceof Player) {
            Player player = event.getEntity().getKiller();

            switch (event.getEntityType()) {
                case ZOMBIE:
                case SKELETON:
                case SPIDER:
                    experienceManager.addExperience(player, 5);
                    break;
                case ENDERMAN:
                case CREEPER:
                    experienceManager.addExperience(player, 10);
                    break;
                case WITCH:
                case WITHER_SKELETON:
                    experienceManager.addExperience(player, 15);
                    break;
                case ENDER_DRAGON:
                case WITHER:
                    experienceManager.addExperience(player, 50);
                    break;
                default:
                    break;
            }
        }
    }

    // 处理采集农作物事件
    @EventHandler
    public void onHarvest(BlockBreakEvent event) {
        if (event.getBlock().getType() == Material.WHEAT && event.getBlock().getData() == 7) {
            experienceManager.addExperience(event.getPlayer(), 3);
        }
    }

    // 处理钓鱼事件
    @EventHandler
    public void onFish(PlayerFishEvent event) {
        if (event.getState() == PlayerFishEvent.State.CAUGHT_FISH) {
            experienceManager.addExperience(event.getPlayer(), 2);
        }
    }
}
