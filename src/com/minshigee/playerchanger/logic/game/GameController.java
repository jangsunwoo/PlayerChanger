package com.minshigee.playerchanger.logic.game;

import com.minshigee.playerchanger.domain.GameState;
import com.minshigee.playerchanger.domain.annotation.MappingCommand;
import com.minshigee.playerchanger.domain.annotation.MappingEvent;
import com.minshigee.playerchanger.util.ConsoleLogs;
import com.minshigee.playerchanger.domain.annotation.NeedOP;
import com.minshigee.playerchanger.domain.module.Controller;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

public class GameController extends Controller<GameRepositoy> {
    public GameController(GameRepositoy repository) {
        super(repository);
    }

    public void executeHelp(Player player, String[] args){
        ConsoleLogs.printConsoleLog("Hello World");
    }

    @MappingCommand(
            arg = "setter",
            needOp = true,
            states = {GameState.Disable}
    )
    public void executeSetter(Player player, String[] args){
        repository.makePlayerSetter(player);
    }

    @MappingEvent(states = {GameState.Disable})
    public void hurtPlayer(EntityDamageEvent event){

    }
}
