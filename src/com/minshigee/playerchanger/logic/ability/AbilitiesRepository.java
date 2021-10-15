package com.minshigee.playerchanger.logic.ability;

import com.minshigee.playerchanger.domain.module.Repository;

public class AbilitiesRepository extends Repository<AbilitiesData> {
    public AbilitiesRepository(AbilitiesData localDB) {
        super(localDB);
    }
}
