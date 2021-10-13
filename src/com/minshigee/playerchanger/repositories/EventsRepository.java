package com.minshigee.playerchanger.repositories;

import com.minshigee.playerchanger.PlayerChanger;
import com.minshigee.playerchanger.domain.PlayInfo;
import com.minshigee.playerchanger.domain.PCH_Status;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;

import java.util.Arrays;

public class EventsRepository {

    private static final InventoryType[] moveItemInventory = {
            InventoryType.PLAYER,
            InventoryType.CRAFTING
    };
    private static final InventoryAction[] moveItemOptions = {
            InventoryAction.PLACE_ALL,
            InventoryAction.PLACE_SOME,
            InventoryAction.PLACE_ONE
    };

    public void resetParticipant(Player player){
        if(PlayInfo.isExistParticipant(player)){
            PlayInfo.delParticipant(player);
            PlayInfo.addLeaveParticipant(player);
        }
    }

    public void quitParticipant(Player player){
        if(PlayInfo.gameStatus == PCH_Status.STARTING) {
            player.setGameMode(GameMode.SPECTATOR);
        }
        resetParticipant(player);
    }

    public void removeBlocksCheduling(BlockPlaceEvent event){
        Bukkit.getScheduler().runTaskLater(
                PlayerChanger.playerChanger,
                blockTask -> {
                    event.getBlockPlaced().setType(Material.AIR);
                },
                20 * PlayInfo.blockRemoveCycle
        );
    }

    public void checkInvMoveBug(InventoryClickEvent event){
        if(PlayInfo.gameStatus != PCH_Status.STARTING)
            return;;
        Player player = (Player) event.getWhoClicked();
        if(!PlayInfo.isExistParticipant(player))
            return;
        try {
            if (event.getClick().isShiftClick()) {
                if (event.getClickedInventory().getType().equals(InventoryType.PLAYER)) {
                    event.setCancelled(true);
                }
            } else if (Arrays.stream(moveItemOptions).anyMatch(event.getAction()::equals)) {
                if (!Arrays.stream(moveItemInventory).anyMatch(event.getClickedInventory().getType()::equals)) {
                    event.setCancelled(true);
                }
            }
        }
        catch (Exception e){

        }
    }

    public void checkInvDragBug(InventoryDragEvent event){
        if(PlayInfo.gameStatus != PCH_Status.STARTING)
            return;
        Player player = (Player) event.getWhoClicked();
        if(!PlayInfo.isExistParticipant(player))
            return;
        if(Arrays.stream(moveItemInventory).anyMatch(event.getInventory().getType()::equals)){
            return;
        }
        event.setCancelled(true);
    }
}
