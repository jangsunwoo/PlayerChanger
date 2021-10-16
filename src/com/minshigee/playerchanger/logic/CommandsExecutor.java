package com.minshigee.playerchanger.logic;

import com.minshigee.playerchanger.PlayerChanger;
import com.minshigee.playerchanger.domain.annotation.MappingCommand;
import com.minshigee.playerchanger.logic.game.GameData;
import com.minshigee.playerchanger.util.ConsoleLogs;
import com.minshigee.playerchanger.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;

public class CommandsExecutor implements CommandExecutor{

    private HashMap<String,Method> availableCommands = new HashMap<>();

    public CommandsExecutor(){
        validateCommand();
    }

    private void validateCommand(){
        Util.getMappableControllers().forEach(aClass -> Arrays.stream(aClass.getDeclaredMethods())
                .filter(method -> method.getDeclaredAnnotation(MappingCommand.class) != null).filter(method -> method.getParameterCount() == 2)
                .forEach(method -> {
                    MappingCommand metaCommand = method.getDeclaredAnnotation(MappingCommand.class);
                    registerAvailableMethods(metaCommand.arg(), method);
                }));
    }

    private void registerAvailableMethods(String arg, Method method){
        availableCommands.put(arg,method);
    }

    private void executeAvailableMethod(Player sender, String[] args){
        Method method = availableCommands.get(args[0]);
        MappingCommand metaCommand = method.getDeclaredAnnotation(MappingCommand.class);
        if(metaCommand.needOp() && !sender.isOp())
            return;
        if(Arrays.stream(metaCommand.states()).noneMatch(gameState -> gameState.equals(GameData.getGameState())))
            return;
        try {
            method.invoke(PlayerChanger.getInstanceOfClass(method.getDeclaringClass()),sender,args);
        }
        catch (Exception e){
            ConsoleLogs.printLogToPlayer(sender, ChatColor.RED + args[0] + " 명령을 실행하는데 실패했습니다.");
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if(!(sender instanceof Player)){
            ConsoleLogs.printConsoleLog(ChatColor.RED + "콘솔에서는 실행할 수 없는 명령어입니다.");
            return true;
        }
        if(args.length < 1)
            return true;

        executeAvailableMethod((Player)sender, args);

        return true;
    }
}
