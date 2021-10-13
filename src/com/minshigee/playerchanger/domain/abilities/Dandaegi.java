package com.minshigee.playerchanger.domain.abilities;

import com.minshigee.playerchanger.util.Util;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.w3c.dom.Attr;

public class Dandaegi extends Ability{

    public Dandaegi(){
        name = "단데기";
        description = "체력이 낮을수록 더욱 단단해진다. 하지만 그만큼 공격 속도도 느려진다.";
    }

    @Override
    protected void initPlayerAttribute(Player player) {
        super.initPlayerAttribute(player);
        player.sendTitle(getName(),getDescription(),20, 260, 20);
        Util.makeGettingAbilitySound(player);
        Util.makeDanDaegiParticle(player);
    }

    // TODO 단데기 업데이트 하기.
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
