package com.minshigee.playerchanger.domain.abilities;

import com.minshigee.playerchanger.domain.abilities.interfaces.AbilityCode;
import com.minshigee.playerchanger.domain.abilities.interfaces.IAbility;
import com.minshigee.playerchanger.util.Util;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class Ability implements IAbility, Listener {

    protected static String name = "";
    protected static String description = "";

    @Override
    public final String getName() {
        return name;
    }

    @Override
    public final String getDescription() {
        return description;
    }

    @Override
    public void executeAbility(Player player) {
        initPlayerAttribute(player);
    }

    protected void initPlayerAttribute(Player player) {
        player.sendTitle(getName(),getDescription(),20, 260, 20);
        Util.makeGettingAbilitySound(player);
    }

}
