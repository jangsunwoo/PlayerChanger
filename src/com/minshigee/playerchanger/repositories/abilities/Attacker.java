package com.minshigee.playerchanger.repositories.abilities;

import com.minshigee.playerchanger.domain.abilities.Ability;
import com.minshigee.playerchanger.util.Util;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;

public class Attacker extends Ability {

    public Attacker(){
        name = "공격자";
        description = "유효타를 더 빨리 날리는 것도 좋아보이네요.";
    }

    @Override
    protected void initPlayerAttribute(Player player) {
        super.initPlayerAttribute(player);;
        player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(
                player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).getDefaultValue() * 1.5D
        );
        Util.makeGettingAbilityParticle(player, Particle.DRAGON_BREATH);
    }

}
