package com.minshigee.playerchanger.events;

import com.minshigee.playerchanger.data.MetaData;
import com.minshigee.playerchanger.data.PCH_Status;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class EventGateway implements Listener {

    private static final EventRepository repository = new EventRepository();

    @EventHandler
    public static void onPlayerJoin(PlayerJoinEvent event){
        if(MetaData.gameStatus == PCH_Status.DISABLED){
            return;
        }
    }

    @EventHandler
    public static void onPlayerLeave(PlayerQuitEvent event){
        if(MetaData.gameStatus == PCH_Status.DISABLED){
            return;
        }
        Player player = event.getPlayer();
        repository.quitParticipant(player);
    }

    @EventHandler
    public static void onPlayerDeath(PlayerDeathEvent event){
        if(MetaData.gameStatus == PCH_Status.DISABLED)
            return;;
        Player player = event.getEntity();
        if(MetaData.isExistParticipant(player)){
            repository.quitParticipant(player);
        }
    }
}
