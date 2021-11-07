package com.minshigee.playerchanger.logic.ability;

import com.minshigee.playerchanger.domain.Participant;
import com.minshigee.playerchanger.domain.module.Data;
import com.minshigee.playerchanger.logic.ability.domain.Abilities;
import com.minshigee.playerchanger.logic.ability.domain.AbilitiesFactory;
import com.minshigee.playerchanger.logic.game.GameData;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class AbilitiesData extends Data {

    private ArrayList<? extends Abilities> abilities = new ArrayList<>();

    public void resetAbilities(){
        Set<Participant> tempAlive = GameData.getParticipantsAlive();
        this.abilities = AbilitiesFactory.createAbilites( tempAlive.size() );
        Iterator<Participant> it = tempAlive.iterator();
        for(int i = 0; i < tempAlive.size(); i++){
            if(!it.hasNext()) continue;
            Participant next = it.next();
            Abilities.getPartsAbility().put(next, abilities.get(i).getAbility());
            Bukkit.getConsoleSender().sendMessage(next + ", " + Abilities.getPartsAbility().get(next).toString());
        }

    }

    public void clearAbilities(){
        this.abilities.clear();
    }

    public ArrayList<? extends Abilities> getAbility(){
        return abilities;
    }

}
