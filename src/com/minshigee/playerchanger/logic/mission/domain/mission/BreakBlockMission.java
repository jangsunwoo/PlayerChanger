package com.minshigee.playerchanger.logic.mission.domain.mission;

import com.minshigee.playerchanger.logic.mission.domain.Mission;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemBreakEvent;

public class BreakBlockMission extends Mission {

    private Material blockMaterial = Material.DIAMOND_BLOCK;

    public BreakBlockMission(int num) {
        super(num);
        this.prefix = "블럭을 캐세요 : ";
        this.description = blockMaterial.name();
    }

    @Override
    public <T> Player updateMission(T event) {
        if(!(event instanceof PlayerItemBreakEvent))
            return null;
        if(!((PlayerItemBreakEvent) event).getBrokenItem().getType().equals(blockMaterial))
            return null;
        PlayerItemBreakEvent ev = (PlayerItemBreakEvent) event;
        this.setClearPlayer(ev.getPlayer());
        return getClearPlayer();
    }
}
