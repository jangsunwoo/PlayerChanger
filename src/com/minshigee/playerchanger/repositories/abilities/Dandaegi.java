package com.minshigee.playerchanger.repositories.abilities;

import com.minshigee.playerchanger.domain.AbilityInfo;
import com.minshigee.playerchanger.domain.abilities.Ability;
import com.minshigee.playerchanger.domain.abilities.interfaces.AbilityCode;
import com.minshigee.playerchanger.util.Util;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.w3c.dom.Attr;

import java.util.Objects;

public class Dandaegi extends Ability {

    public Dandaegi(){
        name = "단데기";
        description = "아주 단단한 곤충이군요. 죽음을 느낄수록 더 무겁고 단단한 벽으로 몸을 보호합니다.";
        hurtSound = Sound.BLOCK_ANVIL_BREAK;
        deathSound = Sound.ENTITY_SILVERFISH_DEATH;
    }

    @Override
    protected void initPlayerAttribute(Player player) {
        super.initPlayerAttribute(player);
        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(10);
        Util.makeGettingAbilityParticle(player, Particle.SLIME);
    }

    //TODO 단데기 이벤트 핸들링
    public void mapDamagedEvent(Player player, EntityDamageEvent event){
        if(AbilityInfo.isExistParticipantInAbilities(player, AbilityCode.Dandaegi)) {
            ((Dandaegi)AbilityInfo.getAbilityObject(AbilityCode.Dandaegi))
                    .damagedEventDandaegi(player, event);
        }
    }
    public void mapAttackEvent(Player player, EntityDamageByEntityEvent event){
        if(AbilityInfo.isExistParticipantInAbilities(player, AbilityCode.Dandaegi)) {
            ((Dandaegi)AbilityInfo.getAbilityObject(AbilityCode.Dandaegi))
                    .attackEventDandaegi(player, event);
        }
    }

    // TODO 단데기 능력 코드 업데이트.
    public void damagedEventDandaegi(Player player, EntityDamageEvent event) {
        double maxHealth = Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getBaseValue();
        double healthRatio = Math.max(player.getHealth() / maxHealth, 0.07D);
        event.setDamage(event.getDamage() * healthRatio);
    }

    public void attackEventDandaegi(Player player, EntityDamageByEntityEvent event){
        double maxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
        double healthRatio = player.getHealth() / maxHealth;
        healthRatio = Math.max(player.getHealth() / maxHealth, 0.5D);
        player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(healthRatio);
    }
}
