package com.minshigee.playerchanger.logic.game;

import com.minshigee.playerchanger.domain.module.Repository;

public class GameRepositoy extends Repository<GameData> {
    public GameRepositoy(GameData localDB) {
        super(localDB);
    }
}
