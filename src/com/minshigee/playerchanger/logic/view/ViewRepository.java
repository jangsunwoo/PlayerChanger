package com.minshigee.playerchanger.logic.view;

import com.minshigee.playerchanger.PlayerChanger;
import com.minshigee.playerchanger.domain.GameState;
import com.minshigee.playerchanger.domain.Participant;
import com.minshigee.playerchanger.domain.Role;
import com.minshigee.playerchanger.domain.annotation.IsRepository;
import com.minshigee.playerchanger.domain.module.Repository;
import com.minshigee.playerchanger.logic.game.GameData;
import com.minshigee.playerchanger.logic.view.domain.ShowType;
import com.minshigee.playerchanger.logic.view.domain.ViewTask;
import com.mojang.datafixers.util.Pair;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.ArrayList;


@IsRepository
public class ViewRepository extends Repository<ViewData> {
    public ViewRepository(ViewData localDB, Integer viewCode) {
        super(localDB, viewCode);
        createViewScheduler();
    }
    public static int curTime = 0; // 1 == 0.1sec

    private void createViewScheduler(){
        new BukkitRunnable(){
            @Override
            public void run() {
                curTime += 1;
                excuteViewTask(curTime);
                if(curTime % 10 == 0){
                    updateUserScoreboards();
                }
            }
        }.runTaskTimer(PlayerChanger.singleton, 0, 2);
    }
    private void excuteViewTask(int time){if(localDB.getViewTask() == null || localDB.getViewTask().getStartTime() > time) return;ViewTask task = localDB.getAndPopTask();task.executeTask();}
    public void addViewTask(int laterTime, ShowType type, Player[] players, String msg) {localDB.addViewTask(new ViewTask(curTime + laterTime, type, players, msg));}
    public void stopScoreboardData(){for(int i = 0; i < 5; i++) localDB.clearNumOfScoreboard(i);GameData.getParticipants().forEach(participant -> {participant.getPlayer().setScoreboard(localDB.scoreboardManager.getMainScoreboard());});}
    public void addScoreboardData(int num, Pair<String,String> data){localDB.registerStringScoreboard(num,data);}
    public void updateScoreboardData(int num, int code, Pair<String,String> data){localDB.updateScoreboardData(num,code,data);}
    public void clearScoreboardData(int num){localDB.clearNumOfScoreboard(num);}

    private void updateUserScoreboards(){GameData.getParticipants().forEach(this::updateUserScoreboard);}
    private void updateUserScoreboard(Participant participant){
        int cnt = 0;
        Scoreboard scoreboard = localDB.scoreboardManager.getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective(participant.getPlayer().getName(), "", ChatColor.DARK_AQUA + "Player Changer");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.getScore(ChatColor.DARK_GRAY + "==================").setScore(cnt--);
        objective.getScore( "게임 상태: " + ChatColor.GREEN + "%s".formatted(GameData.getGameState().name())).setScore(cnt--);
        objective.getScore( "게임 타임: " + ChatColor.GREEN + "%dsec".formatted(curTime/10)).setScore(cnt--);
        objective.getScore("유저 역할: " + ChatColor.GREEN + "%s".formatted(participant.getRole().name())).setScore(cnt--);
        objective.getScore(ChatColor.DARK_GRAY + "=================-").setScore(cnt--);
        for(int i = 0; i < 5; i++){
            ArrayList<Pair<String,String>> data = localDB.getNumOfScoreboard(i);
            if(data.size() == 0)
                continue;
            for(Pair<String,String> pair : data){
                objective.getScore("%s %s".formatted(pair.getFirst(),pair.getSecond())).setScore(cnt--);
            }
            objective.getScore(ChatColor.DARK_GRAY + "================%d".formatted(i)).setScore(cnt--);
        }
        objective.getScore("남은 유저: %d명".formatted(GameData.getParticipantsAlive().size()));
        participant.getPlayer().setScoreboard(scoreboard);
    }

    public void playSoundAllParticipants(Sound sound){
        GameData.getParticipants().stream().map(Participant::getPlayer).forEach(p -> {
            p.playSound(p.getLocation(), sound,1,1);
        });
    }
}
