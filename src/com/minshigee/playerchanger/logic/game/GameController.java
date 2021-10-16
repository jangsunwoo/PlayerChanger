package com.minshigee.playerchanger.logic.game;

import com.minshigee.playerchanger.PlayerChanger;
import com.minshigee.playerchanger.domain.GameState;
import com.minshigee.playerchanger.domain.annotation.IsController;
import com.minshigee.playerchanger.domain.annotation.MappingCommand;
import com.minshigee.playerchanger.domain.annotation.MappingEvent;
import com.minshigee.playerchanger.util.ConsoleLogs;
import com.minshigee.playerchanger.domain.annotation.NeedOP;
import com.minshigee.playerchanger.domain.module.Controller;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEvent;

@IsController
public class GameController extends Controller<GameRepositoy> {
    public GameController(GameRepositoy repository) {
        super(repository);
    }

    @MappingCommand(arg = "start", needOp = true, states = {GameState.Disable, GameState.Waitting})
    public void executeStart(Player player, String[] args){
        repository.excuteGameStart();
    }
    @MappingCommand(arg = "stop", needOp = true, states = {GameState.Waitting, GameState.Enable, GameState.Freezing})
    public void executeStop(Player player, String[] args){

    }
    @MappingCommand(arg = "setter", needOp = true, states = {GameState.Disable})
    public void executeSetter(Player player, String[] args){
        repository.makePlayerSetterOrNot(player);
    }

    @MappingCommand(arg = "help", needOp = false, states = {GameState.Disable})
    public void executeHelp(Player player, String[] args){

    }
    @MappingEvent(states = {GameState.Disable})
    public void updateGameWroldPos(PlayerInteractEvent event){
        repository.updateGameWorldPos(event);
        repository.updateGameCoreBolck(event);
        PlayerChanger.singleton.saveConfig();
    }

    @MappingEvent(states = {GameState.Disable})
    public void hurtPlayer(EntityDamageEvent event){

    }
}
