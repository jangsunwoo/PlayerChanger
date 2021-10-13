package com.minshigee.playerchanger.repositories;

import com.minshigee.playerchanger.PlayerChanger;
import com.minshigee.playerchanger.controllers.GameController;
import com.minshigee.playerchanger.domain.PCH_Status;
import com.minshigee.playerchanger.domain.PlayInfo;
import com.minshigee.playerchanger.views.GameView;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class GameRepository {

    public int minInvPer = 50;
    private static final GameView view = new GameView();
    private final String[] readyMessage = {
            ChatColor.AQUA + "[PlayerChanger]: 게임이 곧 시작됩니다. /pch ready 로 등록하세요.",
            ChatColor.GOLD + "[PlayerChanger]: 당신은 참가자입니다. 취소하려면 /pch leave 를 사용하세요.",
            ChatColor.AQUA + "[PlayerChanger]: 당신은 관전자입니다. /pch ready 로 등록하세요."
    };

    public void initGameStarting(Server server){
        PlayInfo.participants.forEach(player -> {
            player.setGameMode(GameMode.SURVIVAL);
            player.setHealth(player.getMaxHealth());
            player.setFoodLevel(20);
        });

        server.getOnlinePlayers().forEach(p ->
                p.sendMessage(ChatColor.GREEN + "[PlayerChanger]: 게임이 시작되었습니다.\n" +
                        "Author: " + ChatColor.AQUA + "minshigee\n" +
                        ChatColor.GRAY + "desc: 시간마다 플레이어의 정보가 바뀝니다. 인벤토리, 체력, 좌표에 주의하세요.")
        );
    }

    public void resetGameStopping(Server server){
        PlayInfo.gameStatus = PCH_Status.DISABLED;
        GameController.curTime = 0;
        GameController.invPer = PlayInfo.inventoryChangePercent;
        PlayInfo.leaveParticipants.stream()
                .filter(player ->
                        player.getGameMode() == GameMode.SPECTATOR
                )
                .forEach(player -> {
                    player.setGameMode(GameMode.SURVIVAL);
                });
        PlayInfo.participants.clear();
        PlayInfo.leaveParticipants.clear();
    }

    public void executeSecondToParticipants(){
        boolean closeInv = GameController.curTime%PlayInfo.timeCycle > PlayInfo.timeCycle - 5;
        PlayInfo.participants.forEach(p -> {
                    //TODO 초당 참가자에게 코드 실행하기.
                view.makeCountSound(closeInv, p);
                closeInventory(closeInv,p);
            }
        );
    }

    public void executeSecondToLeaveParticipants(){
        PlayInfo.leaveParticipants.forEach(p -> {
                    //TODO 초당 관람자에게 코드 실행하기.
                }
        );
    }

    public void makeGameScheduler(Server server){
        server.getScheduler().runTaskTimer(
                PlayerChanger.playerChanger,
                gameTask -> {
                    if(PlayInfo.gameStatus != PCH_Status.STARTING){
                        gameTask.cancel();
                        return;
                    }

                    GameController.curTime += 1;
                    executeSecondToParticipants();
                    executeSecondToLeaveParticipants();
                    view.createPlayStatusBoard();

                    if(GameController.curTime % PlayInfo.timeCycle != 0){
                        return;
                    }
                    changeInventory(PlayerChanger.playerChanger.getServer());
                },
                0,
                20L * 1L
        );
    }

    public void makeSettingInfoSchedule(Server server){
        server.getScheduler().runTaskTimer(
                PlayerChanger.playerChanger,
                settingTask -> {
                    if(PlayInfo.gameStatus != PCH_Status.SETTING){
                        settingTask.cancel();
                        return;
                    }
                    server.getOnlinePlayers().forEach(p ->
                            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(readyMessage[0]))
                    );
                    PlayInfo.participants.forEach(p ->
                            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(readyMessage[1]))
                    );
                    PlayInfo.leaveParticipants.forEach(p ->
                            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(readyMessage[2]))
                    );
                    view.createPlayStatusBoard();
                },
                0,
                20L * 1L
        );
    }

    public void changeInventory(Server server) {
        Random random = new Random();
        int val = random.nextInt(100);
        boolean changeCode = GameController.invPer < val;
        GameController.invPer = minInvPer + random.nextInt(100 - minInvPer);

        if (changeCode){
            if(minInvPer > 70) {
                return;
            }
            minInvPer += (100 - minInvPer) / 2;
            return;
        }
        minInvPer /= 2;

        List<Player> players = PlayInfo.participants.stream()
                .collect(Collectors.toList());
        Collections.shuffle(players);

        for(int i = 0; i < players.size() / 2; i++){
            Player from = players.get(i);
            Player to = players.get(players.size() - i - 1);
            Inventory tmpInv = Bukkit.createInventory(null, InventoryType.PLAYER, "tmp Inventory");
            ItemStack[] tmpArmor = to.getInventory().getArmorContents();
            tmpInv.setContents(to.getInventory().getContents());

            to.getInventory().setContents(from.getInventory().getContents());
            to.getInventory().setArmorContents(from.getInventory().getArmorContents());

            from.getInventory().setContents(tmpInv.getContents());
            from.getInventory().setArmorContents(tmpArmor);

            to.updateInventory();
            from.updateInventory();

            view.makeSoundInvChangedPlayer(to);
            view.makeSoundInvChangedPlayer(from);
        }
    }

    private void closeInventory(boolean b, Player p){
        if(b){
            p.closeInventory();
        }
    }

}
