package com.minshigee.playerchanger.controllers;

import com.minshigee.playerchanger.domain.AbilityInfo;
import com.minshigee.playerchanger.repositories.abilities.Dandaegi;
import com.minshigee.playerchanger.domain.abilities.interfaces.AbilityCode;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class AbilitiesController implements Listener {

    @EventHandler
    public void getDamagedAndAttackEntitiy(EntityDamageByEntityEvent event){
        ((Dandaegi)AbilityInfo.getAbilityObject(AbilityCode.Dandaegi))
                .mapDamagedAndAttackEvent(event);

    }

}
