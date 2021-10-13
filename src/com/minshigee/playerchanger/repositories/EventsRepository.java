package com.minshigee.playerchanger.repositories;

import com.minshigee.playerchanger.PlayerChanger;
import com.minshigee.playerchanger.domain.PlayInfo;
import com.minshigee.playerchanger.domain.PCH_Status;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;

public class EventsRepository {

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
}
