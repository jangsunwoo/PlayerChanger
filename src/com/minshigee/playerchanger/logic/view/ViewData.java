package com.minshigee.playerchanger.logic.view;

import com.minshigee.playerchanger.logic.view.domain.ViewTask;
import com.mojang.datafixers.util.Pair;
import org.bukkit.Bukkit;
import org.bukkit.scoreboard.ScoreboardManager;

import java.util.*;

public class ViewData {
    public ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
    private Queue<ViewTask> viewTaskQueue = new PriorityQueue<>();

    public ViewTask getAndPopTask(){return viewTaskQueue.poll();}
    public ViewTask getViewTask(){return viewTaskQueue.peek();}
    public void addViewTask(ViewTask task){viewTaskQueue.add(task);}

    private ArrayList<ArrayList<Pair<String,String>>> scoreboardTemplate = new ArrayList<>();

    private void initScoreboardTemplate(){
        for(int i = 0; i < 10; i++)
            scoreboardTemplate.add(new ArrayList<>());
    }

    public ViewData() {
        initScoreboardTemplate();
    }

    public void registerStringScoreboard(int num, Pair<String,String> data){
        scoreboardTemplate.get(num).add(data);
    }
    public ArrayList<Pair<String,String>> getNumOfScoreboard(int num){
        return scoreboardTemplate.get(num);
    }
    public void updateScoreboardData(int num, int code, Pair<String,String> data){scoreboardTemplate.get(num).set(code,data);}
    public void clearNumOfScoreboard(int num){
        scoreboardTemplate.get(num).clear();
    }
}
