package com.minshigee.playerchanger.logic.ability.domain.abilities;

import com.minshigee.playerchanger.logic.ability.domain.Abilities;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

public class GoldenPig extends Abilities {

    private Player player = null;

    public GoldenPig(Player player)
    {
        this.player = player;
    }

    @Override
    public <T> Player updateAbilities(T event) {
        if(!(event instanceof EntityDamageEvent))
            return null;
        EntityDamageEvent ev = (EntityDamageEvent) event;
        if(ev.getEntity().equals(player))
        {
            String pWorld = player.getWorld().toString();
            Bukkit.getWorld(pWorld).dropItemNaturally(player.getLocation(), new ItemStack(Material.APPLE, 1));
            Bukkit.getWorld(pWorld).dropItemNaturally(player.getLocation(), new ItemStack(Material.GOLD_NUGGET, 1));
            Bukkit.getWorld(pWorld).dropItemNaturally(player.getLocation(), new ItemStack(Material.PORKCHOP, 1));
        }
        return null;
    }
}
