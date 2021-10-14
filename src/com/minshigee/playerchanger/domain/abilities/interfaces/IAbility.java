package com.minshigee.playerchanger.domain.abilities.interfaces;

import org.bukkit.entity.Player;

public interface IAbility {

    public String getName();
    public String getDescription();

    public void executeAbility(Player player);
    public void playHurtSound(Player player);

}
