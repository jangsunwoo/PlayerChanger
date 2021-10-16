package com.minshigee.playerchanger.logic.game;

import com.minshigee.playerchanger.util.ConsoleLogs;
import com.minshigee.playerchanger.domain.GameState;
import com.minshigee.playerchanger.domain.Role;
import com.minshigee.playerchanger.domain.Participant;
import com.minshigee.playerchanger.domain.module.Data;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
import java.util.stream.Collectors;

public class GameData extends Data {

    /*
    Game의 상태에 대한 정보와 수정 메소드의 집합입니다.
     */
    private static GameState gameState = GameState.Disable;

    public static GameState getGameState(){
        return gameState;
    }
    private static void setGameStatus(GameState status){
        gameState = status;
    }
    public static void offGameStatus(){
        setGameStatus(GameState.Disable);
    }
    public static void makeNextGameStatus(){
        switch (gameState){
            case Disable -> setGameStatus(GameState.Waitting);
            case Waitting, Freezing -> setGameStatus(GameState.Enable);
            case Enable -> setGameStatus(GameState.Freezing);
        }
    }

    /*
        게임 참가자들을 관리하는 HashSet과 관련 메소드의 집합입니다.
    */
    private static final HashSet<Participant> participants = new HashSet<>();

    public static Set<Participant> getParticipantsByRole(Role role){return participants.stream().filter(participant -> participant.getRole().equals(role)).collect(Collectors.toSet());}
    public static void addPlayerToParticipants(Player player, Role role){boolean res = participants.add(new Participant(player, role));ConsoleLogs.printConsoleLog(ChatColor.GREEN + player.getName() + "님의 " + role.name() + "의 등록이 " + res + "로 처리됨.");}
    public static void clearParticipants(){participants.clear();}
    public static void removePlayerFromParticipants(Player player){getParticipantByPlayer(player).ifPresent(participants::remove);}
    public static boolean containPlayerFromParticipants(Player player){return getParticipantByPlayer(player).isPresent();}
    public static Optional<Participant> getParticipantByPlayer(Player player){return participants.stream().filter(participant -> participant.getPlayer().equals(player)).findAny();}
    public static Role getRoleByPlayer(Player player){Optional<Participant> participant = getParticipantByPlayer(player);if(participant.isEmpty())return null;return participant.get().getRole();}

    /*
    게임 세팅모드를 관리하는 코드입니다.
     */
    private HashSet<Player> setters = new HashSet<>();
    private HashMap<Player, ItemStack[]> tmpSaveInventory = new HashMap<>();
    private final Inventory emptyInventory = Bukkit.createInventory(null, InventoryType.PLAYER);
    private ItemStack[] settingItems = {
            new ItemStack(Material.BLAZE_ROD, 1),
            new ItemStack(Material.ARROW, 1),
            new ItemStack(Material.BONE, 1)
    };
    public void addSetter(Player player) {
        setters.add(player);
        tmpSaveInventory.put(player, player.getInventory().getContents());
        player.getInventory().setContents(
            emptyInventory.getContents()
        );
        player.getInventory().addItem();

    }
    public void removeSetter(Player player){setters.remove(player);player.getInventory().setContents(tmpSaveInventory.get(player));tmpSaveInventory.remove(player);}
    public void clearSettingData(){setters.forEach(this::removeSetter);setters.clear();tmpSaveInventory.clear();}

    public GameData(){
        initGameData();
    }
    private void initGameData(){
         setSettingItems();
    }
    private void setSettingItems(){
        Objects.requireNonNull(settingItems[0].getItemMeta()).setDisplayName("시작 좌표(좌_삭제/우_추가)");
        Objects.requireNonNull(settingItems[1].getItemMeta()).setDisplayName("미션(상자) 좌표(좌_삭제/우_추가)");
        Objects.requireNonNull(settingItems[2].getItemMeta()).setDisplayName("미션 완료 좌표(좌_삭제/우_추가)");
    }
}
