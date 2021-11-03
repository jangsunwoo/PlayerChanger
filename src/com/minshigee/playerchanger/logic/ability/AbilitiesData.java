package com.minshigee.playerchanger.logic.ability;

import com.minshigee.playerchanger.domain.module.Data;
import com.minshigee.playerchanger.logic.ability.domain.Abilities;
import com.minshigee.playerchanger.logic.ability.domain.Ability;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;

public class AbilitiesData extends Data {

    private ArrayList<? extends Abilities> abilities = new ArrayList<>();

    public void resetMissions(){

    }

    public void clearMissions(){
        this.abilities.clear();
    }

    public ArrayList<? extends Abilities> getMissions(){
        return abilities;
    }
}
