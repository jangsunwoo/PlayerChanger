package com.minshigee.playerchanger.logic.ability;

import com.minshigee.playerchanger.PlayerChanger;
import com.minshigee.playerchanger.domain.GameState;
import com.minshigee.playerchanger.domain.Participant;
import com.minshigee.playerchanger.domain.annotation.IsController;
import com.minshigee.playerchanger.domain.annotation.MappingCommand;
import com.minshigee.playerchanger.domain.annotation.MappingEvent;
import com.minshigee.playerchanger.domain.module.Controller;
import com.minshigee.playerchanger.logic.ability.domain.Abilities;
import com.minshigee.playerchanger.logic.game.GameData;
import com.minshigee.playerchanger.util.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.entity.Player;
import net.md_5.bungee.api.ChatMessageType;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import javax.swing.text.html.Option;
import java.util.HashMap;
import java.util.Optional;

@IsController
public class AbilitiesController extends Controller<AbilitiesRepository> {
    public AbilitiesController(AbilitiesRepository repository) {
        super(repository);
        isAvailable = PlayerChanger.config.getBoolean("UsingAbility");
    }

    @MappingCommand(arg = "start", needOp = true, states = {GameState.Enable})
    public void startAbilities(Player player, String[] args){
        repository.resetAbilities();
    }

    @MappingCommand(arg = "ability", needOp = true, states = {GameState.Enable,GameState.Waitting,GameState.Freezing})
    public void abilityCommand(Player player, String[] args){
        if(args != null && args.length < 2)
        {
            MessageUtil.printMsgToPlayer(ChatMessageType.CHAT, player, "/ph ability random");
            MessageUtil.printMsgToPlayer(ChatMessageType.CHAT, player, "/ph ability help");
        }
        else if(args != null && args[1].equals("help")) {
            MessageUtil.printMsgToPlayer(ChatMessageType.CHAT, player, "/ph ability help");
            Optional<Participant> playerPart = GameData.getParticipantByPlayer(player);
            HashMap<Participant, Abilities.Ability> tmp = Abilities.getPartsAbility();
            MessageUtil.printMsgToPlayer(ChatMessageType.CHAT, player, tmp.get(playerPart.get()).toString());
        }
        else if(args != null && args[1].equals("random"))
        {
            MessageUtil.printMsgToPlayer(ChatMessageType.CHAT, player, "/ph ability random");
        }
    }

    @MappingEvent(states = GameState.Enable)
    public void entityDamageByEntityEvent(EntityDamageByEntityEvent event){
        repository.updateAbility(event);
    }
}
