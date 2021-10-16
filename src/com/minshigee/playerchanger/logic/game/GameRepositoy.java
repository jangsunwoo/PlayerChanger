package com.minshigee.playerchanger.logic.game;

import com.minshigee.playerchanger.PlayerChanger;
import com.minshigee.playerchanger.domain.module.Repository;
import com.minshigee.playerchanger.util.ConsoleLogs;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class GameRepositoy extends Repository<GameData> {
    public GameRepositoy(GameData localDB) {
        super(localDB);
    }

    public void excuteGameStart(){
        validateBeforeGameStart();
        GameData.makeNextGameStatus();
    }
    private void validateBeforeGameStart(){
        localDB.clearSettingData();

    }

    /*
    Setter 관련 Repo
     */

    public void makePlayerSetterOrNot(Player player){
        if(!localDB.checkPlayerIsSetter(player)){
            localDB.addSetter(player);
            ConsoleLogs.printLogToPlayer(player, ChatColor.AQUA + "세팅모드가 되었습니다.");
        }
        else{
            localDB.removeSetter(player);
            ConsoleLogs.printLogToPlayer(player, ChatColor.AQUA + "세팅모드가 해제되었습니다.");
        }
    }
    public void updateGameWorldPos(PlayerInteractEvent event){
        if(!localDB.checkPlayerIsSetter(event.getPlayer()))
            return;
        if(!event.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.BLAZE_ROD))
            return;
        Location location = event.getPlayer().getLocation();
        if(event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK){
            PlayerChanger.config.set("GameWorld.Size_Pos1", location);
            ConsoleLogs.printLogToPlayer(event.getPlayer(), ChatColor.AQUA + " 월드 Pos1이 정해졌습니다. {%d / %d / %d}".formatted(location.getBlockX(), location.getBlockY(), location.getBlockZ()));
            return;
        }
        PlayerChanger.config.set("GameWorld.Size_Pos2", event.getPlayer().getLocation());
        ConsoleLogs.printLogToPlayer(event.getPlayer(), ChatColor.AQUA + " 월드 Pos2이 정해졌습니다. {%d / %d / %d}".formatted(location.getBlockX(), location.getBlockY(), location.getBlockZ()));
    }
    public void updateGameCoreBolck(PlayerInteractEvent event){
        if(!localDB.checkPlayerIsSetter(event.getPlayer()))
            return;
        if(!event.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.ARROW))
            return;
        if(event.getAction().equals(Action.LEFT_CLICK_BLOCK)){
            PlayerChanger.config.set("SpawnBlock", event.getClickedBlock().getBlockData().getMaterial().name());
            ConsoleLogs.printLogToPlayer(event.getPlayer(), ChatColor.AQUA + " 스폰 블럭이 %s로 지정되었습니다.".formatted(event.getClickedBlock().getType().name()));
        }
        else if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
            PlayerChanger.config.set("MissionAssignBlock", event.getClickedBlock().getBlockData().getMaterial().name());
            ConsoleLogs.printLogToPlayer(event.getPlayer(), ChatColor.AQUA + " 미션 완료 블럭이 %s로 지정되었습니다.".formatted(event.getClickedBlock().getType().name()));
        }
    }

}
