package com.minshigee.playerchanger.logic.view;

import com.minshigee.playerchanger.domain.annotation.IsController;
import com.minshigee.playerchanger.domain.module.Controller;
import com.minshigee.playerchanger.logic.view.domain.ShowType;
import com.mojang.datafixers.util.Pair;
import org.bukkit.Bukkit;
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

    public void addViewScoreboardString(int num, Pair<String,String> data){

    }


}
