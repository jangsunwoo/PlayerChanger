package com.minshigee.playerchanger;

import com.minshigee.playerchanger.controllers.AbilitiesController;
import com.minshigee.playerchanger.controllers.CommandsController;
import com.minshigee.playerchanger.controllers.EventsController;
import com.minshigee.playerchanger.repositories.CommandsRepository;
import com.minshigee.playerchanger.repositories.EventsRepository;
import com.minshigee.playerchanger.views.GameView;
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

        getServer().getPluginManager().registerEvents(
                new EventsController(new EventsRepository()),
                this
        );
        getServer().getPluginManager().registerEvents(
                new AbilitiesController(),
                this
        );

        getCommand("pch").setExecutor(new CommandsController(new CommandsRepository()));

        GameView.playStatusBoardManager = Bukkit.getScoreboardManager();
    }
}
