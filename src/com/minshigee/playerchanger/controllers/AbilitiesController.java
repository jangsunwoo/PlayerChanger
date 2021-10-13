package com.minshigee.playerchanger.controllers;

import com.minshigee.playerchanger.domain.AbilityInfo;
import com.minshigee.playerchanger.domain.abilities.Dandaegi;
import com.minshigee.playerchanger.domain.abilities.interfaces.AbilityCode;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class AbilitiesController implements Listener {

    @EventHandler
    public void getDandaegiEvent(EntityDamageByEntityEvent event){
        if(event.getEntity().getType().equals(EntityType.PLAYER)){
            Player tmpPlayer = (Player)event.getEntity();
            if(AbilityInfo.isExistParticipantInAbilities(tmpPlayer, AbilityCode.Dandaegi)) {
                ((Dandaegi)AbilityInfo.getAbilityObject(AbilityCode.Dandaegi))
                        .damagedEventDandaegi(tmpPlayer, event);
            }
        }
        if(event.getDamager().getType().equals(EntityType.PLAYER)){
            Player tmpPlayer = (Player)event.getDamager();
            if(AbilityInfo.isExistParticipantInAbilities(tmpPlayer, AbilityCode.Dandaegi)) {
                ((Dandaegi)AbilityInfo.getAbilityObject(AbilityCode.Dandaegi))
                        .attackEventDandaegi(tmpPlayer, event);
            }
        }
    }

}
