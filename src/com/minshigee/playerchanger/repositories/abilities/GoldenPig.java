package com.minshigee.playerchanger.repositories.abilities;

import com.minshigee.playerchanger.domain.AbilityInfo;
import com.minshigee.playerchanger.domain.abilities.Ability;
import com.minshigee.playerchanger.domain.abilities.interfaces.AbilityCode;
import com.minshigee.playerchanger.util.Util;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.Objects;

public class GoldenPig extends Ability {

    public GoldenPig(){
        name = "황금돼지";
        description = "피격 시 확률로 금주괴, 황금사과, 돼지고기를 흘립니다. 도망을 잘 다닙니다.";
    }

    @Override
    protected void initPlayerAttribute(Player player) {
        super.initPlayerAttribute(player);
        Util.makeGettingAbilityParticle(player, Particle.VILLAGER_ANGRY);
    }

    //TODO 황금돼지 이벤트 핸들링
    public void mapDamagedEvent(EntityDamageEvent event){
        if(event.getEntity().getType().equals(EntityType.PLAYER)){
            Player tmpPlayer = (Player)event.getEntity();
            if(AbilityInfo.isExistParticipantInAbilities(tmpPlayer, AbilityCode.GoldenPig)) {
                ((GoldenPig)AbilityInfo.getAbilityObject(AbilityCode.GoldenPig))
                        .damagedEventGoldenPig(tmpPlayer, event);
            }
        }
    }

    // TODO 황금돼지 능력 코드 업데이트.
    public void damagedEventGoldenPig(Player player, EntityDamageEvent event) {


        playDamagedSound(player);
    }

    public void playDamagedSound(Player player){
        player.playSound(player.getLocation(), Sound.ENTITY_PIG_HURT, 3, 1);
    }
}
