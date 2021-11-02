package com.minshigee.playerchanger.logic.change.shops.domain;

import com.minshigee.playerchanger.domain.Participant;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class Shop implements InventoryHolder {

    protected Inventory inventory = null;

    protected void init(){
        inventory = Bukkit.createInventory(this, InventoryType.CHEST,"default");
    }

    protected void makeInventory(Participant participant){

    }

    public Inventory getParticipantShop(Participant participant){
        makeInventory(participant);
        return inventory;
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
