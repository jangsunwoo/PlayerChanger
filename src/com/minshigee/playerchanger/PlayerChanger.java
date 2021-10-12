package com.minshigee.playerchanger;

import com.minshigee.playerchanger.commands.PCH_Commands;
import com.minshigee.playerchanger.events.EventGateway;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class PlayerChanger extends JavaPlugin {

    public static PlayerChanger playerChanger = null;

    @Override
    public void onEnable() {

        playerChanger = this;
        PCH_Commands cmdExecutor = new PCH_Commands();
        getServer().getPluginManager().registerEvents(
                new EventGateway(),
                this
        );
        getCommand("pch").setExecutor(cmdExecutor);
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[PlayerChanger] Plugin is enabled!");
    }

    @Override
    public void onDisable() {
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[PlayerChanger] Plugin is disabled!");
    }
}
