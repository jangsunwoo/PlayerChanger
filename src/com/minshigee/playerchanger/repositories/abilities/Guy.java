package com.minshigee.playerchanger.repositories.abilities;

import com.minshigee.playerchanger.domain.AbilityInfo;
import com.minshigee.playerchanger.domain.abilities.Ability;
import com.minshigee.playerchanger.domain.abilities.interfaces.AbilityCode;
import com.minshigee.playerchanger.util.Util;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;

public class Guy extends Ability {

    public Guy(){
        name = "Guy";
        description = "세상에는 무거운 갑주가 방해가 되는 사람이 있죠.";
        hurtSound = Sound.ENTITY_SHULKER_HURT;
        deathSound = Sound.ENTITY_SHULKER_DEATH;
    }

    @Override
    protected void initPlayerAttribute(Player player) {
        super.initPlayerAttribute(player);
        Util.makeGettingAbilityParticle(player, Particle.EXPLOSION_NORMAL);
    }

    //TODO 가이 이벤트 핸들링
    public void executeGuyEvent(){
        AbilityInfo.participantAbilityInfo.get(AbilityCode.Guy)
                .forEach(this::executeGuyPassive);
    }

    //TODO 가이 이벤트 실행
    private void executeGuyPassive(Player player){
        boolean ev = Arrays.stream(player.getInventory().getArmorContents())
                .anyMatch(itemStack -> itemStack != null);
        if(!ev) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 40, 3));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 120, 1));
            player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 40, 1));
            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 40, 3));
            player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20, 0));
        }
    }

}
