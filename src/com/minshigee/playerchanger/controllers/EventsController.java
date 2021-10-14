package com.minshigee.playerchanger.controllers;

import com.minshigee.playerchanger.domain.PCH_Status;
import com.minshigee.playerchanger.domain.PlayInfo;
import com.minshigee.playerchanger.repositories.EventsRepository;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class EventsController implements Listener {

    private final EventsRepository repository;

    public EventsController(EventsRepository repo){
        this.repository = repo;
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event){
        if(PlayInfo.gameStatus == PCH_Status.DISABLED){
            return;
        }
        Player player = event.getPlayer();
        repository.quitParticipant(player);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event){
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
    public void onItemMove(InventoryClickEvent event){
        repository.checkInvMoveBug(event);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onItemDrag(InventoryDragEvent event){
        repository.checkInvDragBug(event);
    }

    @EventHandler()
    public void placeBlock(BlockPlaceEvent event){
        if(PlayInfo.gameStatus != PCH_Status.STARTING)
            return;
        Player player = event.getPlayer();
        if(!PlayInfo.isExistParticipant(player))
            return;
        repository.removeBlocksCheduling(event);
    }

}
