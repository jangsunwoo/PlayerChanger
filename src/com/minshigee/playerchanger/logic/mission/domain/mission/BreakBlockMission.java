package com.minshigee.playerchanger.logic.mission.domain.mission;

import com.minshigee.playerchanger.logic.mission.domain.Mission;
import com.minshigee.playerchanger.util.MessageUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;

public class BreakBlockMission extends Mission {

    private Material blockMaterial = Material.DIAMOND_BLOCK;

    public BreakBlockMission(int num) {
        super(num);
        this.description = "%s을 캐세요.".formatted(blockMaterial.name().toLowerCase());
    }

    @Override
    public <T> Player updateMission(T event) {
        MessageUtil.printConsoleLog("1");
        if(!(event instanceof BlockBreakEvent))
            return null;
        MessageUtil.printConsoleLog("1");
        if(!((BlockBreakEvent) event).getBlock().getType().equals(blockMaterial))
            return null;
        MessageUtil.printConsoleLog("1");
        BlockBreakEvent ev = (BlockBreakEvent) event;
        this.setClearPlayer(ev.getPlayer());
        return getClearPlayer();
    }
}
