package com.minshigee.playerchanger.logic.change;

import com.minshigee.playerchanger.domain.module.Repository;

public class ChangeRepository extends Repository<ChangeData> {
    public ChangeRepository(ChangeData localDB) {
        super(localDB);
    }
}
