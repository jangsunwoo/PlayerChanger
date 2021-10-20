package com.minshigee.playerchanger.logic.mission.domain.mission;

import com.minshigee.playerchanger.logic.mission.domain.Mission;
import com.minshigee.playerchanger.util.ItemGenerate;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

public class BreakBlockMission extends Mission {

    private Material blockMaterial = Material.DIAMOND_BLOCK;

    public BreakBlockMission(int num) {
        super(num);
        this.description += "%s를 찾아 캐세요.".formatted(blockMaterial.name().toLowerCase());
        this.registerBlockMaterials.add(blockMaterial);
    }

    @Override
    public <T> Player updateMission(T event) {
        if(!(event instanceof BlockBreakEvent))
            return null;
        if(!((BlockBreakEvent) event).getBlock().getType().equals(blockMaterial))
            return null;
        BlockBreakEvent ev = (BlockBreakEvent) event;
        this.setClearPlayer(ev.getPlayer());
        return getClearPlayer();
    }

}
