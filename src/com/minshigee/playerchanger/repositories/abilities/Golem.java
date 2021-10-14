package com.minshigee.playerchanger.repositories.abilities;

import com.minshigee.playerchanger.domain.AbilityInfo;
import com.minshigee.playerchanger.domain.abilities.Ability;
import com.minshigee.playerchanger.domain.abilities.interfaces.AbilityCode;
import com.minshigee.playerchanger.util.Util;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.w3c.dom.Attr;

import java.util.ArrayList;
import java.util.Random;

public class Golem extends Ability {

    public Golem(){
        name = "골렘";
        description = "조금 느리지만 강력한 골렘입니다.";
        hurtSound = Sound.ENTITY_IRON_GOLEM_HURT;
        deathSound = Sound.ENTITY_IRON_GOLEM_DEATH;
    }

    @Override
    protected void initPlayerAttribute(Player player) {
        super.initPlayerAttribute(player);;
        player.getAttribute(Attribute.GENERIC_ARMOR).setBaseValue(4);
        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(26);
        player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(2);
        player.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(5f);
        player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.1D);
        player.setHealth(26);
        Util.makeGettingAbilityParticle(player, Particle.LANDING_OBSIDIAN_TEAR);
    }

}
