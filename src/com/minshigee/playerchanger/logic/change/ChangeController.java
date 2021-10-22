package com.minshigee.playerchanger.logic.change;

import com.minshigee.playerchanger.domain.GameState;
import com.minshigee.playerchanger.domain.annotation.IsController;
import com.minshigee.playerchanger.domain.annotation.MappingCommand;
import com.minshigee.playerchanger.domain.module.Controller;
import org.bukkit.entity.Player;

import java.lang.reflect.Method;

@IsController
public class ChangeController extends Controller<ChangeRepository> {
    public ChangeController(ChangeRepository repository) {
        super(repository);
    }

    @MappingCommand(arg = "change", needOp = true, states = {GameState.Disable,GameState.Waitting, GameState.Freezing})
    public void changePlayers(Player player, String[] args){
        repository.changePlayers(player, args);
    }

    public void registerChangeMethod(Integer code, Method method){
        repository.registerChangeMethod(code, method);
    }
}
