package com.minshigee.playerchanger.repositories.abilities;

import com.minshigee.playerchanger.domain.AbilityInfo;
import com.minshigee.playerchanger.domain.abilities.Ability;
import com.minshigee.playerchanger.domain.abilities.interfaces.AbilityCode;
import com.minshigee.playerchanger.util.Util;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.Objects;
import java.util.Random;

public class GoldenPig extends Ability {

    public GoldenPig(){
        name = "황금돼지";
        description = "피격 시 좋은 것들을 흘립니다. 하지만 쉽게 당하지는 않습니다.";
        hurtSound = Sound.ENTITY_PIG_HURT;
        deathSound = Sound.ENTITY_PIG_DEATH;
    }

    Random random = new Random();

    @Override
    protected void initPlayerAttribute(Player player) {
        super.initPlayerAttribute(player);
        Util.makeGettingAbilityParticle(player, Particle.VILLAGER_ANGRY);
    }

    //TODO 황금돼지 이벤트 핸들링
    public void mapDamagedEvent(Player player){
        if(AbilityInfo.isExistParticipantInAbilities(player, AbilityCode.GoldenPig)) {
            ((GoldenPig)AbilityInfo.getAbilityObject(AbilityCode.GoldenPig))
                    .damagedEventGoldenPig(player);
        }
    }

    // TODO 황금돼지 능력 코드 업데이트.
    public void damagedEventGoldenPig(Player player) {
        int r = random.nextInt(100);
        if(r < 75)
            dropItemToWorld(player, Material.PORKCHOP, random.nextInt(3));
        else if(r < 96)
            dropItemToWorld(player, Material.GOLD_INGOT, random.nextInt(3));
        else if(r < 100)
            dropItemToWorld(player, Material.GOLDEN_APPLE, 2);

        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,20,1));
    }

    private void dropItemToWorld(Player player, Material material , int amount){
        if(amount < 1)
            return;
        ItemStack item = new ItemStack(material, 1);
        for(int i = 0; i < amount; i++){
            player.getLocation().getWorld().dropItemNaturally(player.getLocation().add(0,1,0), item)
                    .setVelocity(new Vector(random.nextInt(4) - 2, random.nextInt(3), random.nextInt(4) - 2));
        }
    }
}
