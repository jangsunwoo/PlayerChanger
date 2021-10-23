package com.minshigee.playerchanger.domain.module;

import com.minshigee.playerchanger.PlayerChanger;
import com.minshigee.playerchanger.domain.annotation.IsRepository;
import com.minshigee.playerchanger.domain.annotation.MappingChange;
import com.minshigee.playerchanger.logic.change.ChangeController;
import com.minshigee.playerchanger.logic.view.ViewData;
import com.minshigee.playerchanger.util.MessageUtil;

import java.util.Arrays;

@IsRepository
public class Repository<T> {
    protected Integer viewCode;
    protected T localDB;

    public Repository(T localDB, Integer viewCode) {this.localDB = localDB;this.viewCode = viewCode;}

    protected void registerChangeMethod(Class cls) {
        try {
            Arrays.stream(cls.getDeclaredMethods())
                    .filter(method -> method.getDeclaredAnnotation(MappingChange.class) != null)
                    .forEach(method -> {
                        ((ChangeController)PlayerChanger.getInstanceOfClass(ChangeController.class))
                                .registerChangeMethod(viewCode, method);
                        MessageUtil.printConsoleLog("%d의 %s를 등록했습니다.".formatted(viewCode,method.getName()));
                    });
        }
        catch (Exception e){
            MessageUtil.printConsoleLog("Change Method 등록에 실패했습니다.");
        }
    }
}
