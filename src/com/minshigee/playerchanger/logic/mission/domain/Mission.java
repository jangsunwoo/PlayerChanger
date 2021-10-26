package com.minshigee.playerchanger.logic.mission.domain;

import com.minshigee.playerchanger.domain.Participant;
import com.minshigee.playerchanger.domain.Role;
import com.minshigee.playerchanger.logic.game.GameData;
import com.minshigee.playerchanger.logic.mission.domain.interface_.IMission;
import com.mojang.datafixers.util.Pair;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class Mission implements IMission {

    protected Integer num;
    protected HashMap<Participant,Integer> participants = new HashMap<>();
    private Integer point;
    private Player clearPlayer = null;
    protected String description = "";

    protected ArrayList<Material> registerBlockMaterials = new ArrayList<>();
    protected ArrayList<Pair<Material,Integer>> registerItemMaterials = new ArrayList<>();

    public ArrayList<Material> getRegisterBlockMaterials() {return registerBlockMaterials;}
    public ArrayList<Pair<Material, Integer>> getRegisterItemMaterials() {return registerItemMaterials;}

    @Override
    public <T> Player updateMission(T event) {
        return null;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public Mission(int num) {
        Set<Participant> tmpParticipants = GameData.getParticipantsByRole(Role.Participant);
        for (Participant participant : tmpParticipants){
            participants.put(participant,0);
        }
        this.point = 10;
        this.num = num;
        this.description = "%d) ".formatted(num);
    }

    protected void setPoint(int point){this.point = point;}
    public Integer getNum() {return this.num;}
    public Integer getPoint(){return this.point;}

    public Player getClearPlayer(){return clearPlayer;}
    protected void setClearPlayer(Player player){this.clearPlayer = player;}

}
