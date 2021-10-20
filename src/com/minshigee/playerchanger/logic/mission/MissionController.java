package com.minshigee.playerchanger.logic.mission;

import com.minshigee.playerchanger.PlayerChanger;
import com.minshigee.playerchanger.domain.GameState;
import com.minshigee.playerchanger.domain.annotation.IsController;
import com.minshigee.playerchanger.domain.annotation.MappingCommand;
import com.minshigee.playerchanger.domain.annotation.MappingEvent;
import com.minshigee.playerchanger.domain.module.Controller;
import com.minshigee.playerchanger.logic.game.GameData;
import com.minshigee.playerchanger.util.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;

@IsController
public class MissionController extends Controller<MissionRepository> {
    public MissionController(MissionRepository repository) {
        super(repository);
        isAvailable = PlayerChanger.config.getBoolean("UsingMission");
    }

    @MappingCommand(arg = "start", needOp = true, states = {GameState.Enable})
    public void startMission(Player player, String[] args){
        repository.resetMissions();
    }
    @MappingCommand(arg = "stop", needOp = true, states = {GameState.Disable})
    public void stopMission(Player player, String[] args){
        repository.clearMissions();
        MessageUtil.printConsoleLog(ChatColor.GREEN + "미션이 초기화 되었습니다.");
    }

    @MappingEvent(states = GameState.Enable)
    public void consumeItemEvent(PlayerItemConsumeEvent event){
        repository.updateMissions(event);
    }
    @MappingEvent(states = GameState.Enable)
    public void breakBlockEvent(BlockBreakEvent event){
        repository.updateMissions(event);
    }
    @MappingEvent(states = GameState.Enable)
    public void interactPlayerEvnet(PlayerInteractEvent event){repository.updateMissions(event);}
    @MappingEvent(states = GameState.Enable)
    public void deathPlayer(PlayerDeathEvent event){
        repository.updateMissions(event);
    }



}
