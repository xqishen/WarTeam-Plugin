package com.dtjiang.WarTeam;

import org.bukkit.plugin.java.JavaPlugin;
import com.dtjiang.WarTeam.PlayerExperienceListener;
import com.dtjiang.WarTeam.TeamCommandExecutor;
public class WarTeamPlugin extends JavaPlugin {

    private PlayerExperienceManager experienceManager;
    private TeamManager teamManager;

    @Override
    public void onEnable() {
        experienceManager = new PlayerExperienceManager();
        teamManager = new TeamManager();

        getServer().getPluginManager().registerEvents(new PlayerExperienceListener(experienceManager), this);
        this.getCommand("team").setExecutor(new TeamCommandExecutor(teamManager, experienceManager));

        teamManager.loadTeamsData(); // 加载队伍数据
    }

    @Override
    public void onDisable() {
        teamManager.saveTeamsData(); // 保存队伍数据
    }
}
