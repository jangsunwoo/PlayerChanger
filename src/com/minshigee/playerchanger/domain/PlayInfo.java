package com.minshigee.playerchanger.domain;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class PlayInfo {

    public static Integer maxParticipantSize = 16;
    public static Integer timeCycle = 60;
    public static Integer blockRemoveCycle = 10;

    public static Integer inventoryChangePercent = 30;
    public static PCH_Status gameStatus = PCH_Status.DISABLED;
    public static ArrayList<Player> leaveParticipants = new ArrayList<>();
    public static ArrayList<Player> participants = new ArrayList<>();
    public static ArrayList<Location> startLocations = new ArrayList<>();

    public static int addParticipant(Player player){
        if(participants.size() >= maxParticipantSize)
            return 1;
        if(participants.contains(player))
            return 2;
        leaveParticipants.remove(player);
        participants.add(player);
        return 0;
    }

    public static void delParticipant(Player player){
        if(!participants.contains(player)){
            return;
        }
        participants.remove(player);
        String msg = String.format(
                ChatColor.GREEN +
                "[PlayerChanger]: (%s)유저가 탈락했습니다. %d명 남았습니다.",player.getName(), participants.size()
        );
        participants.forEach(p -> {
            p.sendMessage(msg);
        });
    }

    public static boolean isExistParticipant(Player player){
        return participants.contains(player);
    }

    public static boolean addStartLocation(Location loc){
        startLocations.add(loc);
        return true;
    }

    public static void addLeaveParticipant(Player player){
        if(!leaveParticipants.contains(player)){
            leaveParticipants.add(player);
        }
    }

    public static void delLeaveParticipant(Player player){
        leaveParticipants.remove(player);
    }

    public static boolean isExistLeaveParticipant(Player player){
        return leaveParticipants.contains(player);
    }

    public static boolean delStartLocation(int idx){
        try {
            startLocations.remove(idx);
            return true;
        }
        catch (Exception exception){
            return false;
        }
    }

    public static void clearStartLocations(){
        startLocations.clear();
    }

    public static void resetPlayInfo() {
        PlayInfo.gameStatus = PCH_Status.DISABLED;
        PlayInfo.leaveParticipants.stream()
                .filter(player -> player.getGameMode() == GameMode.SPECTATOR)
                .forEach(player -> player.setGameMode(GameMode.SURVIVAL));
        PlayInfo.participants.clear();
        PlayInfo.leaveParticipants.clear();
    }
    public static void resetPlayerAttribute(Player player){
        Arrays.stream(Attribute.values()).forEach(attribute -> resetAttribute(player,attribute));
    }
    private static void resetAttribute(Player player, Attribute attribute){
        Objects.requireNonNull(player.getAttribute(attribute)).setBaseValue(
                Objects.requireNonNull(player.getAttribute(attribute)).getDefaultValue()
        );
    }
}
