package com.minshigee.playerchanger.logic.mission.domain;

import com.minshigee.playerchanger.logic.mission.domain.mission.*;
import joptsimple.internal.Reflection;
import org.bukkit.Bukkit;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;

public class MissionFactory {

    private static ArrayList<Class<? extends Mission>> missionClazz = new ArrayList<>(){{
        add(BreakBlockMission.class);
        add(EatFoodMission.class);
        //add(InteractAssignBlockMission.class);
        //add(KillPlayerMission.class);
        //add(TakeItemMission.class);
    }};

    private static ArrayList<Class<? extends Mission>> initMissionClass(){
        //TODO mission 패키지의 mission들을 reflection으로 등록시키기.
        return null;
    }

    private static Random random = new Random();

    public static ArrayList<? extends Mission> createMissions(int count) {
        ArrayList<Mission> missions = new ArrayList<>();
        for(int i = 0; i < count; i++){
            Optional<? extends Mission> mission = createMission(i);
            if(mission.isEmpty()){
                i--;
                continue;
            }
            missions.add(mission.get());
        }
        return missions;
    }

    private static Optional<? extends Mission> createMission(int num){
        int r = random.nextInt(missionClazz.size());
        try {
            Class<? extends Mission> mission = missionClazz.get(r);
            Constructor<? extends Mission> constructor = mission.getConstructor(Integer.TYPE);
            Optional<Mission> optMission = Optional.of(constructor.newInstance(num));
            return optMission;
        }
        catch (Exception e){
            Bukkit.getConsoleSender().sendMessage("미션을 생성할 수 없습니다.");
            return Optional.empty();
        }
    }

}
