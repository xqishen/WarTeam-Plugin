package com.dtjiang.WarTeam;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import java.util.HashSet;
import java.util.Set;

public class Team {
    private String name;
    private String leader;
    private Set<String> viceLeaders;
    private Set<String> members;

    public Team(String name, String leader) {
        this.name = name;
        this.leader = leader;
        this.viceLeaders = new HashSet<>();
        this.members = new HashSet<>();
        this.members.add(leader);
    }

    // 获取队伍名称
    public String getName() {
        return name;
    }

    // 获取队长名字
    public String getLeader() {
        return leader;
    }

    // 设置队长
    public void setLeader(String leader) {
        this.leader = leader;
    }

    // 获取副队长名单
    public Set<String> getViceLeaders() {
        return new HashSet<>(viceLeaders);
    }

    // 添加副队长
    public void addViceLeader(String viceLeader) {
        viceLeaders.add(viceLeader);
    }

    // 移除副队长
    public void removeViceLeader(String viceLeader) {
        viceLeaders.remove(viceLeader);
    }

    // 获取队伍成员
    public Set<String> getMembers() {
        return new HashSet<>(members);
    }

    // 添加成员
    public void addMember(String member) {
        members.add(member);
    }

    // 移除成员
    public void removeMember(String member) {
        members.remove(member);
    }

    public double calculateAverageLevel(PlayerExperienceManager experienceManager) {
        if (members.isEmpty()) {
            return 0;
        }

        double totalLevel = 0;
        for (String memberName : members) {
            Player player = Bukkit.getServer().getPlayer(memberName);
            if (player != null) {
                totalLevel += experienceManager.getLevel(player);
            }
        }
        return totalLevel / members.size();
    }
}
