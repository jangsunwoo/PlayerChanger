package com.minshigee.playerchanger.logic.ability.domain.ability;

import com.minshigee.playerchanger.domain.Participant;
import com.minshigee.playerchanger.logic.ability.domain.Abilities;
import com.minshigee.playerchanger.logic.game.GameData;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class Missionary extends Abilities {
    public Missionary()
    {
        super(Ability.Missionary);
    }

    @Override
    public<T> Player updateAbility(T event)
    {
        if(!(event instanceof EntityDamageByEntityEvent))
            return null;
        if(!(((EntityDamageByEntityEvent) event).getEntity() instanceof Player)) return null;
        Random random = new Random();
        EntityDamageByEntityEvent evDamage = (EntityDamageByEntityEvent) event;
        Player ev = ((Player)(evDamage).getEntity()).getPlayer();
        ArrayList<Participant> thisAbilityParts = getThisAbilityParts(ability);
        for(Participant participant : thisAbilityParts)
        {
            Player player = participant.getPlayer();
            if(ev.equals(player) && random.nextInt(100) <= 10)
            {
                Set<Participant> tmp = GameData.getParticipantsAlive();
                for(Participant part : tmp){
                    if(!part.getPlayer().equals(ev)) part.getPlayer().damage(evDamage.getDamage());
                }
            }
        }
        return null;
    }


}
