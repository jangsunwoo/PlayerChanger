package com.minshigee.playerchanger.logic.ability;

import com.minshigee.playerchanger.PlayerChanger;
import com.minshigee.playerchanger.domain.Participant;
import com.minshigee.playerchanger.domain.annotation.MappingChange;
import com.minshigee.playerchanger.domain.module.Repository;
import com.minshigee.playerchanger.logic.ability.domain.Abilities;
import com.minshigee.playerchanger.logic.change.ChangeController;
import com.minshigee.playerchanger.logic.game.GameData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.Arrays;

public class AbilitiesRepository extends Repository<AbilitiesData> {
    public AbilitiesRepository(AbilitiesData localDB, Integer viewCode) {
        super(localDB, viewCode);

        Arrays.stream(this.getClass().getDeclaredMethods())
                .filter(method -> method.getDeclaredAnnotation(MappingChange.class) != null)
                .forEach(method -> ((ChangeController)PlayerChanger.getInstanceOfClass(ChangeController.class)).registerChangeMethod(viewCode, method));
    }

    public<T> void updateAbility(T event)
    {
        if(!(event instanceof Event))
            return;
        Bukkit.getConsoleSender().sendMessage("----------------");
        for(Abilities abilities : localDB.getAbility()){
            abilities.updateAbility(event);
        }
    }

    public void resetAbilities(){
        reloadAbilities();
        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "능력을 세팅합니다.");
    }

    private void reloadAbilities(){
        localDB.resetAbilities();
        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "능력을 리로딩합니다.");
    }

    @MappingChange
    public void chageAbility(Player from, Player to){
        Participant fromPart = GameData.getParticipantByPlayer(from).get();
        Participant toPart = GameData.getParticipantByPlayer(to).get();
        var tmp = Abilities.getPartAbility(fromPart);
        Abilities.setPartAbility(fromPart, Abilities.getPartAbility(toPart));
        Abilities.setPartAbility(toPart, tmp);
    }
}
