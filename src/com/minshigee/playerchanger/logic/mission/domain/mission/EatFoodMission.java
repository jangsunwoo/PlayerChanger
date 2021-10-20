package com.minshigee.playerchanger.logic.mission.domain.mission;

import com.minshigee.playerchanger.logic.mission.domain.Mission;
import com.minshigee.playerchanger.util.MessageUtil;
import com.mojang.datafixers.util.Pair;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemConsumeEvent;

import java.util.Random;

public class EatFoodMission extends Mission {

    private static final Material[] eatMaterials = {
            Material.APPLE,
            Material.GOLDEN_APPLE,
            Material.COOKED_PORKCHOP
    };
    private Material eatMaterial;
    private Random random = new Random();

    public EatFoodMission(int num) {
        super(num);
        this.eatMaterial = eatMaterials[random.nextInt(eatMaterials.length)];
        this.description += "%s을 드세요.".formatted(eatMaterial.name().toLowerCase());
        this.registerItemMaterials.add(new Pair<>(this.eatMaterial,1));
    }

    @Override
    public <T> Player updateMission(T event) {
        if(!(event instanceof PlayerItemConsumeEvent))
            return null;
        if(!((PlayerItemConsumeEvent) event).getItem().getType().equals(eatMaterial))
            return null;
        PlayerItemConsumeEvent ev = (PlayerItemConsumeEvent) event;
        this.setClearPlayer(ev.getPlayer());
        return getClearPlayer();
    }
}
