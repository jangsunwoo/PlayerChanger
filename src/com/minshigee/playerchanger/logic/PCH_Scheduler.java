package com.minshigee.playerchanger.logic;

import com.minshigee.playerchanger.PlayerChanger;
import com.minshigee.playerchanger.data.MetaData;
import com.minshigee.playerchanger.data.PCH_Status;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.network.chat.ChatMessage;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

public class PCH_Scheduler {

    public static int curTime = 0;
    public static int invPer = 80;
    public static ScoreboardManager playStatusBoardManager;

    private static final PCH_Repository repository = new PCH_Repository();
    private static final String[] readyMessage = {
            ChatColor.AQUA + "[PlayerChanger]: 게임이 곧 시작됩니다. /pch ready 로 등록하세요.",
            ChatColor.GOLD + "[PlayerChanger]: 당신은 참가자입니다. 취소하려면 /pch leave 를 사용하세요.",
            ChatColor.AQUA + "[PlayerChanger]: 당신은 관전자입니다. /pch ready 로 등록하세요."
    };

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

        MetaData.participants.forEach(player -> {
            player.setGameMode(GameMode.SURVIVAL);
            player.setHealth(player.getMaxHealth());
            player.setFoodLevel(20);
        });

        server.getOnlinePlayers().forEach(p ->
                p.sendMessage(ChatColor.GREEN + "[PlayerChanger]: 게임이 시작되었습니다.\n" +
                        "Author: " + ChatColor.AQUA + "minshigee\n" +
                        ChatColor.GRAY + "desc: 시간마다 플레이어의 정보가 바뀝니다. 인벤토리, 체력, 좌표에 주의하세요.")
        );
        makeGameScheduler(server);
    }

    public static void stopGame(){
        MetaData.gameStatus = PCH_Status.DISABLED;
        curTime = 0;
        invPer = MetaData.inventoryChangePercent;
        MetaData.leaveParticipants.stream()
                .filter(player ->
                        player.getGameMode() == GameMode.SPECTATOR
                )
                .forEach(player -> {
                        player.setGameMode(GameMode.SURVIVAL);
                });
        MetaData.leaveParticipants.stream()
                .forEach(player -> {
                    player.setScoreboard(playStatusBoardManager.getMainScoreboard());
                });
        MetaData.participants.stream()
                .forEach(player -> {
                    player.setScoreboard(playStatusBoardManager.getMainScoreboard());
                });

        MetaData.participants.clear();
        MetaData.leaveParticipants.clear();
    }

    private static void makeGameScheduler(Server server){
        server.getScheduler().runTaskTimer(
                PlayerChanger.playerChanger,
                gameTask -> {
                    if(MetaData.gameStatus != PCH_Status.STARTING){
                        gameTask.cancel();
                        return;
                    }

                    curTime += 1;
                    executeSecondToParticipants();
                    executeSecondToLeaveParticipants();
                    createPlayStatusBoard();

                    if(curTime % MetaData.timeCycle != 0){
                        return;
                    }
                    repository.changeInventory(PlayerChanger.playerChanger.getServer());
                },
                0,
                20L * 1L
        );
    }

    private static void executeSecondToParticipants(){
        MetaData.participants.forEach(p -> {
                    //TODO 초당 참가자에게 코드 실행하기.
                }
        );
    }
    private static void executeSecondToLeaveParticipants(){
        MetaData.leaveParticipants.forEach(p -> {
                    //TODO 초당 관람자에게 코드 실행하기.
                }
        );
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
                            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(readyMessage[0]))
                    );
                    MetaData.participants.forEach(p ->
                            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(readyMessage[1]))
                    );
                    MetaData.leaveParticipants.forEach(p ->
                            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(readyMessage[2]))
                    );
                    createPlayStatusBoard();
                },
                0,
                20L * 1L
        );
    }

    private static void createPlayStatusBoard(){
        Scoreboard board = playStatusBoardManager.getNewScoreboard();

        Objective obj = board.registerNewObjective("게임 상태", "", ChatColor.GOLD + "Play Status");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        obj.getScore(ChatColor.GOLD + "=-=-=-=-=-=-=-=-=").setScore(1);
        obj.getScore(ChatColor.GOLD + "플레이 타임 : " + curTime).setScore(1);
        obj.getScore(ChatColor.AQUA + "=-=-=-=-=-=-=-=-=").setScore(1);
        obj.getScore(ChatColor.AQUA + String.format("인벤토리 확률 : %d",invPer)).setScore(1);
        obj.getScore(ChatColor.GREEN + "=-=-=-=-=-=-=-=-=").setScore(1);
        obj.getScore(ChatColor.GREEN + "이벤트 타이머 : " + (MetaData.timeCycle - curTime % MetaData.timeCycle)).setScore(1);
        obj.getScore(ChatColor.GREEN + "남은 유저 수: " + MetaData.participants.size()).setScore(1);
        MetaData.participants.forEach(p -> p.setScoreboard(board));
        MetaData.leaveParticipants.forEach(p -> p.setScoreboard(board));
    }

}
