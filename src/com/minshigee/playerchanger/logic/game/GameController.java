package com.minshigee.playerchanger.logic.game;

import com.minshigee.playerchanger.PlayerChanger;
import com.minshigee.playerchanger.domain.GameState;
import com.minshigee.playerchanger.domain.annotation.IsController;
import com.minshigee.playerchanger.domain.annotation.MappingCommand;
import com.minshigee.playerchanger.domain.annotation.MappingEvent;
import com.minshigee.playerchanger.domain.module.Controller;
import com.minshigee.playerchanger.util.MessageUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;

@IsController
public class GameController extends Controller<GameRepositoy> {
    public GameController(GameRepositoy repository) {
        super(repository);
    }

    @MappingCommand(arg = "start", needOp = true, states = {GameState.Disable, GameState.Waitting})
    public void executeStart(Player player, String[] args){
        repository.excuteGameStart(player);
    }
    @MappingCommand(arg = "stop", needOp = true, states = {GameState.Waitting, GameState.Enable, GameState.Freezing})
    public void executeStop(Player player, String[] args){
        repository.executeGameStop(player);
    }
    @MappingCommand(arg = "setter", needOp = true, states = {GameState.Disable})
    public void executeSetter(Player player, String[] args){
        repository.makePlayerSetterOrNot(player);
    }
    @MappingCommand(arg = "join", needOp = false, states = {GameState.Waitting})
    public void executeJoin(Player player, String[] args){repository.executeJoin(player);}
    @MappingCommand(arg = "spectator", needOp = false, states = {GameState.Waitting})
    public void executeSpectator(Player player, String[] args){repository.executeSpectator(player);}
    @MappingCommand(arg = "leave", needOp = false, states = {GameState.Waitting})
    public void executeLeave(Player player, String[] args){repository.executeLeave(player);}
    @MappingCommand(arg = "help", needOp = false, states = {GameState.Disable})
    public void executeHelp(Player player, String[] args){

    }
    @MappingEvent(states = {GameState.Disable})
    public void updateGameWroldPos(PlayerInteractEvent event){repository.updateGameWorldPos(event);repository.updateGameCoreBolck(event);PlayerChanger.singleton.saveConfig();}

    @MappingEvent(states = {GameState.Disable})
    public void playerBreakBlock(BlockBreakEvent event) {repository.breakBlockSetter(event);}


    @MappingEvent(states = {GameState.Disable})
    public void hurtPlayer(EntityDamageEvent event){

    }
}
