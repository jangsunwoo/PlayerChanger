package com.minshigee.playerchanger.controllers;

import com.minshigee.playerchanger.PlayerChanger;
import com.minshigee.playerchanger.domain.PCH_Status;
import com.minshigee.playerchanger.domain.PlayInfo;
import com.minshigee.playerchanger.repositories.EventsRepository;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Arrays;

public class EventsController implements Listener {

    private static final EventsRepository repository = new EventsRepository();

    @EventHandler
    public static void onPlayerJoin(PlayerJoinEvent event){
        if(PlayInfo.gameStatus == PCH_Status.DISABLED){
            return;
        }
    }

    @EventHandler
    public static void onPlayerLeave(PlayerQuitEvent event){
        if(PlayInfo.gameStatus == PCH_Status.DISABLED){
            return;
        }
        Player player = event.getPlayer();
        repository.quitParticipant(player);
    }

    @EventHandler
    public static void onPlayerDeath(PlayerDeathEvent event){
        if(PlayInfo.gameStatus == PCH_Status.DISABLED)
            return;;
        Player player = event.getEntity();
        if(PlayInfo.isExistParticipant(player)){
            repository.quitParticipant(player);
        }
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        if(PlayInfo.gameStatus != PCH_Status.STARTING)
            return;;
        Player player = event.getPlayer();
        if(!PlayInfo.isExistParticipant(player))
            return;
        event.getItemDrop().remove();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public static void onItemMove(InventoryClickEvent event){
        repository.checkInvMoveBug(event);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public static void onItemDrag(InventoryDragEvent event){
        repository.checkInvDragBug(event);
    }

    @EventHandler()
    public static void placeBlock(BlockPlaceEvent event){
        if(PlayInfo.gameStatus != PCH_Status.STARTING)
            return;
        Player player = event.getPlayer();
        if(!PlayInfo.isExistParticipant(player))
            return;
        repository.removeBlocksCheduling(event);
    }

}
