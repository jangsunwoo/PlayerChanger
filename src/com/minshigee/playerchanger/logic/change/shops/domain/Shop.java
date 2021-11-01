package com.minshigee.playerchanger.logic.change.shops.domain;

import com.minshigee.playerchanger.domain.Participant;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

public class Shop implements IShop{

    protected Inventory inventory = null;

    public Shop() {
        init();
    }

    protected void init(){
        inventory = Bukkit.createInventory(null, InventoryType.CHEST,"default");
    }

    protected void makeInventory(Participant participant){

    }

    public Inventory getParticipantShop(Participant participant){
        makeInventory(participant);
        return inventory;
    }

    @Override
    public Inventory getInventory() {
        return null;
    }
}
