package com.minshigee.playerchanger.domain.abilities;

import com.minshigee.playerchanger.domain.abilities.interfaces.AbilityCode;
import com.minshigee.playerchanger.domain.abilities.interfaces.IAbility;
import com.minshigee.playerchanger.util.Util;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class Ability implements IAbility, Listener {

    protected String name = "";
    protected String description = "";

    protected Sound hurtSound = null;
    protected Sound deathSound = null;

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
        player.sendTitle(this.getName(),this.getDescription(),20, 260, 20);
        Util.makeGettingAbilitySound(player);
    }

    public void playHurtSound(Player player){
        if(hurtSound == null)
            return;
        player.getLocation().getWorld()
                .playSound(player.getLocation(), hurtSound, 3, 1);
    }
    public void playDeathSound(Player player){
        if(hurtSound == null)
            return;
        player.getLocation().getWorld().
                playSound(player.getLocation(), deathSound, 3, 1);
    }

}
