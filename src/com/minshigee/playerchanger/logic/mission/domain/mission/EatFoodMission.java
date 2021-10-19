package com.minshigee.playerchanger.logic.mission.domain.mission;

import com.minshigee.playerchanger.logic.mission.domain.Mission;
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
        this.prefix = "음식을 드세요 : ";
        this.eatMaterial = eatMaterials[random.nextInt(eatMaterials.length)];
        this.description = eatMaterial.name();
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
