package com.minshigee.playerchanger.logic.game;

import com.minshigee.playerchanger.domain.module.Repository;
import com.minshigee.playerchanger.util.ConsoleLogs;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class GameRepositoy extends Repository<GameData> {
    public GameRepositoy(GameData localDB) {
        super(localDB);
    }

    public void makePlayerSetter(Player player){
        localDB.addSetter(player);
        ConsoleLogs.printLogToPlayer(player, ChatColor.AQUA + "세팅모드가 되었습니다.");
    }

    public void initBeforeStart(){
        localDB.clearSettingData();
    }

}
