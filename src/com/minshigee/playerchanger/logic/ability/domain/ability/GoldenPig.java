package com.minshigee.playerchanger.logic.ability.domain.ability;

import com.minshigee.playerchanger.domain.Participant;
import com.minshigee.playerchanger.logic.ability.domain.Abilities;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public class GoldenPig extends Abilities {

    public GoldenPig()
    {
        super(Ability.GoldenPig);
    }

    @Override
    public <T> Player updateAbility(T event) {
        Bukkit.getConsoleSender().sendMessage(super.partsAbility.toString());
        if(!(event instanceof EntityDamageByEntityEvent))
            return null;
        if(!(((EntityDamageByEntityEvent) event).getEntity() instanceof Player)) return null;
        Player ev = ((Player) ((EntityDamageByEntityEvent) event).getEntity()).getPlayer();
        ArrayList<Participant> thisAbilityParts = getThisAbilityParts(ability);
        for(Participant participant : thisAbilityParts)
        {
            Player player = participant.getPlayer();
            if(ev.equals(player))
            {
                player.getWorld().dropItemNaturally(player.getLocation(), new ItemStack(Material.APPLE, 1));
                player.getWorld().dropItemNaturally(player.getLocation(), new ItemStack(Material.GOLD_NUGGET, 1));
                player.getWorld().dropItemNaturally(player.getLocation(), new ItemStack(Material.PORKCHOP, 1));
            }
        }
        return null;
    }
}
