package com.dtjiang.WarTeam;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.util.Set;
import com.dtjiang.WarTeam.Team;

public class TeamCommandExecutor implements CommandExecutor {
    private TeamManager teamManager;
    private PlayerExperienceManager experienceManager;

    public TeamCommandExecutor(TeamManager teamManager, PlayerExperienceManager experienceManager) {
        this.teamManager = teamManager;
        this.experienceManager = experienceManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "只有玩家可以使用这个命令。");
            return true;
        }

        Player player = (Player) sender;
        if (args.length == 0) {
            player.sendMessage(ChatColor.YELLOW + "请指定一个子命令。");
            return false;
        }

        String subCommand = args[0].toLowerCase();
        switch (subCommand) {
            case "create":
                return handleCreateTeam(player, args);
            case "join":
                return handleJoinTeam(player, args);
            case "leave":
                return handleLeaveTeam(player, args);
            case "info":
                return handleTeamInfo(player, args);
            case "level":
                return handleLevelCommand(player);
            case "requests":
                return handleViewRequests(player, args);
            case "accept":
                return handleAcceptJoinRequest(player, args);
            case "reject":
                return handleRejectJoinRequest(player, args);
            // 其他子命令...
            default:
                player.sendMessage(ChatColor.RED + "未知命令。");
                return false;
        }
    }

    private boolean handleCreateTeam(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(ChatColor.YELLOW + "请指定队伍名称。");
            return true;
        }
        teamManager.createTeam(player, args[1]);
        return true;
    }

    private boolean handleJoinTeam(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(ChatColor.YELLOW + "请指定队伍名称。");
            return true;
        }
        teamManager.addMember(args[1], player);
        return true;
    }

    private boolean handleLeaveTeam(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(ChatColor.YELLOW + "请指定队伍名称。");
            return true;
        }
        teamManager.removeMember(args[1], player);
        return true;
    }

    private boolean handleTeamInfo(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(ChatColor.YELLOW + "请指定队伍名称。");
            return true;
        }
        Team team = teamManager.getTeam(args[1]);
        if (team != null) {
            player.sendMessage(ChatColor.GREEN + "队伍名: " + team.getName());
            player.sendMessage(ChatColor.GREEN + "队长: " + team.getLeader() + " 等级: " + experienceManager.getLevel(Bukkit.getPlayer(team.getLeader())));
            player.sendMessage(ChatColor.GREEN + "副队长: " + String.join(", ", team.getViceLeaders()));
            for (String viceLeader : team.getViceLeaders()) {
                Player vLeaderPlayer = Bukkit.getPlayer(viceLeader);
                if (vLeaderPlayer != null) {
                    player.sendMessage(ChatColor.GREEN + viceLeader + " 等级: " + experienceManager.getLevel(vLeaderPlayer));
                }
            }
            double averageLevel = team.calculateAverageLevel(experienceManager);
            player.sendMessage(ChatColor.GREEN + "队伍成员平均等级: " + averageLevel);
        } else {
            player.sendMessage(ChatColor.RED + "队伍不存在。");
        }
        return true;
    }

    private boolean handleLevelCommand(Player player) {
        int level = experienceManager.getLevel(player);
        player.sendMessage(ChatColor.GREEN + "你的等级是: " + level);
        return true;
    }

    private boolean handleViewRequests(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(ChatColor.YELLOW + "请指定队伍名称。");
            return true;
        }
        Set<String> requests = teamManager.getJoinRequests(args[1]);
        if (requests.isEmpty()) {
            player.sendMessage(ChatColor.YELLOW + "没有待处理的加入申请。");
        } else {
            player.sendMessage(ChatColor.GREEN + "加入申请列表:");
            for (String request : requests) {
                player.sendMessage(ChatColor.GREEN + "- " + request);
            }
        }
        return true;
    }

    private boolean handleRejectJoinRequest(Player player, String[] args) {
        if (args.length < 3) {
            player.sendMessage(ChatColor.YELLOW + "请指定队伍名称和玩家名。");
            return true;
        }
        // 从申请列表中移除玩家的逻辑
        return true;
    }

    private boolean handleAcceptJoinRequest(Player player, String[] args) {
        if (args.length < 3) {
            player.sendMessage(ChatColor.YELLOW + "用法: /team accept <teamName> <playerName>");
            return true;
        }

        String teamName = args[1];
        String playerName = args[2];

        Team team = teamManager.getTeam(teamName);
        if (team == null) {
            player.sendMessage(ChatColor.RED + "队伍不存在。");
            return true;
        }

        // 确保执行命令的玩家是队长或副队长
        if (!team.getLeader().equals(player.getName()) && !team.getViceLeaders().contains(player.getName())) {
            player.sendMessage(ChatColor.RED + "你没有权限接受加入请求。");
            return true;
        }

        if (teamManager.getJoinRequests(teamName).contains(playerName)) {
            teamManager.acceptJoinRequest(teamName, playerName);
            player.sendMessage(ChatColor.GREEN + "已接受 " + playerName + " 加入队伍。");
            // 可以添加逻辑通知被接受的玩家
        } else {
            player.sendMessage(ChatColor.RED + "没有来自 " + playerName + " 的加入请求。");
        }
        return true;
    }
// 其他子命令的处理方法...
}