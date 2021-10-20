package com.minshigee.playerchanger.logic.view;

import com.minshigee.playerchanger.domain.annotation.IsController;
import com.minshigee.playerchanger.domain.module.Controller;
import com.minshigee.playerchanger.logic.view.domain.ShowType;
import com.mojang.datafixers.util.Pair;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.Arrays;

@IsController
public class ViewController extends Controller<ViewRepository> {
    public static ViewController singleton = null;
    public ViewController(ViewRepository repository) {
        super(repository);
        if(singleton != null)
            return;
        singleton = this;
    }


    public void addViewTask(int laterTime, ShowType type, Player[] players, String msg){
        repository.addViewTask(laterTime,type,players,msg);
        return;
    }

    public void playSoundAllParticipants(Sound sound){repository.playSoundAllParticipants(sound);}

    public void stopViewScoreboard(){repository.stopScoreboardData();}
    public void clearViewScoreboard(int num){repository.clearScoreboardData(num);}
    public void addViewScoreboardString(int num, Pair<String,String> data) {repository.addScoreboardData(num,data);}
    public void updateViewScoreboardString(int num, int code, Pair<String,String> data){repository.updateScoreboardData(num,code,data);}

}
