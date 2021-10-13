package com.minshigee.playerchanger.views;

import com.minshigee.playerchanger.controllers.GameController;
import com.minshigee.playerchanger.domain.PlayInfo;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

public class GameView {
    public static ScoreboardManager playStatusBoardManager;

    public void createPlayStatusBoard(){
        Scoreboard board = playStatusBoardManager.getNewScoreboard();

        Objective obj = board.registerNewObjective("게임 상태", "", ChatColor.GOLD + "Play Status");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        obj.getScore(ChatColor.GRAY + "=-=-=-=-=-=-=-=-=").setScore(6);
        obj.getScore(ChatColor.DARK_AQUA + "플레이 타임 : " + ChatColor.GOLD + GameController.curTime/60 + "분").setScore(5);
        obj.getScore(ChatColor.GRAY + "=-=-=-=-=-=-=-===").setScore(4);
        obj.getScore(ChatColor.DARK_AQUA + "인벤토리 확률 : " + ChatColor.GOLD + GameController.invPer + "%").setScore(3);
        obj.getScore(ChatColor.GRAY + "=-=-=-=-=-=-===-=").setScore(2);
        obj.getScore(ChatColor.DARK_AQUA + "이벤트 타이머 : " + ChatColor.GOLD + (PlayInfo.timeCycle - GameController.curTime % PlayInfo.timeCycle)).setScore(1);
        obj.getScore(ChatColor.DARK_AQUA + "남은 유저 수: " + ChatColor.GOLD + PlayInfo.participants.size()).setScore(0);
        PlayInfo.participants.forEach(p -> p.setScoreboard(board));
        PlayInfo.leaveParticipants.forEach(p -> p.setScoreboard(board));
    }

    public void makeSoundInvChangedPlayer(Player player){
        player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_USE, 2f, 1f);
    }
    public void makeCountSound(boolean b, Player player){
        if(b) {
            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
        }
    }
}
