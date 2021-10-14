package com.minshigee.playerchanger.controllers;

import com.minshigee.playerchanger.domain.AbilityInfo;
import com.minshigee.playerchanger.repositories.abilities.Dandaegi;
import com.minshigee.playerchanger.domain.abilities.interfaces.AbilityCode;
import com.minshigee.playerchanger.repositories.abilities.GoldenPig;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class AbilitiesController implements Listener {

    @EventHandler
    public void getDamaged(EntityDamageEvent event){
        ((Dandaegi)AbilityInfo.getAbilityObject(AbilityCode.Dandaegi))
                .mapDamagedEvent(event);
        ((GoldenPig)AbilityInfo.getAbilityObject(AbilityCode.GoldenPig))
                .mapDamagedEvent(event);
    }

    @EventHandler
    public void getDamagedByEntitiy(EntityDamageByEntityEvent event){
        ((Dandaegi)AbilityInfo.getAbilityObject(AbilityCode.Dandaegi))
                .mapAttackEvent(event);

    }

}
