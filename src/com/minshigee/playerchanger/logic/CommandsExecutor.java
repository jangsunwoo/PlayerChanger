package com.minshigee.playerchanger.logic;

import com.minshigee.playerchanger.PlayerChanger;
import com.minshigee.playerchanger.domain.annotation.MappingCommand;
import com.minshigee.playerchanger.logic.game.GameData;
import com.minshigee.playerchanger.util.MessageUtil;
import com.minshigee.playerchanger.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class CommandsExecutor implements CommandExecutor{

    private HashMap<String, ArrayList<Method>> availableCommands = new HashMap<>();

    public CommandsExecutor(){
        MessageUtil.printConsoleLog(ChatColor.DARK_AQUA + "커맨드 등록을 시작합니다.");
        validateCommand();
    }

    private void validateCommand(){
        Util.getMappableControllers().forEach(aClass -> {
            Arrays.stream(aClass.getDeclaredMethods())
                    .filter(method -> method.getDeclaredAnnotation(MappingCommand.class) != null).filter(method -> method.getParameterCount() == 2)
                    .forEach(method -> {
                        MappingCommand metaCommand = method.getDeclaredAnnotation(MappingCommand.class);
                        registerAvailableMethods(metaCommand.arg(), method);
                        MessageUtil.printConsoleLog(ChatColor.GREEN + metaCommand.arg() + " 커맨드가 등록되었습니다.");
                    });
        });
        MessageUtil.printConsoleLog(ChatColor.DARK_AQUA + "커맨드 등록이 끝났습니다.");
    }

    private void registerAvailableMethods(String arg, Method method){
        if(availableCommands.get(arg) == null){
            availableCommands.put(arg,new ArrayList<>());
        }
        availableCommands.get(arg).add(method);
    }

    private void executeAvailableMethod(Player sender, String[] args){
        ArrayList<Method> methods = availableCommands.get(args[0]);
        methods.stream().forEach(method -> {
            MappingCommand metaCommand = method.getDeclaredAnnotation(MappingCommand.class);
            if(metaCommand.needOp() && !sender.isOp())
                return;
            if(Arrays.stream(metaCommand.states()).noneMatch(gameState -> gameState.equals(GameData.getGameState())))
                return;
            try {
                method.invoke(PlayerChanger.getInstanceOfClass(method.getDeclaringClass()),sender,args);
            }
            catch (Exception e){
                MessageUtil.printLogToPlayer(sender, ChatColor.RED + args[0] + " 명령을 실행하는데 실패했습니다.");
            }
        });
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if(!(sender instanceof Player)){
            MessageUtil.printConsoleLog(ChatColor.RED + "콘솔에서는 실행할 수 없는 명령어입니다.");
            return true;
        }
        if(args.length < 1)
            return true;

        executeAvailableMethod((Player)sender, args);
        return true;
    }
}
