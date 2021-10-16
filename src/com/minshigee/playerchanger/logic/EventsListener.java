package com.minshigee.playerchanger.logic;

import com.minshigee.playerchanger.PlayerChanger;
import com.minshigee.playerchanger.domain.annotation.MappingEvent;
import com.minshigee.playerchanger.logic.game.GameData;
import com.minshigee.playerchanger.util.ConsoleLogs;
import com.minshigee.playerchanger.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class EventsListener implements Listener {
    private HashMap<Class,HashSet<Method>> avilableMethods = new HashMap<>();

    @EventHandler
    public void damagedPlayer(EntityDamageEvent event){
        if(!(event.getEntity() instanceof Player))
            return;
        if(GameData.getParticipantByPlayer((Player)event.getEntity()).isPresent()){
            executeEventMethod(event);
        }
    }

    public EventsListener(){
        validateAvailableMethods();
    }

    private void validateAvailableMethods(){
        Util.getMappableControllers().forEach(aClass -> {
                    Arrays.stream(aClass.getDeclaredMethods())
                            .filter(method -> {
                                return method.getDeclaredAnnotation(MappingEvent.class) != null;
                            }).filter(method -> method.getParameterCount() == 1)
                            .forEach(method -> registerAvailableMethods(method.getParameters()[0], method));
                });
    }

    private<T> void registerAvailableMethods(T event, Method method){
        avilableMethods.computeIfAbsent(event.getClass(), k -> new HashSet<>());
        avilableMethods.get(event.getClass()).add(method);
    }

    private <T> void executeEventMethod(T event){
        avilableMethods.get(event.getClass()).forEach(method -> {
            if(Arrays.stream(((MappingEvent) method.getDeclaredAnnotation(MappingEvent.class)).states())
                    .anyMatch(gameState -> gameState.equals(GameData.getGameState()))){
                try {
                    method.invoke(PlayerChanger.getInstanceOfClass(method.getDeclaringClass()), event);
                }
                catch (Exception e){
                    ConsoleLogs.printConsoleLog(ChatColor.RED + method.getName() + " 의 실행을 실패했습니다.");
                }
            }
        });

    }
}
