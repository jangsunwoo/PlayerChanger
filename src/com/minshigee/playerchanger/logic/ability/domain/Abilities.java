package com.minshigee.playerchanger.logic.ability.domain;

import com.minshigee.playerchanger.domain.Participant;
import com.minshigee.playerchanger.logic.ability.domain.interface_.iAbilities;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Abilities implements iAbilities {

    protected static HashMap<Participant, Ability> partsAbility = new HashMap<>();

    protected static HashMap<Ability, String> abilityHelp = new HashMap<>();

    protected static HashMap<Participant, Long> partsCoolDown = new HashMap<>();

    public enum Ability {
        None,
        GoldenPig,
        Missionary,
        IronMan
        }

    protected Ability ability;

    @Override
    public <T> Player updateAbility(T event) {
//        getPartsCoolDown().forEach(player -> {
//            if(partsCoolDown.get(player) > System.currentTimeMillis()){
//
//            } else {
//
//            }
//        });

        return null;
    }

    public Abilities(Ability ability) {
        this.ability = ability;
    }

    public Ability getAbility() { return this.ability; }

    public static HashMap<Participant, Ability> getPartsAbility(){
        return partsAbility;
    }

    public static void setPartAbility(Participant part, Ability ability){
        partsAbility.replace(part, ability);
    }

    public static Ability getPartAbility(Participant part){
        return partsAbility.get(part);
    }

    public static String getAbilityHelp(Ability ability){
        return abilityHelp.get(ability);
    }

    public static HashMap<Participant, Long> getPartsCoolDown() { return partsCoolDown; }

    public ArrayList<Participant> getThisAbilityParts(Ability ability){
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
