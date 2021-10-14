package com.minshigee.playerchanger.repositories.abilities;

import com.minshigee.playerchanger.domain.AbilityInfo;
import com.minshigee.playerchanger.domain.abilities.Ability;
import com.minshigee.playerchanger.domain.abilities.interfaces.AbilityCode;
import com.minshigee.playerchanger.util.Util;
import org.bukkit.Particle;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Random;

public class Blacksmith extends Ability {

    public Blacksmith(){
        name = "대장장이";
        description = "시간마다 손에 든 모든 것들에 귀한 가치를 부여합니다.";
    }

    @Override
    protected void initPlayerAttribute(Player player) {
        super.initPlayerAttribute(player);;
        Util.makeGettingAbilityParticle(player, Particle.DRIP_LAVA);
    }

    Random random = new Random();

    private ArrayList<Enchantment> availableEnchants = new ArrayList<>(){{
        add(Enchantment.DAMAGE_ALL);
        add(Enchantment.PROTECTION_ENVIRONMENTAL);
        add(Enchantment.DURABILITY);
        add(Enchantment.PROTECTION_FIRE);
        add(Enchantment.PROTECTION_FALL);
    }};

    private ArrayList<Integer> percentEnchantLevels = new ArrayList<>(){{
        add(70);
        add(90);
        add(98);
        add(100);
    }};

    //TODO 대장장이 이벤트 핸들링
    public void enchantBlacksmithHandItem(){
        AbilityInfo.participantAbilityInfo.get(AbilityCode.Blacksmith)
                .forEach( player -> {
                    player.getInventory().getItemInMainHand()
                            .addUnsafeEnchantment(choiceRandomEnchantment(),choiceRandomEnchantmentLevel());
                });
    }

    private Enchantment choiceRandomEnchantment(){
        return availableEnchants.get(random.nextInt(availableEnchants.size()));
    }

    private int choiceRandomEnchantmentLevel(){
        int r = random.nextInt(100);
        for(int i = 0; i < percentEnchantLevels.size(); i++){
            if(r > percentEnchantLevels.get(i))
                continue;
            return i+1;
        }
        return 1;
    }

}
