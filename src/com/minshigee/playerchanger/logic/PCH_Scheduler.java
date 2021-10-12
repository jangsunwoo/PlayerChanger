package com.minshigee.playerchanger.logic;

import com.minshigee.playerchanger.PlayerChanger;
import com.minshigee.playerchanger.data.MetaData;
import com.minshigee.playerchanger.data.PCH_Status;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.network.chat.ChatMessage;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.entity.Player;

public class PCH_Scheduler {

    private static final PCH_Repository repository = new PCH_Repository();
    private static final String readyMessage = ChatColor.AQUA + "[PlayerChanger]: 게임이 곧 시작됩니다. /pch ready 로 등록하세요.";

    public static void startGame(Player player){
        if(MetaData.gameStatus == PCH_Status.DISABLED){
            changeStatusToSetting(player.getServer());
        }
        else if(MetaData.gameStatus == PCH_Status.SETTING){
            changeStatusToStarting(player.getServer());
        }
        else{
            player.sendMessage(ChatColor.RED + "[PlayerChanger]: 이미 게임이 시작되었습니다.");
        }
    }

    private static void changeStatusToSetting(Server server){
        MetaData.gameStatus = PCH_Status.SETTING;
        makeSettingInfoSchedule(server);
    }

    private static void changeStatusToStarting(Server server){
        MetaData.gameStatus = PCH_Status.STARTING;
        server.getOnlinePlayers().forEach(p ->
                p.sendMessage(ChatColor.GREEN + "[PlayerChanger]: 게임이 시작되었습니다.\n" +
                        "Author: minshigee\n" +
                        ChatColor.GRAY + "desc: 시간마다 플레이어의 정보가 바뀝니다. 인벤토리, 체력, 좌표에 주의하세요.")
        );

    }

    public static void stopGame(){
        MetaData.gameStatus = PCH_Status.DISABLED;
        MetaData.participants.clear();
        MetaData.leaveParticipants.clear();
    }

    private static void makeGameScheduler(Server server){

    }

    private static void makeSettingInfoSchedule(Server server){
        server.getScheduler().runTaskTimer(
                PlayerChanger.playerChanger,
                settingTask -> {
                    if(MetaData.gameStatus != PCH_Status.SETTING){
                        settingTask.cancel();
                        return;
                    }
                    server.getOnlinePlayers().forEach(p ->
                            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(readyMessage))
                    );
                },
                0,
                20L * 10L
        );
    }

}
