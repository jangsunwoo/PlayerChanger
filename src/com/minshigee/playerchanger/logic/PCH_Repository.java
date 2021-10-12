package com.minshigee.playerchanger.logic;

import com.minshigee.playerchanger.data.MetaData;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class PCH_Repository {

    public int minInvPer = 50;

    public void changeInventory(Server server) {

        Random random = new Random();
        int val = random.nextInt(100);
        boolean changeCode = PCH_Scheduler.invPer < val;
        PCH_Scheduler.invPer = minInvPer + random.nextInt(100 - minInvPer);

        if (changeCode){
            if(minInvPer > 70) {
                return;
            }
            minInvPer += (100 - minInvPer) / 2;
            return;
        }
        minInvPer /= 2;

        List<Player> players = MetaData.participants.stream()
                .collect(Collectors.toList());
        Collections.shuffle(players);

        for(int i = 0; i < players.size() / 2; i++){
            Player from = players.get(i);
            Player to = players.get(players.size() - i - 1);
            Inventory tmpInv = Bukkit.createInventory(null, InventoryType.PLAYER, "tmp Inventory");
            ItemStack[] tmpArmor = to.getInventory().getArmorContents();
            tmpInv.setContents(to.getInventory().getContents());

            to.getInventory().setContents(from.getInventory().getContents());
            to.getInventory().setArmorContents(from.getInventory().getArmorContents());

            from.getInventory().setContents(tmpInv.getContents());
            from.getInventory().setArmorContents(tmpArmor);

            to.updateInventory();
            from.updateInventory();
        }
    }
}
