package com.minshigee.playerchanger.logic.mission.domain;

import com.minshigee.playerchanger.domain.Participant;
import com.minshigee.playerchanger.domain.Role;
import com.minshigee.playerchanger.logic.game.GameData;
import com.minshigee.playerchanger.logic.mission.domain.interface_.IMission;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Set;

public class Mission implements IMission {

    protected Integer num;
    protected HashMap<Participant,Integer> participants = new HashMap<>();
    private Integer point;
    private Player clearPlayer = null;
    protected String prefix = "";

    @Override
    public <T> Player updateMission(T event) {
        return null;
    }

    @Override
    public String getDescription() {
        return null;
    }

    protected String description = "";

    public Mission(int num) {
        Set<Participant> tmpParticipants = GameData.getParticipantsByRole(Role.Participant);
        for (Participant participant : tmpParticipants){
            participants.put(participant,0);
        }
        this.point = 1;
        this.num = num;
    }

    protected void setPoint(int point){this.point = point;}
    public Integer getNum() {return this.num;}
    public Integer getPoint(){return this.point;}

    public Player getClearPlayer(){return clearPlayer;}
    protected void setClearPlayer(Player player){this.clearPlayer = player;}

}
