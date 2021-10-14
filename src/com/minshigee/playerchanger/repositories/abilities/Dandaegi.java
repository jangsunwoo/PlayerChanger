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
import org.w3c.dom.Attr;

public class Dandaegi extends Ability {

    public Dandaegi(){
        name = "단데기";
        description = "체력이 낮을수록 더욱 단단해진다. 하지만 그만큼 공격 속도도 느려진다.";
    }

    @Override
    protected void initPlayerAttribute(Player player) {
        super.initPlayerAttribute(player);
        Util.makeGettingAbilityParticle(player, Particle.SLIME);
    }

    //TODO 단데기 이벤트 핸들링
    public void mapDamagedAndAttackEvent(EntityDamageByEntityEvent event){
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

    // TODO 단데기 능력 코드 업데이트.
    public void damagedEventDandaegi(Player player, EntityDamageByEntityEvent event) {
        double maxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
        double healthRatio = Math.max(player.getHealth() / maxHealth, 0.05D);
        event.setDamage(event.getDamage() * healthRatio);
        playDamagedSound(player);
    }

    public void attackEventDandaegi(Player player, EntityDamageByEntityEvent event){
        double maxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
        double healthRatio = player.getHealth() / maxHealth;
        healthRatio = Math.max(player.getHealth() / maxHealth, 0.1D);
        player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(healthRatio);
    }

    public void playDamagedSound(Player player){
        player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_BREAK, 1, 1);
    }
}
