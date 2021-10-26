package com.minshigee.playerchanger.logic.ability;

import com.minshigee.playerchanger.domain.module.Repository;
import org.bukkit.entity.Player;

public class AbilitiesRepository extends Repository<AbilitiesData> {
    public AbilitiesRepository(AbilitiesData localDB, Integer viewCode) {
        super(localDB, viewCode);
    }
}
