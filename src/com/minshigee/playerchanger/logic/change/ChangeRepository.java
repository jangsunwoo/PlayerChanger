package com.minshigee.playerchanger.logic.change;

import com.minshigee.playerchanger.domain.Participant;
import com.minshigee.playerchanger.domain.annotation.MappingChange;
import com.minshigee.playerchanger.domain.module.Repository;
import com.minshigee.playerchanger.logic.game.GameData;
import com.minshigee.playerchanger.util.Util;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class ChangeRepository extends Repository<ChangeData> {
    public ChangeRepository(ChangeData localDB, Integer viewCode) {
        super(localDB,viewCode);
        registerChangeMethod(this.getClass());
    }

    public void changePlayers(Player player, String[] args){
        localDB.executeMethods();
    }

    public void registerChangeMethod(Integer code, Method method){
        localDB.addMethod(code, method);
    }

    @MappingChange
    public void changeInventory(){
        ArrayList<Participant> pars = new ArrayList<>(GameData.getParticipantsAlive());
        ArrayList<Participant> tmpPars = Util.getShuffledAliveParticipants();
        ItemStack[] tmpInv;
        for(int i = 0; i < pars.size(); i++){
            Player p = pars.get(i).getPlayer();
            Player tmpP = tmpPars.get(i).getPlayer();

            tmpInv = p.getInventory().getContents();
            p.getInventory().setContents(tmpP.getInventory().getContents());
            tmpP.getInventory().setContents(tmpInv);

            tmpInv = p.getInventory().getArmorContents();
            p.getInventory().setArmorContents(tmpP.getInventory().getArmorContents());
            tmpP.getInventory().setArmorContents(tmpInv);


            tmpInv = p.getInventory().getExtraContents();
            p.getInventory().setExtraContents(tmpP.getInventory().getExtraContents());
            tmpP.getInventory().setExtraContents(tmpInv);
        }
    }
}
