package com.minshigee.playerchanger.logic.game;

import com.minshigee.playerchanger.constant.ConsoleLogs;
import com.minshigee.playerchanger.domain.annitation.NeedOP;
import com.minshigee.playerchanger.domain.module.Controller;
import org.bukkit.entity.Player;

public class GameController extends Controller<GameRepositoy> {
    public GameController(GameRepositoy repository) {
        super(repository);
    }

    @NeedOP
    public void executeHelp(Player player, String[] args){
        ConsoleLogs.printConsoleLog("Hello World");
    }
}
