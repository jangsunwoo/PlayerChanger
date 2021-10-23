package com.minshigee.playerchanger.logic.change;

import com.minshigee.playerchanger.PlayerChanger;
import com.minshigee.playerchanger.domain.module.Data;
import com.minshigee.playerchanger.util.MessageUtil;
import com.mojang.datafixers.util.Pair;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class ChangeData extends Data {
    private final ArrayList<Pair<Integer, Method>> changeMethods = new ArrayList<>();

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

}
