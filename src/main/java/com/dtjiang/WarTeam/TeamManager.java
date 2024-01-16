package com.dtjiang.WarTeam;

import org.bukkit.entity.Player;
import org.bukkit.Bukkit;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import com.dtjiang.WarTeam.Team;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Collections;

public class TeamManager {
    private Map<String, Team> teams = new HashMap<>();

    private Map<String, Set<String>> joinRequests = new HashMap<>(); // 保存队伍加入请求

    // 创建队伍
    public void createTeam(Player player, String teamName) {
        if (teams.containsKey(teamName)) {
            player.sendMessage("队伍已存在。");
            return;
        }
        Team newTeam = new Team(teamName, player.getName());
        teams.put(teamName, newTeam);
        player.sendMessage("队伍 " + teamName + " 创建成功！");
    }

    // 添加成员
    public void addMember(String teamName, Player player) {
        Team team = teams.get(teamName);
        if (team != null) {
            team.addMember(player.getName());
            player.sendMessage("你已加入队伍 " + teamName + "。");
        }
    }

    // 移除成员
    public void removeMember(String teamName, Player player) {
        Team team = teams.get(teamName);
        if (team != null) {
            team.removeMember(player.getName());
            player.sendMessage("你已离开队伍 " + teamName + "。");
        }
    }

    // 设置队长
    public void setLeader(String teamName, Player player) {
        Team team = teams.get(teamName);
        if (team != null) {
            team.setLeader(player.getName());
            player.sendMessage("你现在是队伍 " + teamName + " 的队长。");
        }
    }

    // 保存队伍数据
    public void saveTeamsData() {
        JSONObject teamsData = new JSONObject();
        for (Map.Entry<String, Team> entry : teams.entrySet()) {
            String teamName = entry.getKey();
            Team team = entry.getValue();
            JSONObject teamData = new JSONObject();
            teamData.put("leader", team.getLeader());
            // 手动构建 viceLeaders 的 JSONArray
            JSONArray viceLeadersArray = new JSONArray();
            for (String viceLeader : team.getViceLeaders()) {
                viceLeadersArray.add(viceLeader);
            }
            teamData.put("viceLeaders", viceLeadersArray);
            // 手动构建 members 的 JSONArray
            JSONArray membersArray = new JSONArray();
            for (String member : team.getMembers()) {
                membersArray.add(member);
            }
            teamData.put("members", membersArray);

            teamsData.put(teamName, teamData);
        }

        try (PrintWriter writer = new PrintWriter(new File("teamsData.json"))) {
            writer.print(teamsData.toJSONString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    // 加载队伍数据
    public void loadTeamsData() {
        File dataFile = new File("teamsData.json");
        if (!dataFile.exists()) {
            // 如果文件不存在，不需要加载任何数据
            return;
        }

        JSONParser parser = new JSONParser();
        try (FileReader reader = new FileReader("teamsData.json")) {
            JSONObject teamsData = (JSONObject) parser.parse(reader);
            for (Object key : teamsData.keySet()) {
                String teamName = (String) key;
                JSONObject teamData = (JSONObject) teamsData.get(teamName);
                String leader = (String) teamData.get("leader");
                Team team = new Team(teamName, leader);

                JSONArray viceLeaders = (JSONArray) teamData.get("viceLeaders");
                for (Object viceLeader : viceLeaders) {
                    team.addViceLeader((String) viceLeader);
                }

                JSONArray members = (JSONArray) teamData.get("members");
                for (Object member : members) {
                    team.addMember((String) member);
                }

                teams.put(teamName, team);
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    // 获取队伍
    public Team getTeam(String teamName) {
        return teams.get(teamName);
    }

    public void requestJoinTeam(String teamName, Player player) {
        if (!teams.containsKey(teamName)) {
            player.sendMessage("队伍不存在。");
            return;
        }
        joinRequests.computeIfAbsent(teamName, k -> new HashSet<>()).add(player.getName());
        player.sendMessage("已向队伍 " + teamName + " 发送加入申请。");
    }

    public Set<String> getJoinRequests(String teamName) {
        return joinRequests.getOrDefault(teamName, Collections.emptySet());
    }

    public void acceptJoinRequest(String teamName, String playerName) {
        Set<String> requests = joinRequests.getOrDefault(teamName, new HashSet<>());
        if (requests.contains(playerName)) {
            addMember(teamName, Bukkit.getPlayer(playerName));
            requests.remove(playerName);
        }
    }
// 其他必要的方法...
}