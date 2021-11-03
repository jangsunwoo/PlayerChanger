package com.minshigee.playerchanger.logic.ability;

import com.minshigee.playerchanger.domain.module.Repository;
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

    }
}
