package com.minshigee.playerchanger.logic.game;

import com.minshigee.playerchanger.logic.view.ViewRepository;
import com.minshigee.playerchanger.util.MessageUtil;
import com.minshigee.playerchanger.domain.GameState;
import com.minshigee.playerchanger.domain.Role;
import com.minshigee.playerchanger.domain.Participant;
import com.minshigee.playerchanger.domain.module.Data;
import com.mojang.datafixers.util.Pair;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.BlockVector;

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
    public static void offGameState(){
        setGameStatus(GameState.Disable);
    }
    public static void makeNextGameStatus(){
        switch (gameState){
            case Disable -> setGameStatus(GameState.Waitting);
            case Waitting, Freezing -> setGameStatus(GameState.Enable);
            case Enable -> setGameStatus(GameState.Freezing);
        }
        ViewRepository.curTime = 0;
    }

    /*
        게임 참가자들을 관리하는 HashSet과 관련 메소드의 집합입니다.
    */
    private static HashSet<Participant> participants = new HashSet<>();
    public static Set<BlockVector> spawnBlockVectors = Collections.synchronizedSet(new HashSet<>());
    public static Set<BlockVector> chestBlockVectors = Collections.synchronizedSet(new HashSet<>());
    public static Set<BlockVector> craftingBlockVectors = Collections.synchronizedSet(new HashSet<>());
    public static Set<BlockVector> furnaceBlockVectors = Collections.synchronizedSet(new HashSet<>());

    public static HashSet<Participant> getParticipants() {return participants;}
    public static Set<Participant> getParticipantsAlive() {return participants.stream().filter(participant -> participant.getRole().equals(Role.Participant)).filter(participant -> !participant.getIsDeath()).collect(Collectors.toSet());}
    public static Optional<Participant> getParticipantAlive(Player player){return getParticipantsAlive().stream().filter(participant -> participant.getPlayer().equals(player)).findFirst();}
    public static Set<Participant> getParticipantsByRole(Role role){return participants.stream().filter(participant -> participant.getRole().equals(role)).collect(Collectors.toSet());}
    public static void addPlayerToParticipants(Player player, Role role){boolean res = participants.add(new Participant(player, role));
        MessageUtil.printConsoleLog(ChatColor.GREEN + player.getName() + "님의 " + role.name() + "의 등록이 " + res + "로 처리됨.");}
    public static void clearParticipants(){participants.clear();}
    public static void removePlayerFromParticipants(Player player){getParticipantByPlayer(player).ifPresent(participants::remove);}
    public static boolean containPlayerFromParticipants(Player player){return getParticipantByPlayer(player).isPresent();}
    public static Optional<Participant> getParticipantByPlayer(Player player){return participants.stream().filter(participant -> participant.getPlayer().equals(player)).findAny();}
    public static Role getRoleByPlayer(Player player){Optional<Participant> participant = getParticipantByPlayer(player);if(participant.isEmpty())return null;return participant.get().getRole();}
    public static void clearWorldBlockSets(){spawnBlockVectors.clear();chestBlockVectors.clear();craftingBlockVectors.clear();furnaceBlockVectors.clear();}

    /*
    게임 세팅모드를 관리하는 코드입니다.
     */
    private HashSet<Player> setters = new HashSet<>();
    private HashMap<Player, ItemStack[]> tmpSaveInventory = new HashMap<>();
    private final Inventory emptyInventory = Bukkit.createInventory(null, InventoryType.PLAYER);
    private ItemStack settingItem = new ItemStack(Material.BLAZE_ROD, 1);
    public boolean checkPlayerIsSetter(Player player){
        return setters.contains(player);
    }
    public void addSetter(Player player) {
        setters.add(player);
        tmpSaveInventory.put(player, player.getInventory().getContents());
        player.getInventory().setContents(
            emptyInventory.getContents()
        );
        player.getInventory().addItem(settingItem);
    }
    public void removeSetter(Player player){setters.remove(player);player.getInventory().setContents(tmpSaveInventory.get(player));tmpSaveInventory.remove(player);}
    public void clearSettingData(){setters.forEach(this::removeSetter);setters.clear();tmpSaveInventory.clear();}

    public GameData(){
        initGameData();
    }
    private void initGameData(){
         setSettingItems();
    }
    private void setSettingItems(){ItemMeta meta = settingItem.getItemMeta();meta.setDisplayName("게임 월드 범위 설정(좌_pos1/우_pos2)");}

    /*
    기본적으로 생성되는 아이템 리스트와 비율입니다.
     */
    public ArrayList<Pair<Material, Integer>> returnDefaultInBoxItems(){
        return new ArrayList<>(){{
            add(new Pair<>(Material.WOODEN_SWORD, getPerParticipantsAlive(1)));
            add(new Pair<>(Material.STONE_SWORD, getPerParticipantsAlive(0.7)));
            add(new Pair<>(Material.STONE_PICKAXE, getPerParticipantsAlive(0.5)));
            add(new Pair<>(Material.CHAINMAIL_BOOTS, getPerParticipantsAlive(1)));
            add(new Pair<>(Material.CHAINMAIL_CHESTPLATE, getPerParticipantsAlive(1)));
            add(new Pair<>(Material.CHAINMAIL_HELMET, getPerParticipantsAlive(1)));
            add(new Pair<>(Material.CHAINMAIL_LEGGINGS, getPerParticipantsAlive(1)));
            add(new Pair<>(Material.IRON_SWORD, getPerParticipantsAlive(0.5)));
            add(new Pair<>(Material.IRON_BOOTS, getPerParticipantsAlive(0.1)));
            add(new Pair<>(Material.IRON_CHESTPLATE, getPerParticipantsAlive(0.2)));
            add(new Pair<>(Material.IRON_HELMET, getPerParticipantsAlive(0.1)));
            add(new Pair<>(Material.IRON_LEGGINGS, getPerParticipantsAlive(0.1)));
            add(new Pair<>(Material.GLASS, getPerParticipantsAlive(32)));
            add(new Pair<>(Material.COBWEB, getPerParticipantsAlive(5)));
            add(new Pair<>(Material.ENDER_PEARL, getPerParticipantsAlive(4)));
            add(new Pair<>(Material.APPLE, getPerParticipantsAlive(1)));
            add(new Pair<>(Material.GOLDEN_APPLE, getPerParticipantsAlive(0.4)));
            add(new Pair<>(Material.COOKED_PORKCHOP, getPerParticipantsAlive(1)));
            add(new Pair<>(Material.COOKED_BEEF, getPerParticipantsAlive(5)));
        }};
    }
    private Integer getPerParticipantsAlive(double d){
        return Math.max(1,(int)Math.round(getParticipantsAlive().size() * d));
    }


}
