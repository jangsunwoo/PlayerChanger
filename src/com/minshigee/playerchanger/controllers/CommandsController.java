package com.minshigee.playerchanger.controllers;

import com.minshigee.playerchanger.repositories.CommandsRepository;
import com.minshigee.playerchanger.repositories.interfaces.NeedPermission;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;

public class CommandsController implements CommandExecutor {

    private CommandsRepository repository = null;

    public CommandsController(CommandsRepository repo){
        this.repository = repo;
    }
    private final String[] cmds = {
            "Ready",
            "List",
            "Help",
            "Leave",
            "Spectator",
            "Start",
            "Stop",
            "Ability",
            "ReadyAll",
            "Loc",
            "AddLoc",
            "DelLoc"
    };

    public boolean checkPermission(Player player, Method method){
        if(
                method.isAnnotationPresent(NeedPermission.class) &&
                method.getAnnotation(NeedPermission.class).needOp()
        ){
            if(!player.isOp()){
                player.sendMessage(ChatColor.RED + "[PlayerChanger] 권한이 없습니다.");
                return false;
            }
        }
        return true;
    }
    public void excuteCommand(Player player, Optional<Method> method) {
        if(!checkPermission(player, method.get()))
            return;
        try {
            method.get().invoke(repository, player);
        }
        catch (Exception e){
            player.getServer().getConsoleSender().sendMessage(ChatColor.RED + "[PlayerChanger]: 명령어 Invoke 에러!");
        }
    }

    private Optional<Method> findMethod(String methodName){
        return Arrays.stream(CommandsRepository.class.getDeclaredMethods()).filter(method -> method.getName().equals(methodName)).findFirst();
    }

    private boolean isCommandName(String cmd, String cmdName){
        return cmd.equalsIgnoreCase(cmdName);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(!(commandSender instanceof Player)){
            commandSender.getServer().getConsoleSender().sendMessage(ChatColor.RED + "[PlayerChanger]: 이 명령어는 플레이어만 사용이 가능합니다.");
            return true;
        }

        Player player = (Player) commandSender;

        if(!command.getName().equalsIgnoreCase("pch"))
            return true;

        if(strings.length == 0){
            player.sendMessage(ChatColor.GREEN + "[PlayerChanger]: /pch help 를 입력하여 명령어를 확인하세요.");
            return true;
        }

        if(strings.length == 1) {
            String arg = strings[0];
            String excuteName = Arrays.stream(cmds).filter(cmd -> isCommandName(arg, cmd)).findFirst().get();

            if (excuteName.equals(null)) {
                player.sendMessage(ChatColor.RED + "[PlayerChanger]: 유효하지 않은 명령입니다.");
                return true;
            }
            Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "[PlayerChanger]: " + player.getName() + " " + "execute" + excuteName);
            excuteCommand(player, findMethod("execute" + excuteName));
            return true;
        }

        if(player.isOp() && strings[0].equalsIgnoreCase("delloc")){
            repository.executeDelLoc(player, Integer.getInteger(strings[1]));
        }
        return true;
    }
}
