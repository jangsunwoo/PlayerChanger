package com.minshigee.playerchanger.logic.mission;

import com.minshigee.playerchanger.PlayerChanger;
import com.minshigee.playerchanger.domain.GameState;
import com.minshigee.playerchanger.domain.annotation.IsController;
import com.minshigee.playerchanger.domain.annotation.MappingCommand;
import com.minshigee.playerchanger.domain.annotation.MappingEvent;
import com.minshigee.playerchanger.domain.module.Controller;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

@IsController
public class MissionController extends Controller<MissionRepository> {
    public MissionController(MissionRepository repository) {
        super(repository);
        isAvailable = PlayerChanger.config.getBoolean("UsingMission");
    }

    @MappingCommand(arg = "start", needOp = true, states = {GameState.Waitting})
    public void startMission(Player player, String[] args){
        repository.resetMissions();
    }

    @MappingEvent(states = GameState.Enable)
    public void onEvent(Event event){
        repository.updateMissions(event);
    }

}
