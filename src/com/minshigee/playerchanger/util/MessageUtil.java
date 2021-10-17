package com.minshigee.playerchanger.util;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.awt.*;

public class MessageUtil {
    private static final String prefix = ChatColor.BLUE + "[ChangedChaser]:";

    public static void printMsgToAll(ChatMessageType type, String msg){
        printConsoleLog(msg);
        printMsgToPlayers(type, msg);
    }

    public static void printConsoleLog(String msg){
        Bukkit.getConsoleSender().sendMessage(prefix + msg);
    }

    public static void printMsgToPlayers(ChatMessageType type, String msg){
        Bukkit.getServer().getOnlinePlayers().forEach(player -> {
            printMsgToPlayer(type, player, msg);
        });
    }

    public static void printMsgToPlayer(ChatMessageType type, Player player, String msg){
        player.spigot().sendMessage(type,TextComponent.fromLegacyText(prefix + msg));
    }

    public static void printLogToPlayer(Player player, String msg){
        player.sendMessage(prefix + msg);
    }
}
