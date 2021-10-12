package com.minshigee.playerchanger.events;

import com.minshigee.playerchanger.data.MetaData;
import com.minshigee.playerchanger.data.PCH_Status;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class EventRepository {

    public void resetParticipant(Player player){
        if(MetaData.isExistParticipant(player)){
            MetaData.delParticipant(player);
            MetaData.addLeaveParticipant(player);
        }
    }

    public void quitParticipant(Player player){
        if(MetaData.gameStatus == PCH_Status.STARTING) {
            player.setGameMode(GameMode.SPECTATOR);
        }
        resetParticipant(player);
    }
}
