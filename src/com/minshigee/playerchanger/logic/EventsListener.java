package com.minshigee.playerchanger.logic;

import com.minshigee.playerchanger.PlayerChanger;
import com.minshigee.playerchanger.domain.annotation.MappingEvent;
import com.minshigee.playerchanger.logic.game.GameData;
import com.minshigee.playerchanger.util.MessageUtil;
import com.minshigee.playerchanger.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class EventsListener implements Listener {
    private HashMap<String,HashSet<Method>> avilableMethods = new HashMap<>();

    @EventHandler
    public void damagedPlayer(EntityDamageEvent event){
        if(!(event.getEntity() instanceof Player))
            return;
        executeEventMethod(event.getEventName(),event);
        if(GameData.getParticipantByPlayer((Player)event.getEntity()).isPresent()){
            executeEventMethod(event.getEventName(),event);
        }
    }
    @EventHandler public void interactPlayer(PlayerInteractEvent event){executeEventMethod(event.getEventName(),event);}
    @EventHandler void breakBlockPlayer(BlockBreakEvent event){executeEventMethod(event.getEventName(),event);}

    public EventsListener(){
        MessageUtil.printConsoleLog(ChatColor.DARK_AQUA + "이벤트 등록을 시작합니다.");
        validateAvailableMethods();
    }

    private void validateAvailableMethods(){
        Util.getMappableControllers().forEach(aClass -> {
                    Arrays.stream(aClass.getDeclaredMethods())
                            .filter(method -> {
                                return method.getDeclaredAnnotation(MappingEvent.class) != null;
                            }).filter(method -> method.getParameterCount() == 1)
                            .forEach(method -> {
                                registerAvailableMethods(method.getParameters()[0].getType().getSimpleName(), method);
                            });
                });
        MessageUtil.printConsoleLog(ChatColor.DARK_AQUA + "이벤트 등록이 끝났습니다.");
    }

    private void registerAvailableMethods(String typeName, Method method){
        avilableMethods.computeIfAbsent(typeName, k -> new HashSet<>());
        avilableMethods.get(typeName).add(method);
        MessageUtil.printConsoleLog(ChatColor.GREEN + typeName + "의 " + method.getName() + "이 등록되었습니다.");
    }

    private <T> void executeEventMethod(String typeName, T event){
        if(avilableMethods.get(typeName) == null)
            return;
        avilableMethods.get(typeName).forEach(method -> {
            if(Arrays.stream(method.getDeclaredAnnotation(MappingEvent.class).states())
                    .anyMatch(gameState -> gameState.equals(GameData.getGameState()))){
                try {
                    method.invoke(PlayerChanger.getInstanceOfClass(method.getDeclaringClass()), event);
                }
                catch (Exception e){
                    MessageUtil.printConsoleLog(ChatColor.RED + method.getName() + " 의 실행을 실패했습니다.");
                }
            }
        });

    }
}
