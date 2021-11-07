package com.minshigee.playerchanger.logic.ability.domain;

import com.minshigee.playerchanger.domain.Participant;
import com.minshigee.playerchanger.domain.Role;
import com.minshigee.playerchanger.logic.ability.domain.interface_.iAbilities;
import com.minshigee.playerchanger.logic.game.GameData;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Abilities implements iAbilities {

    protected static HashMap<Participant, Ability> partsAbility = new HashMap<>();

    public enum Ability {
        None,
        GoldenPig,
        Missionary
    }

    protected Ability ability;

    @Override
    public <T> Player updateAbility(T event) {
        return null;
    }

    public Abilities(Ability ability) {
        this.ability = ability;
    }

    public Ability getAbility() { return this.ability; }

    public static HashMap<Participant, Ability> getPartsAbility()
    {
        return partsAbility;
    }

    public ArrayList<Participant> getThisAbilityParts(Ability ability)
    {
        Set<Map.Entry<Participant, Ability>> entrySet = Abilities.getPartsAbility().entrySet();
        ArrayList<Participant> tmpParts = new ArrayList<>();
        for (Map.Entry<Participant, Ability> entry : entrySet) {
            if (entry.getValue().equals(ability)) {
                tmpParts.add(entry.getKey());
            }
        }
        return tmpParts;
    }
}
