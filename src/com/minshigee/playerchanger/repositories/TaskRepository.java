package com.minshigee.playerchanger.repositories;

import com.minshigee.playerchanger.domain.AbilityInfo;
import com.minshigee.playerchanger.domain.abilities.interfaces.AbilityCode;
import com.minshigee.playerchanger.repositories.abilities.Blacksmith;
import org.bukkit.entity.Player;

public class TaskRepository {

    public void executeTaskParticipantsSecond(Player player){

    }

    public void executeTaskSpectatorsSecond(Player player){

    }

    public void executeEventSchedulesSecond(){

    }

    public void executeEventSchedules10Second(){
        ((Blacksmith) AbilityInfo.getAbilityObject(AbilityCode.Blacksmith))
                .enchantBlacksmithHandItem(); //대장장이 이벤트 븡록.
    }

    public void executeEventSchedules30Second(){

    }

    public void executeEventSchedules60Second(){

    }

}
