package com.minshigee.playerchanger.logic.game;

import com.minshigee.playerchanger.constant.ConsoleLogs;
import com.minshigee.playerchanger.constant.GameState;
import com.minshigee.playerchanger.constant.Role;
import com.minshigee.playerchanger.domain.Participant;
import com.minshigee.playerchanger.domain.module.Data;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class GameData extends Data {

    /*
    Game의 상태에 대한 정보와 수정 메소드의 집합입니다.
     */
    private static GameState gameState = GameState.Disable;

    public static GameState getGameState(){
        return gameState;
    }
    private static void setGameStatus(GameState status){
        gameState = status;
    }
    public static void offGameStatus(){
        setGameStatus(GameState.Disable);
    }
    public static void makeNextGameStatus(){
        switch (gameState){
            case Disable -> setGameStatus(GameState.Waitting);
            case Waitting, Freezing -> setGameStatus(GameState.Enable);
            case Enable -> setGameStatus(GameState.Freezing);
        }
    }

    /*
        게임 참가자들을 관리하는 HashSet과 관련 메소드의 집합입니다.
    */
    private static final HashSet<Participant> participants = new HashSet<>();

    public static Set<Participant> getParticipantsByRole(Role role){
        return participants.stream()
                .filter(participant -> participant.getRole().equals(role)).collect(Collectors.toSet());
    }

    public static void addPlayerToParticipants(Player player, Role role){
        boolean res = participants.add(new Participant(player, role));
        ConsoleLogs.printConsoleLog(ChatColor.GREEN + player.getName() + "님의 " + role.name() + "의 등록이 " + res + "로 처리됨.");
    }

    public static void clearParticipants(){
        participants.clear();
    }

    public static void removePlayerFromParticipants(Player player){
        getParticipantByPlayer(player).ifPresent(participants::remove);
    }

    public static boolean containPlayerFromParticipants(Player player){
        return getParticipantByPlayer(player).isPresent();
    }

    public static Optional<Participant> getParticipantByPlayer(Player player){
        return participants.stream()
                .filter(participant -> participant.getPlayer().equals(player))
                .findAny();
    }

    public static Role getRoleByPlayer(Player player){
        Optional<Participant> participant = getParticipantByPlayer(player);
        if(participant.isEmpty()){
            return null;
        }
        return participant.get().getRole();
    }

}
