package com.minshigee.playerchanger.logic.mission;

import com.minshigee.playerchanger.PlayerChanger;
import com.minshigee.playerchanger.domain.module.Repository;
import com.minshigee.playerchanger.logic.game.GameData;
import com.minshigee.playerchanger.logic.mission.domain.Mission;
import com.minshigee.playerchanger.logic.view.ViewController;
import com.mojang.datafixers.View;
import com.mojang.datafixers.util.Pair;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.scheduler.BukkitRunnable;

public class MissionRepository extends Repository<MissionData> {
    public MissionRepository(MissionData localDB, Integer viewCode) {
        super(localDB,viewCode);
    }

    public void resetMissions(){
        ViewController.singleton.clearViewScoreboard(viewCode);
        localDB.resetMissions();
        registerScoreboard();
        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "미션을 세팅합니다.");
    }
    public void clearMissions(){
        localDB.clearMissions();
    }

    public<T> void updateMissions(T event){
        if(!(event instanceof Event))
            return;
        localDB.getMissions().stream().filter(mission -> {
            if(mission.getClearPlayer() != null)
                return false;
            Player player = mission.updateMission(event);
            if(player == null)
                return false;
            updateScoreboard(mission);
            ViewController.singleton.playSoundAllParticipants(Sound.BLOCK_NOTE_BLOCK_FLUTE);
            validateMission();
            return true;
        }).findFirst();
    }

    private void validateMission(){
        long mCnt = localDB.getMissions().stream().filter(mission -> {
            return mission.getClearPlayer() == null;
        }).count();
        if(mCnt > 0)
            return;
        clearAllMissionAndReset();
    }

    private void clearAllMissionAndReset(){
        GameData.makeNextGameStatus();
        ViewController.singleton.playSoundAllParticipants(Sound.BLOCK_BELL_USE);
        new BukkitRunnable(){
            @Override
            public void run() {
                resetMissions();
            }
        }.runTaskLater(PlayerChanger.singleton, 100);
    }

    private void updateScoreboard(Mission mission){Player player = mission.getClearPlayer();String name = player.getName();String description = ChatColor.STRIKETHROUGH + mission.getDescription();Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_AQUA + description + " 로 업데이트 되었습니다.");ViewController.singleton.updateViewScoreboardString(this.viewCode, mission.getNum(), new Pair(ChatColor.RED + (ChatColor.ITALIC + "%d-Cleared : ").formatted(mission.getNum()), name));}
    private void registerScoreboard(){ViewController.singleton.clearViewScoreboard(viewCode);localDB.getMissions().forEach(mission -> {String description = mission.getDescription();ViewController.singleton.addViewScoreboardString(viewCode, new Pair(ChatColor.BOLD + description, ""));Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_AQUA + description);});}
}
