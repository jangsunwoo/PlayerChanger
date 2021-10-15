package com.minshigee.playerchanger.logic;

import com.minshigee.playerchanger.PlayerChanger;
import com.minshigee.playerchanger.constant.ConsoleLogs;
import com.minshigee.playerchanger.domain.CommandInfo;
import com.minshigee.playerchanger.domain.annitation.NeedOP;
import com.minshigee.playerchanger.logic.game.GameController;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

public class CommandsExecutor implements CommandExecutor{

    private final CommandInfo[] gameCommands = {
            new CommandInfo("help", getMethod(GameController.class,"executeHelp"))
    };
    private final CommandInfo[] abilityCommands = {

    };
    private final CommandInfo[] missionCommands = {

    };
    private final CommandInfo[] changeCommands = {

    };

    private final ArrayList<CommandInfo[]> commands = new ArrayList<>(
            Arrays.asList(
                    gameCommands,
                    abilityCommands,
                    missionCommands,
                    changeCommands
            )
    );

    private Method getMethod(Class cls, String str){
        try {
            return Arrays.stream(cls.getDeclaredMethods())
                    .filter(method -> method.getName().equals(str))
                    .findFirst().get();
        }
        catch (Exception e){
            ConsoleLogs.printConsoleLog(ChatColor.RED + cls.getName() + "의 " + str + "을 불러오지 못했습니다.");
        }
        return null;
    }

    private void executeCommandMethod(Method method, Player player, String[] args){
        try {
            method.invoke(PlayerChanger.getInstanceOfClass(method.getDeclaringClass()), player, args);
        }
        catch (Exception e){
            ConsoleLogs.printConsoleLog(ChatColor.RED + "명령어 실행 중에 오류가 발생했습니다.");
        }
    }

    private void validatePermissionAndExecute(Method method, Player player, String[] args){
        NeedOP needOp = method.getDeclaredAnnotation(NeedOP.class);
        if(needOp == null){
            executeCommandMethod(method, player, args);
            return;
        }
        if(needOp.value() && player.isOp()){
            executeCommandMethod(method, player, args);
            return;
        }
        ConsoleLogs.printLogToPlayer(player,ChatColor.RED + "권한이 없는 명령어입니다.");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if(!(sender instanceof Player)){
            ConsoleLogs.printConsoleLog(ChatColor.RED + "콘솔에서는 실행할 수 없는 명령어입니다.");
            return true;
        }
        if(args.length < 1)
            return true;

        AtomicBoolean isExecute = new AtomicBoolean(false);
        for(CommandInfo[] cmds : commands){
            Arrays.stream(cmds)
                    .filter(_cmd -> _cmd.command.equalsIgnoreCase(args[0]))
                    .findFirst().ifPresent(_cmd ->{
                        isExecute.set(true);
                        validatePermissionAndExecute(_cmd.method, (Player)sender, args);
                    });
            if(isExecute.get())
                break;
        }
        return true;
    }


}
