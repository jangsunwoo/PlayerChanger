package com.minshigee.playerchanger.logic.ability;

import com.minshigee.playerchanger.PlayerChanger;
import com.minshigee.playerchanger.domain.annotation.IsController;
import com.minshigee.playerchanger.domain.module.Controller;

@IsController
public class AbilitiesController extends Controller<AbilitiesRepository> {
    public AbilitiesController(AbilitiesRepository repository) {
        super(repository);
        isAvailable = PlayerChanger.config.getBoolean("UsingAbility");
    }

}
