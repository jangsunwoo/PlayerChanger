package com.minshigee.playerchanger.util;

import com.minshigee.playerchanger.PlayerChanger;
import com.minshigee.playerchanger.domain.Participant;
import com.minshigee.playerchanger.domain.annotation.IsController;
import com.minshigee.playerchanger.domain.module.Controller;
import com.minshigee.playerchanger.logic.game.GameData;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Skull;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.*;
import java.util.stream.Collectors;

public class Util {
    public static ArrayList<Class> getMappableControllers(){
        return (ArrayList<Class>) PlayerChanger.getContainerKeys().stream()
                .filter(aClass -> {
                    return aClass.getDeclaredAnnotation(IsController.class) != null;
                })
                .filter(aClass -> {
                    try {
                        return ((Controller)PlayerChanger.getInstanceOfClass(aClass)).getIsAvailable();
                    }
                    catch (Exception e){
                        MessageUtil.printConsoleLog(ChatColor.RED + "Controller의 isAvailable 참조에 실패했습니다.");
                        return false;
                    }
                }).collect(Collectors.toList());
    }

    public static ArrayList<Participant> getShuffledAliveParticipants(){
        ArrayList<Participant> res = new ArrayList<>(GameData.getParticipantsAlive());
        Collections.shuffle(res);
        return res;
    }

    public static ItemStack createItem(String name, Material material, List<String> lore){
        ItemStack item = new ItemStack(material, 1);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(name);
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack createPlayerHead(Player player, String name, List<String> lore){
        ItemStack item = createItem(name, Material.SKELETON_SKULL, lore);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        assert meta != null;
        meta.setOwningPlayer(player);
        item.setItemMeta(meta);
        return item;
    }
}
