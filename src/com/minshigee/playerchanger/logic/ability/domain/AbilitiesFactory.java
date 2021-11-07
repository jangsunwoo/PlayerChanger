package com.minshigee.playerchanger.logic.ability.domain;

import com.minshigee.playerchanger.logic.ability.domain.ability.GoldenPig;
import com.minshigee.playerchanger.logic.ability.domain.ability.Missionary;
import org.bukkit.Bukkit;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

public class AbilitiesFactory {

    private static ArrayList<Class<? extends Abilities>> abilityClazz = new ArrayList<>(){{
        add(GoldenPig.class);
        add(Missionary.class);
    }};

    public static ArrayList<? extends Abilities> createAbilites(int count)
    {
        ArrayList<Abilities> abilities = new ArrayList<>();
        ArrayList<Class<? extends Abilities>> tmpClazz = new ArrayList<>(){{
            addAll(abilityClazz);
        }};
        Collections.shuffle(tmpClazz);
        for(int i = 0; i < count && i < tmpClazz.size(); i++){
            Bukkit.getConsoleSender().sendMessage("반복 횟수 : " + i);
            Optional<? extends Abilities> mission = createAbility(tmpClazz,i);
            if(mission.isEmpty()){
                continue;
            }
            abilities.add(mission.get());
        }
//        count -= tmpClazz.size();
//        tmpClazz = new ArrayList<>(abilityClazz);
//        Collections.shuffle(tmpClazz);
//        for(int i = 0; i < count; i++){
//            Optional<? extends Abilities> mission = createAbility(tmpClazz,i);
//            if(mission.isEmpty()){
//                continue;
//            }
//            abilities.add(mission.get());
//        }
        return abilities;
    }

    private static Optional<? extends Abilities> createAbility(ArrayList<Class<? extends Abilities>> tmpClazz, int num){
        try {
            Class<? extends Abilities> ability = tmpClazz.get(num % tmpClazz.size());
            Constructor<? extends Abilities> constructor = ability.getConstructor();
            return Optional.<Abilities>of(constructor.newInstance());
        }
        catch (Exception e){
            Bukkit.getConsoleSender().sendMessage("능력을 생성할 수 없습니다.");
            Bukkit.getConsoleSender().sendMessage(e.toString());
            return Optional.empty();
        }
    }
}
