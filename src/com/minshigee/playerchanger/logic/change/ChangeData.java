package com.minshigee.playerchanger.logic.change;

import com.minshigee.playerchanger.PlayerChanger;
import com.minshigee.playerchanger.domain.Participant;
import com.minshigee.playerchanger.domain.module.Data;
import com.minshigee.playerchanger.logic.change.shops.domain.Shop;
import com.minshigee.playerchanger.logic.game.GameData;
import com.minshigee.playerchanger.util.MessageUtil;
import com.minshigee.playerchanger.util.Util;
import com.mojang.datafixers.util.Pair;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.lang.reflect.Method;
import java.util.*;

public class ChangeData extends Data implements InventoryHolder {
    private final ArrayList<Pair<Integer, Method>> changeMethods = new ArrayList<>();
    public static HashMap<Participant, Integer> scoreData = new HashMap<>();
    private HashMap<Integer, ? extends Shop> shopData = new HashMap<>();

    public Integer getScore(Participant participant){
        return scoreData.get(participant);
    }

    public void updateScore(Participant participant, Integer score){
        try{
            scoreData.putIfAbsent(participant, 0);
            scoreData.replace(participant,getScore(participant) + score);
            MessageUtil.printLogToPlayer(participant.getPlayer(),"%s%d %s포인트를 지급합니다. %s{현재: %d점}".formatted(ChatColor.GOLD,score,ChatColor.GRAY,ChatColor.GREEN,getScore(participant)));
        }
        catch (Exception e){
            MessageUtil.printConsoleLog(e.getMessage());
        }
    }

    public boolean useScore(Participant participant, Integer value){
        Integer score = getScore(participant);
        if(score < value) {
            return false;
        }
        updateScore(participant,-value);
        return true;
    }

    public void clearScores(){
        scoreData.clear();
    }

    public void addMethod(Integer code, Method method)
    {
        MessageUtil.printConsoleLog("%s를 Change 목록에 추가합니다.".formatted(method.getName()));
        changeMethods.add(new Pair<>(code, method));
    }

    public void executeMethod(Integer code, Player p1, Player p2){
        changeMethods.stream()
                .filter(pair -> pair.getFirst().equals(code))
                .map(Pair::getSecond)
                .forEach(method -> {
                            try {
                                method.invoke(
                                        PlayerChanger.getInstanceOfClass(PlayerChanger.getContainerKeys().get(code)));
                            } catch (Exception e) {
                                MessageUtil.printConsoleLog("change에 실패했습니다.");
                            }
                        }
                );
    }

    public void executeMethods(Player p1, Player p2){
        changeMethods.forEach(pair ->{
            int code = pair.getFirst();
            Method method = pair.getSecond();
            try {
                method.invoke(PlayerChanger.getInstanceOfClass(method.getDeclaringClass()), p1, p2);
            } catch (Exception e) {
                MessageUtil.printConsoleLog(e.getMessage());
                MessageUtil.printConsoleLog("change에 실패했습니다.");
            }
        });
    }

    @Override
    public Inventory getInventory() {
        return null;
    }
}
