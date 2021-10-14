package com.minshigee.playerchanger.domain;

import com.minshigee.playerchanger.domain.abilities.Ability;
import com.minshigee.playerchanger.repositories.abilities.*;
import com.minshigee.playerchanger.domain.abilities.interfaces.AbilityCode;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class AbilityInfo {

    public static HashMap<AbilityCode, Ability> abilityMap = new HashMap<>();

    public static HashMap<AbilityCode, HashSet<Player>> participantAbilityInfo = new HashMap<>();

    public static HashMap<Player, AbilityCode> abilityParticipantInfo = new HashMap<>();

    public static boolean isExistParticipantInAbilities(Player player, AbilityCode code){
        return participantAbilityInfo.get(code).contains(player);
    }

    private static void initAbilityMap(){
        abilityMap = new HashMap<>(){{
            put(AbilityCode.Dandaegi, new Dandaegi());
            put(AbilityCode.Blacksmith, new Blacksmith());
            put(AbilityCode.GoldenPig, new GoldenPig());
            put(AbilityCode.Golem, new Golem());
            put(AbilityCode.Solidarity, new Solidarity());
            put(AbilityCode.Guy, new Guy());
            put(AbilityCode.Attacker, new Attacker());
        }};
    }

    private static void initParticipantAbilityInfo(){
        participantAbilityInfo = new HashMap<>(){{
            put(AbilityCode.Dandaegi, new HashSet<>());
            put(AbilityCode.Blacksmith, new HashSet<>());
            put(AbilityCode.GoldenPig, new HashSet<>());
            put(AbilityCode.Golem, new HashSet<>());
            put(AbilityCode.Solidarity, new HashSet<>());
            put(AbilityCode.Guy, new HashSet<>());
            put(AbilityCode.Attacker, new HashSet<>());
        }};
    }

    public static void resetAbilityDataSet(){
        abilityMap.clear();
        participantAbilityInfo.clear();
        abilityParticipantInfo.clear();
        initAbilityMap();
        initParticipantAbilityInfo();
    }

    public static void resetAbilityInfo(){
        try {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[PlayerChanger]: 능력 정보를 초기화합니다.");
            resetAbilityDataSet();
        }
        catch (Exception ex){
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[PlayerChanger]: 이미 비어져 있거나 진행에 실패했습니다.");
        }
    }

    public static void resetParticipantAbility(Player player){
        player.sendMessage(ChatColor.RED + player.getName() + "의 능력을 초기화 하는 중.");
        try {
            (participantAbilityInfo.get(abilityParticipantInfo.get(player))).remove(player);
            abilityParticipantInfo.remove(player);
        }
        catch (Exception exc){
            player.sendMessage(ChatColor.RED + "[PlayerChanger]: 능력 데이터 슬롯이 이미 비어있습니다.");
        }
    }

    public static void giveParticipantAbility(Player player, AbilityCode code){
        resetParticipantAbility(player);
        player.sendMessage(ChatColor.RED + player.getName() + "에게 " + abilityMap.get(code).getName() + " 능력을 지급합니다.");
        abilityParticipantInfo.put(player,code);
        participantAbilityInfo.get(code).add(player);
        abilityMap.get(code).executeAbility(player);
    }

    public static Ability getAbilityObject(AbilityCode code){
        return abilityMap.get(code);
    }

    public static void playAbilityHurtSound(Player player){
        getAbilityObject(
                abilityParticipantInfo.get(player)
        ).playHurtSound(player);
    }

    public static void playAbilityDeathSound(Player player){
        getAbilityObject(
                abilityParticipantInfo.get(player)
        ).playerDeathSound(player);
    }
}
