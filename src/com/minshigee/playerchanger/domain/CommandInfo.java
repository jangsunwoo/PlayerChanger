package com.minshigee.playerchanger.domain;

import java.lang.reflect.Method;

public class CommandInfo {
    public String command;
    public Method method;

    public CommandInfo(String command, Method method) {
        this.command = command;
        this.method = method;
    }
}
