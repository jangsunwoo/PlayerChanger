package com.minshigee.playerchanger.util;

import com.minshigee.playerchanger.PlayerChanger;
import com.minshigee.playerchanger.domain.Participant;
import com.minshigee.playerchanger.domain.annotation.IsController;
import com.minshigee.playerchanger.domain.module.Controller;
import com.minshigee.playerchanger.logic.game.GameData;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

public class Util {
    public static ArrayList<Class> getMappableControllers(){
        return (ArrayList<Class>) PlayerChanger.getContainerKeys().stream()
                .filter(aClass -> {
                    return aClass.getDeclaredAnnotation(IsController.class) != null;
                })
                .filter(aClass -> {
                    try {
                        return ((Controller)PlayerChanger.getInstanceOfClass(aClass)).getIsAvailable();
                    }
                    catch (Exception e){
                        MessageUtil.printConsoleLog(ChatColor.RED + "Controller의 isAvailable 참조에 실패했습니다.");
                        return false;
                    }
                }).collect(Collectors.toList());
    }

    public static ArrayList<Participant> getShuffledAliveParticipants(){
        ArrayList<Participant> res = new ArrayList<>(GameData.getParticipantsAlive());
        Collections.shuffle(res);
        return res;
    }
}
