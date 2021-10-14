package com.minshigee.playerchanger.util;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.Random;

public class Util {

    public static void makeActionBarMessage(Player player, String msg){
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(msg));
    }

    //TODO PARTICLE DATA
    public static void makeGettingAbilityParticle(Player player, Particle particle){
        Random random = new Random();
        for(int i = 0; i < 5; i++) {
            for (int j = -1; j <= 1; j+=1)
                for (int k = -1; k <= 1; k++) {
                    if(j * k != 0)
                        continue;
                    player.getLocation().getWorld()
                            .spawnParticle(particle, player.getLocation().add(j, i, k), 1);
                }
        }
    }

    public static void makeGameMainSound(){
        Bukkit.getServer().getOnlinePlayers().forEach(player -> {
            player.playSound(player.getLocation(), Sound.BLOCK_BELL_USE, 2f, 0.4f);
        });
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
