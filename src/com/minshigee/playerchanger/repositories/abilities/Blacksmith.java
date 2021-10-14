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
        description = "손에 든 아이템이 10초마다 랜덤으로 강화된다.";
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
        add(20);
        add(8);
        add(2);
    }};

    //TODO 대장장이 이벤트 핸들링
    public void enchantBlacksmithHandItem(){
        AbilityInfo.participantAbilityInfo.get(AbilityCode.Blacksmith)
                .forEach( player -> {
                    player.getInventory().getItemInMainHand()
                            .addEnchantment(choiceRandomEnchantment(),choiceRandomEnchantmentLevel());
                });
    }

    private Enchantment choiceRandomEnchantment(){
        return availableEnchants.get(random.nextInt(availableEnchants.size()));
    }

    private int choiceRandomEnchantmentLevel(){
        int r = random.nextInt(100);
        return percentEnchantLevels.stream().filter(integer -> r < integer).findFirst().get();
    }

}
