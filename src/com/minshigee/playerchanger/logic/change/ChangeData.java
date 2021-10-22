package com.minshigee.playerchanger.logic.change;

import com.minshigee.playerchanger.PlayerChanger;
import com.minshigee.playerchanger.domain.module.Data;
import com.minshigee.playerchanger.util.MessageUtil;
import com.mojang.datafixers.util.Pair;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class ChangeData extends Data {
    private final ArrayList<Pair<Integer, Method>> changeMethods = new ArrayList<>();

    public void addMethod(Integer code, Method method){
        changeMethods.add(new Pair<>(code, method));
    }

    public void excuteMethod(Integer code){
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

    public void executeMethods(){
        changeMethods.forEach(pair ->{
            int code = pair.getFirst();
            Method method = pair.getSecond();
            try {
                method.invoke(PlayerChanger.getInstanceOfClass(PlayerChanger.getContainerKeys().get(code)));
            } catch (Exception e) {
                MessageUtil.printConsoleLog("change에 실패했습니다.");
            }
        });
    }

}
