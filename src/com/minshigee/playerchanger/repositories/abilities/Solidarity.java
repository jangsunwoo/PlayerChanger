package com.minshigee.playerchanger.repositories.abilities;

import com.minshigee.playerchanger.domain.AbilityInfo;
import com.minshigee.playerchanger.domain.PlayInfo;
import com.minshigee.playerchanger.domain.abilities.Ability;
import com.minshigee.playerchanger.domain.abilities.interfaces.AbilityCode;
import com.minshigee.playerchanger.util.Util;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.Objects;
import java.util.Random;

public class Solidarity extends Ability {

    public Solidarity(){
        name = "전도사";
        description = "사람들은 당신과 고통까지도 함께할 것입니다.";
        hurtSound = Sound.ENTITY_VILLAGER_HURT;
        deathSound = Sound.ENTITY_VILLAGER_DEATH;
    }

    @Override
    protected void initPlayerAttribute(Player player) {
        super.initPlayerAttribute(player);
        Util.makeGettingAbilityParticle(player, Particle.HEART);
    }

    Random random = new Random();

    //TODO 전도사 이벤트 핸들링
    public void mapDamagedEvent(Player player, EntityDamageEvent event){
        if(AbilityInfo.isExistParticipantInAbilities(player, AbilityCode.Solidarity)) {
            ((Solidarity)AbilityInfo.getAbilityObject(AbilityCode.Solidarity))
                    .damagedEventSolidarity(player, event);
        }
    }

    // TODO 전도사 능력 코드 업데이트.
    public void damagedEventSolidarity(Player player, EntityDamageEvent event) {
        int r= random.nextInt(10);
        if(r != 5)
            return;
        PlayInfo.participants.stream()
                .filter(p -> !p.equals(player))
                .forEach(p -> { p.damage(event.getDamage());
        });
    }

}
