package com.minshigee.playerchanger.logic.view.domain;

import com.minshigee.playerchanger.util.MessageUtil;
import net.md_5.bungee.api.ChatMessageType;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class ViewTask implements Comparable<ViewTask>{
    private Integer viewTime;
    private String viewMessage;
    private ShowType showType;
    private Player[] players;

    public ViewTask(Integer viewTime, ShowType showType, Player[] players, String viewMessage) {
        this.viewTime = viewTime;
        this.viewMessage = viewMessage;
        this.showType = showType;
        this.players = players;
    }

    @Override
    public int compareTo(ViewTask t) {
        return this.viewTime;
    }

    public int getStartTime(){
        return viewTime;
    }

    public void executeTask(){
        if(players == null)
            return;
        if(showType == ShowType.Chat){
            Arrays.stream(players).forEach(p -> MessageUtil.printMsgToPlayer(ChatMessageType.CHAT, p, viewMessage));
        }
        else if(showType == ShowType.ActionBar){
            Arrays.stream(players).forEach(p -> MessageUtil.printMsgToPlayer(ChatMessageType.ACTION_BAR, p, viewMessage));
        }
        else if(showType == ShowType.Title){
            String s = viewMessage.split("\n")[0];
            String s2 = viewMessage.split("\n")[1];
            Arrays.stream(players).forEach(p -> p.sendTitle(s,s2,20,60,20));
        }
        return;
    }
}