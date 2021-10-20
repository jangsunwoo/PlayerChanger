package com.minshigee.playerchanger.logic.mission.domain.mission;

import com.minshigee.playerchanger.logic.mission.domain.Mission;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;

import java.util.Objects;

public class InteractAssignBlockMission extends Mission {

    Material assignMaterial = Material.LECTERN;

    public InteractAssignBlockMission(int num) {
        super(num);
        this.description += "독서대(상점)를 찾아 우클릭하세요.";
    }

    @Override
    public <T> Player updateMission(T event) {
        if(!(event instanceof PlayerInteractEvent))
            return null;
        PlayerInteractEvent ev = (PlayerInteractEvent) event;
        if(!ev.getAction().equals(Action.RIGHT_CLICK_BLOCK))
            return null;
        if(!Objects.requireNonNull(ev.getClickedBlock()).getType().equals(assignMaterial))
            return null;
        this.setClearPlayer(ev.getPlayer());
        return getClearPlayer();
    }
}
