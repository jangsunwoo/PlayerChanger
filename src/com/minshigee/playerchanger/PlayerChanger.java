package com.minshigee.playerchanger;

import com.minshigee.playerchanger.commands.PCH_Commands;
import com.minshigee.playerchanger.events.EventGateway;
import com.minshigee.playerchanger.logic.PCH_Scheduler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class PlayerChanger extends JavaPlugin {

    public static PlayerChanger playerChanger = null;

    @Override
    public void onEnable() {

        playerChanger = this;
        init();
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[PlayerChanger] Plugin is enabled!");
    }

    @Override
    public void onDisable() {
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[PlayerChanger] Plugin is disabled!");
    }

    private void init(){

        PCH_Commands cmdExecutor = new PCH_Commands();

        getServer().getPluginManager().registerEvents(
                new EventGateway(),
                this
        );

        getCommand("pch").setExecutor(cmdExecutor);

        PCH_Scheduler.playStatusBoardManager = Bukkit.getScoreboardManager();
    }
}
