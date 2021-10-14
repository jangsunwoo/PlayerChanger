package com.minshigee.playerchanger.controllers;

import com.minshigee.playerchanger.domain.AbilityInfo;
import com.minshigee.playerchanger.domain.PCH_Status;
import com.minshigee.playerchanger.domain.PlayInfo;
import com.minshigee.playerchanger.repositories.abilities.Dandaegi;
import com.minshigee.playerchanger.domain.abilities.interfaces.AbilityCode;
import com.minshigee.playerchanger.repositories.abilities.GoldenPig;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

public class AbilitiesController implements Listener {

    @EventHandler
    public void getDamaged(EntityDamageEvent event){
        if(!isAvailable(event.getEntity()))
            return;
        Player player = (Player) event.getEntity();

        AbilityInfo.playAbilityHurtSound(player); //TODO Hurt 소리 재생

        ((Dandaegi)AbilityInfo.getAbilityObject(AbilityCode.Dandaegi))
                .mapDamagedEvent(player, event); //TODO 단데기 호출
        ((GoldenPig)AbilityInfo.getAbilityObject(AbilityCode.GoldenPig))
                .mapDamagedEvent(player); //TODO 황금돼지 호출
    }

    @EventHandler
    public void getDamagedByEntitiy(EntityDamageByEntityEvent event){
        if(!isAvailable(event.getEntity()))
            return;
        Player player = (Player) event.getEntity();
        ((Dandaegi)AbilityInfo.getAbilityObject(AbilityCode.Dandaegi))
                .mapAttackEvent(player, event); //TODO 단데기 호출
    }

    @EventHandler
    public void getDeath(PlayerDeathEvent event){
        if(!isAvailable(event.getEntity()))
            return;
        Player player = (Player) event.getEntity();

        AbilityInfo.playAbilityDeathSound(player);//TODO Death 소리 재생
    }

    private boolean isAvailable(Entity entity){
        if(!PlayInfo.gameStatus.equals(PCH_Status.STARTING) || !(entity instanceof Player))
            return false;
        Player player = (Player) entity;
        if(!PlayInfo.isExistParticipant(player))
            return false;
        return true;
    }

    private boolean isAvailable(Player player){
        if(!PlayInfo.gameStatus.equals(PCH_Status.STARTING) || !PlayInfo.isExistParticipant(player))
            return false;
        return true;
    }

}
