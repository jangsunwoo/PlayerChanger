package com.minshigee.playerchanger.logic.mission;

import com.minshigee.playerchanger.domain.Role;
import com.minshigee.playerchanger.domain.module.Data;
import com.minshigee.playerchanger.logic.game.GameData;
import com.minshigee.playerchanger.logic.mission.domain.Mission;
import com.minshigee.playerchanger.logic.mission.domain.MissionFactory;

import java.util.ArrayList;

public class MissionData extends Data {

    private ArrayList<? extends Mission> missions = new ArrayList<>();

    public void resetMissions(){
        this.missions = MissionFactory.createMissions(
                GameData.getParticipantsAlive().size()/2
        );
    }

    public ArrayList<? extends Mission> getMissions(){
        return missions;
    }

}
