package com.minshigee.playerchanger.util;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class Util {
    public static void makeActionBarMessage(Player player, String msg){
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(msg));
    }
    //TODO PARTICLE DATA
    public static void makeGettingAbilityParticle(Player player, Particle particle){
        for(int i = 0; i < 5; i++)
            player.spawnParticle(particle, player.getLocation().add(0,i,0), 5);
    }

    //TODO SOUND DATA
    public static void makeGettingAbilitySound(Player player){
        player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_AMBIENT, 2f, 1f);
    }
    public static void makeSoundInvChangedPlayer(Player player){
        player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_USE, 2f, 1f);
    }
    public static void makeCountSound(boolean b, Player player){
        if(b) {
            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
        }
    }
}
