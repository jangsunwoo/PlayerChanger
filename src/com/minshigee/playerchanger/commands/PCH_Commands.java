package com.minshigee.playerchanger.commands;

import com.minshigee.playerchanger.data.MetaData;
import com.minshigee.playerchanger.data.PCH_Status;
import com.minshigee.playerchanger.logic.PCH_Scheduler;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public class PCH_Commands implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(!(commandSender instanceof Player)){
            commandSender.getServer().getConsoleSender().sendMessage(ChatColor.RED + "[PlayerChanger]: 이 명령어는 플레이어만 사용이 가능합니다.");
            return true;
        }
        Player player = (Player) commandSender;

        if(command.getName().equalsIgnoreCase("pch")){
            if(strings.length < 1){
                player.sendMessage("args가 필요합니다.");
                return false;
            }
            if(strings[0].equalsIgnoreCase("start")){
                if(!player.isOp()){
                    return false;
                }
                PCH_Scheduler.startGame(player);
            }
            else if(strings[0].equalsIgnoreCase("stop")){
                if(!player.isOp()){
                    return false;
                }
                PCH_Scheduler.stopGame();
            }
            else if(strings[0].equalsIgnoreCase("ready")){
                if(MetaData.gameStatus != PCH_Status.SETTING){
                    player.sendMessage(ChatColor.GREEN + "[PlayerChanger]: 현재는 등록이 불가능합니다.");
                    return false;
                }
                int code = MetaData.addParticipant(player);
                switch (code){
                    case 0:
                        player.sendMessage(ChatColor.GREEN + "[PlayerChanger]: 게임에 등록되었습니다.");
                        break;
                    case 1:
                        player.sendMessage(ChatColor.RED + "[PlayerChanger]: 실패. 최대 인원입니다.");
                        break;
                    case 2:
                        player.sendMessage(ChatColor.GOLD + "[PlayerChanger]: 이미 등록된 상태입니다.");
                }
            }
            else if(strings[0].equalsIgnoreCase("leave")){
                if(MetaData.gameStatus != PCH_Status.SETTING){
                    player.sendMessage(ChatColor.GREEN + "[PlayerChanger]: 현재는 퇴장이 불가능합니다.");
                    return false;
                }
                MetaData.delParticipant(player);
            }
            else if(strings[0].equalsIgnoreCase("loc")){
                for(int idx = 0; idx < MetaData.startLocations.size(); idx++){
                    Location loc = MetaData.startLocations.get(idx);
                    String msg = String.format("start loc: idx: %d, loc: {%.1f,%.1f,%.1f}",idx,loc.getX(),loc.getY(),loc.getZ());
                    player.sendMessage(msg);
                }
            }
            else if(strings[0].equalsIgnoreCase("addloc")){
                MetaData.addStartLocation(player.getLocation());
            }
            else if(strings[0].equalsIgnoreCase("delloc")){
                if(strings.length < 2){
                    player.sendMessage("번호를 입력하시오.");
                }
                try {
                    int idx = Integer.getInteger(strings[1]);
                    MetaData.delStartLocation(idx);
                }
                catch (Exception exception){
                    player.sendMessage("틀린 명령어 수식입니다.");
                }
            }
        }
        return false;
    }
}
