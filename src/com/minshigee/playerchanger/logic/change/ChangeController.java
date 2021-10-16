package com.minshigee.playerchanger.logic.change;

import com.minshigee.playerchanger.PlayerChanger;
import com.minshigee.playerchanger.domain.module.Controller;

public class ChangeController extends Controller<ChangeRepository> {
    public ChangeController(ChangeRepository repository) {
        super(repository);
        isAvailable = PlayerChanger.config.getBoolean("UsingChanging");
    }

}
