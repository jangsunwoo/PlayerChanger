package com.minshigee.playerchanger.logic.mission;

import com.minshigee.playerchanger.PlayerChanger;
import com.minshigee.playerchanger.domain.annotation.IsController;
import com.minshigee.playerchanger.domain.module.Controller;

@IsController
public class MissionController extends Controller<MissionRepository> {
    public MissionController(MissionRepository repository) {
        super(repository);
        isAvailable = PlayerChanger.config.getBoolean("UsingMission");
    }



}
