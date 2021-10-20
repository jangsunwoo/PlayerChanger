package com.minshigee.playerchanger.logic.mission.domain.interface_;

import com.minshigee.playerchanger.logic.mission.domain.Mission;
import org.bukkit.entity.Player;

public interface IMission {
    public<T> Player updateMission(T event);
    public Integer getPoint();
    public Player getClearPlayer();
    public String getDescription();
    public void generateItem();
    public void changeBlocks();
}
