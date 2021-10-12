package com.minshigee.playerchanger.events;

import com.minshigee.playerchanger.data.MetaData;
import org.bukkit.entity.Player;

public class EventRepository {

    public void resetLeaveParticipant(Player player){
        if(MetaData.isExistLeaveParticipant(player)){
            MetaData.delLeaveParticipant(player);
            player.setHealth(0.0D);
        }
    }

    public void resetParticipant(Player player){
        if(MetaData.isExistParticipant(player)){
            MetaData.delParticipant(player);
            MetaData.addLeaveParticipant(player);
        }
    }
}
