package com.minshigee.playerchanger.repositories;

import com.minshigee.playerchanger.PlayerChanger;
import com.minshigee.playerchanger.controllers.GameController;
import com.minshigee.playerchanger.domain.AbilityInfo;
import com.minshigee.playerchanger.domain.PCH_Status;
import com.minshigee.playerchanger.domain.PlayInfo;
import com.minshigee.playerchanger.domain.abilities.interfaces.AbilityCode;
import com.minshigee.playerchanger.util.Util;
import com.minshigee.playerchanger.views.GameView;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class GameRepository {

    private final TaskRepository taskRepository;

    public GameRepository(TaskRepository _taskRepository){
        taskRepository = _taskRepository;
    }

    public int minInvPer = 50;
    private static final GameView view = new GameView();
    private final String[] readyMessage = {
            ChatColor.AQUA + "[PlayerChanger]: 게임이 곧 시작됩니다. /pch ready 로 등록하세요.",
            ChatColor.GOLD + "[PlayerChanger]: 당신은 참가자입니다. 취소하려면 /pch leave 를 사용하세요.",
            ChatColor.AQUA + "[PlayerChanger]: 당신은 관전자입니다. /pch ready 로 등록하세요."
    };

    public void executeSecondToParticipants(){
        boolean closeInv = GameController.curTime%PlayInfo.timeCycle > PlayInfo.timeCycle - 5;
        PlayInfo.participants.forEach(p -> {
                Util.makeCountSound(closeInv, p);
                closeInventory(closeInv,p);
                updateActionBar(p);
                taskRepository.executeTaskParticipantsSecond(p);
            }
        );
    }

    public void executeSecondToLeaveParticipants(){
        PlayInfo.leaveParticipants.forEach(p -> taskRepository.executeTaskSpectatorsSecond(p));
    }

    public void initGameStarting(Server server){
        PlayInfo.participants.forEach(player -> {
            player.setGameMode(GameMode.SURVIVAL);
            player.setHealth(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getDefaultValue());
            player.setFoodLevel(20);
        });

        //TODO 능력부여
        giveAbility();

        server.getOnlinePlayers().forEach(p ->
                p.sendMessage(ChatColor.GREEN + "Author: " + ChatColor.AQUA + "minshigee\n" +
                        ChatColor.GRAY + "시간마다 플레이어의 정보가 바뀝니다. 인벤토리, 체력, 좌표에 주의하세요.")
        );
    }

    public void resetGame(Server server){
        GameController.curTime = 0;
        GameController.invPer = PlayInfo.inventoryChangePercent;
        PlayInfo.participants.forEach(PlayInfo::resetPlayerAttribute);
        PlayInfo.leaveParticipants.forEach(PlayInfo::resetPlayerAttribute);
        PlayInfo.resetPlayInfo(); //TODO 무조건 뒤에 배치 (참가자 명단이 사라집니다.)
        AbilityInfo.resetAbilityInfo();
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

                    taskRepository.executeEventSchedulesSecond();

                    if(GameController.curTime % 10 == 0){
                        taskRepository.executeEventSchedules10Second();
                    }
                    if(GameController.curTime % 30 == 0){
                        taskRepository.executeEventSchedules30Second();
                    }
                    if(GameController.curTime % 60 == 0){
                        taskRepository.executeEventSchedules60Second();
                    }

                    if(GameController.curTime % PlayInfo.timeCycle == 0){
                        changeInventory(PlayerChanger.playerChanger.getServer());
                    }
                },
                0,
                20L
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
                    server.getOnlinePlayers().forEach(p -> Util.makeActionBarMessage(p,readyMessage[0]));
                    PlayInfo.participants.forEach(p -> Util.makeActionBarMessage(p,readyMessage[1]));
                    PlayInfo.leaveParticipants.forEach(p -> Util.makeActionBarMessage(p,readyMessage[2]));
                    view.createPlayStatusBoard();
                },
                0,
                20L
        );
    }

    public void giveAbility(){
        List<Player> players = new ArrayList<>(PlayInfo.participants);
        Collections.shuffle(players);
        List<AbilityCode> tmpAbilityCode = Arrays.stream(AbilityCode.values()).collect(Collectors.toList());
        Collections.shuffle(tmpAbilityCode);
        AtomicInteger idx = new AtomicInteger();
        int cycle = tmpAbilityCode.size();
        players.forEach(player -> {
            AbilityInfo.giveParticipantAbility(player, tmpAbilityCode.get(idx.getAndIncrement() % cycle));
        });
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

        List<Player> players = new ArrayList<>(PlayInfo.participants);
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

            Util.makeSoundInvChangedPlayer(to);
            Util.makeSoundInvChangedPlayer(from);
        }
    }

    private void closeInventory(boolean b, Player p){
        if(b){
            p.closeInventory();
        }
    }

    private void updateActionBar(Player p){
        Util.makeActionBarMessage(p,
                ChatColor.AQUA + "Time - " +
                        ChatColor.GOLD + (PlayInfo.timeCycle - GameController.curTime % PlayInfo.timeCycle) +
                        ChatColor.AQUA + " Percentage - " + ChatColor.GOLD + GameController.invPer + "%"
        );
    }

}
