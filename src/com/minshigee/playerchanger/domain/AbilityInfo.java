package com.minshigee.playerchanger.domain;

import com.minshigee.playerchanger.domain.abilities.Ability;
import com.minshigee.playerchanger.domain.abilities.Dandaegi;
import com.minshigee.playerchanger.domain.abilities.interfaces.AbilityCode;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;

public class AbilityInfo {

    public static final HashMap<AbilityCode, Ability> abilityMap = new HashMap<>(){{
        put(AbilityCode.Dandaegi, new Dandaegi());
    }};

    public static HashMap<AbilityCode, ArrayList<Player>> participantAbilityInfo = new HashMap<>(){{
        put(AbilityCode.Dandaegi, new ArrayList<>());
    }};

    public static HashMap<Player, AbilityCode> abilityParticipantInfo = new HashMap<>();

    public static boolean isExistParticipantInAbilities(Player player, AbilityCode code){
        return participantAbilityInfo.get(code).stream().anyMatch(player::equals);
    }

    public static void resetParticipantAbility(Player player){
        player.sendMessage(ChatColor.RED + player.getName() + "의 능력을 초기화 하는 중.");
        try {
            (participantAbilityInfo.get(abilityParticipantInfo.get(player))).remove(player);
            abilityParticipantInfo.remove(player);
        }
        catch (Exception exc){
            player.sendMessage(ChatColor.RED + "[PlayerChanger]: 능력 데이터 슬롯이 비어있습니다. 재설정합니다.");
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
}
