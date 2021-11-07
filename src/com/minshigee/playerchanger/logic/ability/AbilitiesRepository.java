package com.minshigee.playerchanger.logic.ability;

import com.minshigee.playerchanger.domain.module.Repository;
import com.minshigee.playerchanger.logic.ability.domain.Abilities;
import com.minshigee.playerchanger.logic.game.GameData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public class AbilitiesRepository extends Repository<AbilitiesData> {
    public AbilitiesRepository(AbilitiesData localDB, Integer viewCode) {
        super(localDB, viewCode);
    }

    public<T> void updateAbility(T event)
    {
        if(!(event instanceof Event))
            return;
        for(Abilities abilities : localDB.getAbility()){
            Bukkit.getConsoleSender().sendMessage("----------------");
            abilities.updateAbility(event);
        }
    }

    public void resetAbilities(){
        reloadAbilities();
        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "능력을 세팅합니다.");
    }

    private void reloadAbilities(){
        localDB.resetAbilities();
        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "능력을 리로딩합니다.");
    }
}
